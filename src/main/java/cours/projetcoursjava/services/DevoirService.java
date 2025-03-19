package cours.projetcoursjava.services;

import cours.projetcoursjava.entities.Devoir;
import cours.projetcoursjava.repositories.DevoirRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DevoirService
{
    @Autowired
    private DevoirRepository devoirRepository;

    public List<Devoir> GetAllDevoirs()
    {
        return devoirRepository.findAll();
    }

    public Devoir GetOneDevoir(int id)
    {
        return devoirRepository.findById(id).orElse(null);
    }

    public Devoir DeleteOneDevoir(int id) throws Exception
    {
        Devoir devoir = devoirRepository.findById(id).orElse(null);
        if (devoir == null)
        {
            throw new Exception("Impossible de supprimer le devoir d'id " + id + " car il ne semble pas exister");
        }

        devoirRepository.delete(devoir);
//        devoirRepository.flush();
        return devoir;
    }

    public Devoir CreateDevoir(Devoir devoir)
    {
        return devoirRepository.save(devoir);
    }
}
