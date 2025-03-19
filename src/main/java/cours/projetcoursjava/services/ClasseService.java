package cours.projetcoursjava.services;

import cours.projetcoursjava.entities.Classe;
import cours.projetcoursjava.repositories.ClasseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClasseService
{
    @Autowired
    private ClasseRepository classeRepository;

    public List<Classe> GetAllClasses()
    {
        return classeRepository.findAll();
    }

    public Classe GetOneClasse(int id) throws Exception
    {
        Classe classe = classeRepository.findById(id).orElse(null);
        if (classe == null)
            throw new Exception("la classe d'id " + id + " n'existe pas");

        return classe;
    }

    public Classe DeleteOneClasse(int id) throws Exception
    {
        Classe classe = classeRepository.findById(id).orElse(null);
        if (classe == null)
        {
            throw new Exception("Impossible de supprimer la classse d'id " + id + " car elle ne semble pas exister");
        }

        classeRepository.delete(classe);
        classeRepository.flush();
        return classe;
    }

    public Classe UpdateClasse(int id, String nvNom) throws Exception
    {
        Classe classe = classeRepository.findById(id).orElse(null);

        if(classe == null)
            throw new Exception("aucune classe dans la base de données avec l'id : " + id );

        classe.setNom(nvNom);
        return classeRepository.save(classe);
    }

    public Classe CreateClasse(Classe classe)
    {
        return classeRepository.save(classe);
    }
}
