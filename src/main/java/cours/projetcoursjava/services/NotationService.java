package cours.projetcoursjava.services;

import cours.projetcoursjava.entities.*;
import cours.projetcoursjava.repositories.*;
import cours.projetcoursjava.types.MoyenneTotale;
import cours.projetcoursjava.types.NotationTotale;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class NotationService {

    @Autowired
    private NotationRepository notationRepository;
    @Autowired
    private EtudiantRepository etudiantRepository;
    @Autowired
    private PartieDevoirRepository partieDevoirRepository;
    @Autowired
    private DevoirRepository devoirRepository;
    @Autowired
    private MatiereService matiereService;
    @Autowired
    private MatiereRepository matiereRepository;

    /**
     * Crée ou met à jour la note d'un étudiant pour une partie de devoir.
     */
    public Notation createOrUpdateNotation(Integer etudiantId, Integer partieId, Float note) throws Exception {
        Etudiant etu = etudiantRepository.findById(etudiantId).orElse(null);
        if (etu == null) {
            throw new Exception("Étudiant introuvable avec l'id " + etudiantId);
        }

        PartieDevoir partieDevoir = partieDevoirRepository.findById(partieId).orElse(null);
        if (partieDevoir == null) {
            throw new Exception("Partie de devoir introuvable avec l'id " + partieId);
        }

        // On regarde si la notation existe déjà
        Notation notation = notationRepository.findAll().stream()
                .filter(n ->
                        n.getEtudiant().getId().equals(etudiantId) && n.getPartieDevoir().getId().equals(partieId))
                .findFirst()
                .orElse(null);

        if (notation == null) {
            notation = new Notation();
            notation.setEtudiant(etu);
            notation.setPartieDevoir(partieDevoir);
        }

        // Sécuriser la note
        if (note < 0) note = 0f;
        if (note > partieDevoir.getPoints()) {
            // On peut décider de limiter la note au max
            note = partieDevoir.getPoints();
        }

        notation.setNote(note);

        return notationRepository.save(notation);
    }

    /**
     * Récupérer la somme des notes d'un étudiant pour un devoir
     */
    public Float getNoteDevoirEtudiant(Integer etudiantId, Integer devoirId) throws Exception
    {
        Etudiant etu = etudiantRepository.findById(etudiantId).orElse(null);
        if (etu == null) {
            throw new Exception("Étudiant introuvable avec l'id " + etudiantId);
        }

        Devoir devoir = devoirRepository.findById(devoirId).orElse(null);
        if (devoir == null) {
            throw new Exception("Devoir introuvable avec l'id " + devoirId);
        }

        List<Notation> notations = notationRepository.findByEtudiantAndDevoir(etudiantId, devoirId);
        // Somme des notes de toutes les parties
        return notations.stream()
                .map(Notation::getNote)
                .reduce(0f, Float::sum);
    }

    /**
     * Récupérer toutes les notations d'un étudiant
     */
    public List<Notation> getAllNotationsForEtudiant(Integer etudiantId) {
        return notationRepository.findByEtudiant(etudiantId);
    }

    /**
     * Récupérer les notes de tous les devoirs d'un étudiant
     * On peut renvoyer un map <DevoirId, somme des notes du devoir> par exemple
     */
    public Map<Integer, Float> getAllNotesDevoirsEtudiant(Integer etudiantId) {
        List<Notation> notations = notationRepository.findByEtudiant(etudiantId);
        // Regrouper par devoir
        return notations.stream().collect(
                        Collectors.groupingBy(
                                n -> n.getPartieDevoir().getDevoir().getId(),
                                Collectors.summingDouble(n -> n.getNote().doubleValue())
                        )
                ).entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> e.getValue().floatValue()
                ));
    }

    /**
     * Noter plusieurs parties d'un devoir pour un étudiant
     */
    public List<Notation> createOrUpdateMultipleNotations(Integer etudiantId, Integer devoirId, Map<Integer, Float> notesParPartie) throws Exception {
        List<Notation> resultat = new ArrayList<>();

        // On itère sur chaque partieId -> note
        for (Map.Entry<Integer, Float> entry : notesParPartie.entrySet()) {
            Integer partieId = entry.getKey();
            Float note = entry.getValue();
            // On vérifie d'abord que cette partie correspond bien au devoir qu'on nous a transmis
            PartieDevoir partie = partieDevoirRepository.findById(partieId).orElse(null);
            if (partie == null || !partie.getDevoir().getId().equals(devoirId)) {
                throw new Exception("La partie " + partieId + " ne correspond pas au devoir " + devoirId);
            }
            resultat.add(createOrUpdateNotation(etudiantId, partieId, note));
        }

        return resultat;
    }

    /**
     * Retourne les moyennes par matière pour un étudiant.
     */
    public List<MoyenneTotale> getAllMoyennesMatieresEtudiant(Integer etudiantId) {
        Etudiant etudiant = etudiantRepository.findById(etudiantId).orElse(null);
        if (etudiant == null) {
            return Collections.emptyList(); // Or throw an exception
        }

        List<Notation> notations = notationRepository.findByEtudiant(etudiantId);

        // Récupérer la liste unique des matières de l’étudiant
        Set<Matiere> matieres = notations.stream()
                .map(n -> n.getPartieDevoir().getDevoir().getMatiere())
                .collect(Collectors.toSet());

        return matieres.stream()
                .map(matiere -> {
                    Float moyenne = getMoyenneMatiereEtudiantCalcul(etudiantId, matiere.getId());
                    return new MoyenneTotale(etudiant, matiere, moyenne);
                })
                .collect(Collectors.toList());
    }

    /**
     * Calcul de la moyenne d’un étudiant sur une matière
     *
     * @param etudiantId
     * @param matiereId
     * @return MoyenneTotale object
     */
    public MoyenneTotale getMoyenneMatiereEtudiant(Integer etudiantId, Integer matiereId) {
        Etudiant etudiant = etudiantRepository.findById(etudiantId).orElse(null);
        Matiere matiere = matiereRepository.findById(matiereId).orElse(null);
        if (etudiant == null || matiere == null) {
            return null;
        }
        Float moyenne = getMoyenneMatiereEtudiantCalcul(etudiantId, matiereId);
        return new MoyenneTotale(etudiant, matiere, moyenne);
    }

    private Float getMoyenneMatiereEtudiantCalcul(Integer etudiantId, Integer matiereId) {
        // Pour simplifier, on part sur tous les Notation de l’étudiant, on filtre par matiere
        List<Notation> notations = notationRepository.findByEtudiant(etudiantId);

        Map<Devoir, List<Notation>> mapDevoirs = notations.stream()
                .collect(Collectors.groupingBy(n -> n.getPartieDevoir().getDevoir()));

        float sommeNoteXCoeff = 0;
        float sommeCoeff = 0;

        for (Map.Entry<Devoir, List<Notation>> entry : mapDevoirs.entrySet()) {
            Devoir d = entry.getKey();
            if (d.getMatiere().getId().equals(matiereId)) {
                float totalDevoir = 0;
                for (Notation n : entry.getValue()) {
                    totalDevoir += n.getNote();
                }
                // On multiplie la note du devoir par le coefficient
                sommeNoteXCoeff += totalDevoir * d.getCoefficient();
                sommeCoeff += d.getCoefficient();
            }
        }

        if (sommeCoeff == 0) return 0f;

        return sommeNoteXCoeff / sommeCoeff;
    }

    /**
     * Moyenne générale de l’étudiant
     *
     * @param etudiantId
     * @return MoyenneTotale object with null Matiere
     */
    public MoyenneTotale getMoyenneGeneraleEtudiant(Integer etudiantId) {
        Etudiant etudiant = etudiantRepository.findById(etudiantId).orElse(null);
        if (etudiant == null) {
            return null; // Or throw an exception
        }
        Float moyenneGenerale = getMoyenneGeneraleEtudiantCalcul(etudiantId);
        return new MoyenneTotale(etudiant, null, moyenneGenerale);
    }

    private Float getMoyenneGeneraleEtudiantCalcul(Integer etudiantId) {
        // Récupérons toutes les notations
        List<Notation> notations = notationRepository.findByEtudiant(etudiantId);

        // Groupons par devoir pour faire la somme des notes
        Map<Devoir, Float> noteParDevoir = new HashMap<>();

        for (Notation n : notations) {
            Devoir d = n.getPartieDevoir().getDevoir();
            noteParDevoir.putIfAbsent(d, 0f);
            noteParDevoir.put(d, noteParDevoir.get(d) + n.getNote());
        }

        float somme = 0;
        float sommeCoeff = 0;
        for (Map.Entry<Devoir, Float> e : noteParDevoir.entrySet()) {
            Devoir d = e.getKey();
            Float totalDevoir = e.getValue();
            somme += totalDevoir * d.getCoefficient();
            sommeCoeff += d.getCoefficient();
        }
        if (sommeCoeff == 0) return 0f;

        return somme / sommeCoeff;
    }

    /**
     * Supprimer toutes les notes d’un étudiant pour un devoir (cas d’annulation, etc.)
     */
    @Transactional
    public void deleteNotationsDevoirEtudiant(Integer etudiantId, Integer devoirId) throws Exception {
        int deletedCount = notationRepository.deleteNotationsByEtudiantAndDevoir(etudiantId, devoirId);

        if (deletedCount == 0) {
            throw new Exception("Aucune note trouvée pour l'étudiant avec l'id " + etudiantId + " et le devoir " + devoirId);
        }
    }

    /**
     * Récupérer toutes les notes totales de chaque devoir de chaque élève d'une matière
     * On renvoie un List<NotationTotale>
     */
    public List<NotationTotale> getNotesParDevoirParElevePourMatiere(Integer matiereId) {
        List<Devoir> devoirs = devoirRepository.findAll().stream()
                .filter(devoir -> devoir.getMatiere().getId().equals(matiereId))
                .toList();
        List<Etudiant> etudiants = etudiantRepository.findAll();
        List<NotationTotale> notationTotales = new ArrayList<>();

        for (Etudiant etudiant : etudiants) {
            for (Devoir devoir : devoirs) {
                Float noteTotale = 0f;
                List<Notation> notations = notationRepository.findByEtudiantAndDevoir(etudiant.getId(), devoir.getId());
                for (Notation notation : notations) {
                    noteTotale += notation.getNote();
                }
                if (noteTotale > 0) { // Only add if there are notes for this combination
                    NotationTotale notationTotale = new NotationTotale();
                    notationTotale.setEtudiant(etudiant);
                    notationTotale.setDevoir(devoir);
                    notationTotale.setNoteTotale(noteTotale);
                    notationTotales.add(notationTotale);
                }
            }
        }
        return notationTotales;
    }

    /**
     * Récupérer toutes les notes totales de chaque devoir de chaque élève d'une classe
     * On renvoie un List<NotationTotale>
     */
    public List<NotationTotale> getNotesParDevoirParElevePourClasse(Integer classeId) {
        List<Devoir> devoirs = devoirRepository.findAll().stream()
                .filter(devoir -> devoir.getClasse().getId().equals(classeId))
                .toList();
        List<Etudiant> etudiants = etudiantRepository.findAll().stream()
                .filter(etudiant -> etudiant.getClasse() != null && etudiant.getClasse().getId().equals(classeId))
                .toList();
        List<NotationTotale> notationTotales = new ArrayList<>();

        for (Etudiant etudiant : etudiants) {
            for (Devoir devoir : devoirs) {
                Float noteTotale = 0f;
                List<Notation> notations = notationRepository.findByEtudiantAndDevoir(etudiant.getId(), devoir.getId());
                for (Notation notation : notations) {
                    noteTotale += notation.getNote();
                }
                if (noteTotale > 0) { // Only add if there are notes for this combination
                    NotationTotale notationTotale = new NotationTotale();
                    notationTotale.setEtudiant(etudiant);
                    notationTotale.setDevoir(devoir);
                    notationTotale.setNoteTotale(noteTotale);
                    notationTotales.add(notationTotale);
                }
            }
        }
        return notationTotales;
    }
    /**
     * Récupérer toutes les notes totales de chaque devoir de chaque élève d'une matière par classe
     * On renvoie un Map<EtudiantId, Map<DevoirId, NoteTotale>>
     */
    public Map<Integer, Map<Integer, Float>> getNotesParDevoirParElevePourMatiereEtClasse(Integer matiereId, Integer classeId) {
        return notationRepository.findAll().stream()
                .filter(notation -> notation.getPartieDevoir().getDevoir().getMatiere().getId().equals(matiereId) &&
                        notation.getEtudiant().getClasse() != null && notation.getEtudiant().getClasse().getId().equals(classeId))
                .collect(Collectors.groupingBy(
                        notation -> notation.getEtudiant().getId(),
                        Collectors.groupingBy(
                                notation -> notation.getPartieDevoir().getDevoir().getId(),
                                Collectors.summingDouble(notation -> notation.getNote().doubleValue())
                        )
                ))
                .entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> e.getValue().entrySet().stream()
                                .collect(Collectors.toMap(Map.Entry::getKey, v -> v.getValue().floatValue()))
                ));
    }

    /**
     * Récupérer toutes les notes totales d'un devoir de chaque élève.
     * On renvoie un Map<EtudiantId, NoteTotale>
     */
    public List<NotationTotale> getNotesParEtudiantPourDevoir(Integer devoirId) throws Exception
    {
        List<Object[]> results = notationRepository.findTotalNotesByDevoirGroupByEtudiant(devoirId);
        List<NotationTotale> notationTotales = new ArrayList<>();
        Devoir devoir = devoirRepository.findById(devoirId).orElse(null);

        if (devoir == null) {
            // Gérer le cas où le devoir n'existe pas, éventuellement lancer une exception
            return notationTotales;
        }

        for (Object[] result : results) {
            Integer etudiantId = (Integer) result[0];
            Double totalNote = (Double) result[1];
            Etudiant etudiant = etudiantRepository.findById(etudiantId).orElse(null);

            if (etudiant == null) {
                throw new Exception("l'étudiant avec l'id " + etudiantId + " n'existe pas");
            }

            if (etudiant != null) {
                NotationTotale notationTotale = new NotationTotale();
                notationTotale.setNoteTotale(totalNote.floatValue());
                notationTotale.setEtudiant(etudiant);
                notationTotale.setDevoir(devoir);
                notationTotales.add(notationTotale);
            }
        }
        return notationTotales;
    }

}