package cours.projetcoursjava.services;

import cours.projetcoursjava.entities.Devoir;
import cours.projetcoursjava.entities.PartieDevoir;
import cours.projetcoursjava.entities.Notation;
import cours.projetcoursjava.repositories.DevoirRepository;
import cours.projetcoursjava.repositories.PartieDevoirRepository;
import cours.projetcoursjava.repositories.NotationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PartieDevoirService
{
    @Autowired
    private PartieDevoirRepository partieDevoirRepository;
    @Autowired
    private DevoirRepository devoirRepository;
    @Autowired
    private NotationRepository notationRepository; // Inject NotationRepository

    public List<PartieDevoir> GetPartiesDevoirOfDevoir(Integer idDevoir) throws Exception
    {
        Devoir devoir = devoirRepository.findById(idDevoir).orElse(null);
        if (devoir == null)
        {
            throw new Exception("impossible de récupérer les parties du devoir à l'id " + idDevoir + " car il n'existe pas");
        }

        return devoir.getPartiedevoirs().stream().toList();
    }

    public PartieDevoir AddPartieDevoir(Integer idDevoir) throws Exception
    {
        return AddPartieDevoir(idDevoir, 0);
    }

    public PartieDevoir AddPartieDevoir(Integer idDevoir, float points) throws Exception
    {
        Devoir devoir = devoirRepository.findById(idDevoir).orElse(null);
        PartieDevoir partieDevoir = new PartieDevoir();
        partieDevoir.setPoints(points);
        partieDevoirRepository.save(partieDevoir);
        if (devoir == null)
        {
            partieDevoirRepository.delete(partieDevoir);
            throw new Exception("aucun devoir avec l'id " + idDevoir + " à qui ajouter une partie de devoir");
        }

        devoir.getPartiedevoirs().add(partieDevoir);
        devoirRepository.save(devoir);
        partieDevoir.setDevoir(devoir);
        partieDevoirRepository.save(partieDevoir);
        return partieDevoir;
    }

    public PartieDevoir DeletePartieDevoir(Integer id) throws Exception
    {
        PartieDevoir partieDevoir = partieDevoirRepository.findById(id).orElse(null);

        if (partieDevoir == null)
        {
            throw new Exception("Impossible de supprimer la partie de devoir d'id " + id + " car elle n'existe pas");
        }

        partieDevoirRepository.delete(partieDevoir);
        partieDevoirRepository.flush();
        return partieDevoir;
    }

    public PartieDevoir SetPoints(Integer id, float points) throws Exception
    {
        PartieDevoir partieDevoir = partieDevoirRepository.findById(id).orElse(null);

        if (partieDevoir == null)
            throw new Exception("Impossible de modifier les points de la partie de devoir d'id " + id + " car elle n'existe pas");

        partieDevoir.setPoints(points);
        return partieDevoirRepository.save(partieDevoir);
    }

    public PartieDevoir GetOnePartie(Integer id) {
        return partieDevoirRepository.findById(id).orElse(null);
    }

    // New method to get all notations for a partie
    public List<Notation> getAllNotationsForPartie(Integer partieId) {
        PartieDevoir partieDevoir = partieDevoirRepository.findById(partieId).orElse(null);
        if (partieDevoir != null) {
            return notationRepository.findAll().stream()
                    .filter(notation -> notation.getPartieDevoir().getId().equals(partieId))
                    .toList();
        }
        return List.of();
    }
}