package cours.projetcoursjava.controllers;

import cours.projetcoursjava.entities.Classe;
import cours.projetcoursjava.entities.Devoir;
import cours.projetcoursjava.entities.Etudiant;
import cours.projetcoursjava.entities.Notation;
import cours.projetcoursjava.services.ClasseService;
import cours.projetcoursjava.services.DevoirService;
import cours.projetcoursjava.services.EtudiantService;
import cours.projetcoursjava.services.NotationService;
import cours.projetcoursjava.types.MoyenneTotale;
import cours.projetcoursjava.types.NotationTotale;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Contrôleur pour gérer toutes les routes relatives aux notations
 */
@RestController
@RequestMapping("/api/notation")
public class NotationController {

    @Autowired
    private NotationService notationService;
    @Autowired
    private ClasseService classeService;
    @Autowired
    private EtudiantService etudiantService;
    @Autowired
    private DevoirService devoirService;

    @GetMapping("/etudiant/{etudiantId}/devoir/{devoirId}")
    public ResponseEntity<NotationTotale> getNoteTotaleDevoirEtudiant(
            @PathVariable Integer etudiantId,
            @PathVariable Integer devoirId
    ) {
        Etudiant etudiant = null;
        try {
            etudiant = etudiantService.GetOneEtudiant(etudiantId);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        Devoir devoir = devoirService.GetOneDevoir(devoirId);

        if (etudiant == null || devoir == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Float note = null;
        try {
            note = notationService.getNoteDevoirEtudiant(etudiantId, devoirId);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        NotationTotale notationTotale = new NotationTotale();
        notationTotale.setNoteTotale(note);
        notationTotale.setEtudiant(etudiant);
        notationTotale.setDevoir(devoir);

        return new ResponseEntity<>(notationTotale, HttpStatus.OK);
    }

    /**
     * Récupérer les notes de tous les devoirs d'un étudiant
     * GET => /api/notation/etudiant/{etudiantId}/devoir/all
     * On renvoie un List<NotationTotale>
     */
    @GetMapping("/etudiant/{etudiantId}/devoir/all")
    public ResponseEntity<?> getAllNotesDevoirsEtudiant(
            @PathVariable Integer etudiantId
    ) {
        Etudiant etudiant;
        try {
            etudiant = etudiantService.GetOneEtudiant(etudiantId);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        if (etudiant == null) {
            return new ResponseEntity<>("Étudiant non trouvé avec l'id " + etudiantId, HttpStatus.NOT_FOUND);
        }

        List<Notation> notations = notationService.getAllNotationsForEtudiant(etudiantId);
        Map<Devoir, Float> totalNotesByDevoir = new HashMap<>();

        for (Notation notation : notations) {
            Devoir devoir = notation.getPartieDevoir().getDevoir();
            totalNotesByDevoir.merge(devoir, notation.getNote(), Float::sum);
        }

        List<NotationTotale> result = totalNotesByDevoir.entrySet().stream()
                .map(entry -> {
                    NotationTotale notationTotale = new NotationTotale();
                    notationTotale.setDevoir(entry.getKey());
                    notationTotale.setEtudiant(etudiant);
                    notationTotale.setNoteTotale(entry.getValue());
                    return notationTotale;
                })
                .collect(Collectors.toList());

        return new ResponseEntity<>(result, HttpStatus.OK);
    }


    /**
     * Noter plusieurs parties d'un devoir d'un étudiant
     * POST => /api/notation/etudiant/{etudiantId}/devoir/{devoirId}/parties
     * On attend dans le corps de la requête un JSON du type :
     * {
     *   "parties": {
     *     "1": 15.5,
     *     "2": 13,
     *     "4": 20
     *   }
     * }
     */
    @PostMapping("/etudiant/{etudiantId}/devoir/{devoirId}/parties")
    public ResponseEntity<List<Notation>> noterPlusieursParties(
            @PathVariable Integer etudiantId,
            @PathVariable Integer devoirId,
            @RequestBody Map<String, Map<Integer, Float>> payload
    ) {
        try {
            Map<Integer, Float> notesParPartie = payload.getOrDefault("parties", new HashMap<>());
            List<Notation> resultat = notationService.createOrUpdateMultipleNotations(etudiantId, devoirId, notesParPartie);
            return new ResponseEntity<>(resultat, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
    /**
     * Avoir toutes les moyennes de chaque matière d'un étudiant
     *
     * GET => /api/notation/etudiant/{etudiantId}/matieres/moyenne/all
     *
     * On renvoie une List<MoyenneTotale>
     */
    @GetMapping("/etudiant/{etudiantId}/matieres/moyenne/all")
    public ResponseEntity<List<MoyenneTotale>> getAllMoyennesMatieresEtudiant(
            @PathVariable Integer etudiantId
    ) {
        Etudiant etudiant = null;
        try {
            etudiant = etudiantService.GetOneEtudiant(etudiantId);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if (etudiant == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        List<MoyenneTotale> moyennes = notationService.getAllMoyennesMatieresEtudiant(etudiantId);
        return new ResponseEntity<>(moyennes, HttpStatus.OK);
    }

    /**
     * Avoir la moyenne d'une matière d'un étudiant
     *
     * GET => /api/notation/etudiant/{etudiantId}/matieres/moyenne/{matiereId}
     */
    @GetMapping("/etudiant/{etudiantId}/matieres/moyenne/{matiereId}")
    public ResponseEntity<MoyenneTotale> getMoyenneMatiereEtudiant(
            @PathVariable Integer etudiantId,
            @PathVariable Integer matiereId
    ) {
        Etudiant etudiant = null;
        try {
            etudiant = etudiantService.GetOneEtudiant(etudiantId);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if (etudiant == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        MoyenneTotale moyenne = notationService.getMoyenneMatiereEtudiant(etudiantId, matiereId);
        return new ResponseEntity<>(moyenne, HttpStatus.OK);
    }

    /**
     * Avoir la moyenne générale de l'étudiant
     *
     * GET => /api/notation/etudiant/{etudiantId}/matieres/moyenne/generale
     */
    @GetMapping("/etudiant/{etudiantId}/matieres/moyenne/generale")
    public ResponseEntity<MoyenneTotale> getMoyenneGeneraleEtudiant(
            @PathVariable Integer etudiantId
    ) {
        Etudiant etudiant = null;
        try {
            etudiant = etudiantService.GetOneEtudiant(etudiantId);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if (etudiant == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        MoyenneTotale moyenneGenerale = notationService.getMoyenneGeneraleEtudiant(etudiantId);
        return new ResponseEntity<>(moyenneGenerale, HttpStatus.OK);
    }


    /**
     * Supprimer les notes d'un devoir d'un étudiant
     * DELETE => /api/notation/etudiant/{etudiantId}/devoir/{devoirId}
     */
    @DeleteMapping("/etudiant/{etudiantId}/devoir/{devoirId}")
    public ResponseEntity<?> deleteNotationDevoirEtudiant(
            @PathVariable Integer etudiantId,
            @PathVariable Integer devoirId
    ) {
        try {
            notationService.deleteNotationsDevoirEtudiant(etudiantId, devoirId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); // 204 No Content
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND); // 404 Not Found
        }
    }

    /**
     * Récupérer toutes les notes totales de chaque devoir de chaque élève d'une matière
     *
     * GET => /api/notation/matiere/{matiereId}/devoirs/all
     *
     * On renvoie un List<NotationTotale>
     */
    @GetMapping("/matiere/{matiereId}/devoirs/all")
    public ResponseEntity<?> getNotesParDevoirParElevePourMatiere(
            @PathVariable Integer matiereId
    ) {
        List<NotationTotale> result = notationService.getNotesParDevoirParElevePourMatiere(matiereId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/classe/{classeId}/devoirs/all")
    public ResponseEntity<?> getNotesParDevoirParElevePourClasse(
            @PathVariable Integer classeId
    ) {
        List<NotationTotale> result = notationService.getNotesParDevoirParElevePourClasse(classeId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * Récupérer toutes les notes totales de chaque devoir de chaque élève d'une matière par classe
     * GET => /api/notation/matiere/{matiereId}/classe/{classeId}/devoirs/all
     * On renvoie un Map<EtudiantId, Map<DevoirId, NoteTotale>>
     */
    @GetMapping("/matiere/{matiereId}/classe/{classeId}/devoirs/all")
    public ResponseEntity<Map<Integer, Map<Integer, Float>>> getNotesParDevoirParElevePourMatiereEtClasse(
            @PathVariable Integer matiereId,
            @PathVariable Integer classeId
    ) {
        Map<Integer, Map<Integer, Float>> result = notationService.getNotesParDevoirParElevePourMatiereEtClasse(matiereId, classeId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PutMapping("/etudiant/{etudiantId}/devoir/{devoirId}")
    public ResponseEntity<List<Notation>> updatePlusieursParties(
            @PathVariable Integer etudiantId,
            @PathVariable Integer devoirId,
            @RequestBody Map<String, Map<Integer, Float>> payload
    ) {
        try {
            Map<Integer, Float> notesParPartie = payload.getOrDefault("parties", new HashMap<>());
            List<Notation> resultat = notationService.createOrUpdateMultipleNotations(etudiantId, devoirId, notesParPartie);
            return new ResponseEntity<>(resultat, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Affiche le bulletin de notes : afficher pour chaque étudiant.e, sa moyenne par matiere et sa moyenne generale
     * GET ("api/notation/classe/{classeId}/bulletin)
     */
    @GetMapping("/classe/{classeId}/bulletin")
    public ResponseEntity<?> getBulletinDeNotes(
            @PathVariable Integer classeId
    ) {
        Classe classe;
        try
        {
            classe = classeService.GetOneClasse(classeId);
        } catch (Exception e)
        {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        if (classe == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        List<Map<String, Object>> bulletins = classe.getEtudiants().stream()
                .map(etudiant -> {
                    Map<String, Object> bulletin = new HashMap<>();
                    bulletin.put("etudiantId", etudiant.getId());
                    bulletin.put("nom", etudiant.getNom());
                    bulletin.put("prenom", etudiant.getPrenom());
                    bulletin.put("moyennesParMatiere", notationService.getAllMoyennesMatieresEtudiant(etudiant.getId()));
                    bulletin.put("moyenneGenerale", notationService.getMoyenneGeneraleEtudiant(etudiant.getId()));
                    return bulletin;
                })
                .collect(Collectors.toList());

        return new ResponseEntity<>(bulletins, HttpStatus.OK);
    }

    /**
     * Récupérer toutes les notes totales d'un devoir de chaque élève.
     * GET => /api/notation/devoir/{devoirId}/notes
     * On renvoie un List<NotationTotale>
     */
    @GetMapping("/devoir/{devoirId}/notes")
    public ResponseEntity<?> getNotesParEtudiantPourDevoir(
            @PathVariable Integer devoirId
    ) {
        List<NotationTotale> result = null;
        try
        {
            result = notationService.getNotesParEtudiantPourDevoir(devoirId);
        } catch (Exception e)
        {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}