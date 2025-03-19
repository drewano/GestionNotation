package cours.projetcoursjava.services;

import cours.projetcoursjava.entities.Etudiant;
import cours.projetcoursjava.repositories.EtudiantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EtudiantService
{
    @Autowired
    private EtudiantRepository etudiantRepository;

    public List<Etudiant> GetAllEtudiants()
    {
        return etudiantRepository.findAll();
    }

    public Etudiant GetOneEtudiant(int id) throws Exception
    {
        Etudiant etudiant = etudiantRepository.findById(id).orElse(null);

        if (etudiant == null)
        {
            throw new Exception("Il n y a pas d'étdudiant avec l'id " + id);
        }

        return etudiant;
    }

    public Etudiant DeleteOneEtudiant(int id) throws Exception
    {
        Etudiant etudiant = etudiantRepository.findById(id).orElse(null);
        if (etudiant == null)
        {
            throw new Exception("Impossible de supprimer l'étudiant d'id " + id + " car il ne semble pas exister");
        }

        etudiantRepository.delete(etudiant);
        etudiantRepository.flush();
        return etudiant;
    }

    public Etudiant CreateEtudiant(Etudiant etudiant)
    {
        return etudiantRepository.save(etudiant);
    }

    //rechercher les etudiants disponibles
    public List<Etudiant> GetEtudiantsDisponibles()
    {
        return etudiantRepository.findByClasseIsNull();
    }
}
