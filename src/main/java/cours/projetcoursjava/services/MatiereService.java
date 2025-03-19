package cours.projetcoursjava.services;

import cours.projetcoursjava.entities.Matiere;
import cours.projetcoursjava.repositories.MatiereRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MatiereService
{
    @Autowired
    private MatiereRepository matiereRepository;


    public List<Matiere> GetAllMatieres()
    {
        return matiereRepository.findAll();
    }

    public Matiere GetOneMatiere(int id) throws Exception
    {
        Matiere matière = matiereRepository.findById(id).orElse(null);
        if (matière == null)
        {
            throw new Exception("la matière d'id " + id + " n'existe pas");
        }
        return matière;
    }

    public Matiere DeleteOneMatiere(int id) throws Exception
    {
        Matiere matiere = matiereRepository.findById(id).orElse(null);
        if (matiere == null)
        {
            throw new Exception("Impossible de supprimer la matière d'id " + id + " car elle ne semble pas exister");
        }

        matiereRepository.delete(matiere);
        matiereRepository.flush();
        return matiere;
    }

    public Matiere UpdateMatiere(int id, String nvNom) throws Exception
    {
        Matiere matiere = matiereRepository.findById(id).orElse(null);

        if(matiere == null)
            throw new Exception("aucune matière dans la base de données avec l'id : " + id );

        matiere.setNom(nvNom);
        return matiereRepository.save(matiere);
    }

    public Matiere CreateMatiere(Matiere matiere)
    {
        return matiereRepository.save(matiere);
    }
}
