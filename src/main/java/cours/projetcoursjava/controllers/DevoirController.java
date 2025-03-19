package cours.projetcoursjava.controllers;

import cours.projetcoursjava.entities.Classe;
import cours.projetcoursjava.entities.Devoir;
import cours.projetcoursjava.entities.Matiere;
import cours.projetcoursjava.entities.PartieDevoir;
import cours.projetcoursjava.services.*;
import cours.projetcoursjava.types.TypeDevoir;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/devoirs")
public class DevoirController {
    @Autowired
    private DevoirService devoirService;
    @Autowired
    private ClasseService classeService;
    @Autowired
    private MatiereService matiereService;
    @Autowired
    private EtudiantService etudiantService;
    @Autowired
    private PartieDevoirService partieDevoirService;

    @GetMapping("/all")
    @ResponseBody
    public ResponseEntity<List<Devoir>> GetAllDevoir()
    {
        return new ResponseEntity<>(devoirService.GetAllDevoirs(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @ResponseBody
    public ResponseEntity<Devoir> GetOneDevoir(@PathVariable int id)
    {
        Devoir devoir = devoirService.GetOneDevoir(id);

        return new ResponseEntity<>(devoir, devoir == null ? HttpStatus.NOT_FOUND : HttpStatus.OK);
    }

    @PutMapping("/update/{id}")
    @ResponseBody
    public ResponseEntity<?> UpdateDevoir(@PathVariable int id, @RequestBody Map<String, Object> devoir)
    {
        Devoir devoirToUpdate = devoirService.GetOneDevoir(id);

        if (devoirToUpdate == null)
            return new ResponseEntity<>("Il n'existe pas de devoir avec l'id " + id, HttpStatus.NOT_FOUND);

        devoirToUpdate.setTypeDevoir(TypeDevoir.valueOf(
                devoir.getOrDefault("typeDevoir", devoirToUpdate.getTypeDevoir()
                ).toString()
        ));



        devoirToUpdate.setDateDevoir(ZonedDateTime.parse(
                devoir.getOrDefault("dateDevoir", devoirToUpdate.getDateDevoir()
                ).toString()
        ));

        devoirToUpdate.setCoefficient(Float.parseFloat(devoir.getOrDefault("coefficient", devoirToUpdate.getCoefficient()).toString()));


        int classeId = (Integer)devoir.getOrDefault("classe", -1);

        if (classeId >= 0)
        {
            Classe newClasse = null;
            try
            {
                newClasse = classeService.GetOneClasse(classeId);
            } catch (Exception e)
            {
                System.out.println(e.getMessage());
                return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
            }
            if (newClasse != null)
                devoirToUpdate.setClasse(newClasse);
        }

        int MatiereId = (Integer)devoir.getOrDefault("matiere", -1);

        if (classeId >= 0)
        {
            Matiere newMatiere;

            try
            {
                newMatiere = matiereService.GetOneMatiere(MatiereId);
            }
            catch (Exception e)
            {
                System.out.println(e.getMessage());
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            if (newMatiere != null)
                devoirToUpdate.setMatiere(newMatiere);
        }

        ArrayList<Map<String, Object>> parties = (ArrayList<Map<String, Object>>) devoir.get( "partiedevoirs");

        if (parties != null)
        {
            ClearPartiesOfDevoir(devoirToUpdate);

            try
            {
                InitPartiesOfDevoir(devoirToUpdate, parties);
            }
            catch (Exception e)
            {
                System.out.println(e.getMessage());
                return new ResponseEntity<>(e, HttpStatus.BAD_REQUEST);
            }

        }

        return new ResponseEntity<>(devoirService.CreateDevoir(devoirToUpdate), HttpStatus.OK);
    }

    @PostMapping("/new")
    @ResponseBody
    public ResponseEntity<?> CreateDevoir(@RequestBody Map<String, Object> devoir)
    {
        Devoir nvDevoir = new Devoir();

        nvDevoir.setTypeDevoir(TypeDevoir.valueOf(
                devoir.getOrDefault("typeDevoir", TypeDevoir.CC
                ).toString()
        ));

        nvDevoir.setDateDevoir(ZonedDateTime.parse(
                devoir.getOrDefault("dateDevoir", LocalDate.now()
                ).toString()
        ));

        nvDevoir.setCoefficient(Float.parseFloat(devoir.getOrDefault("coefficient", 1).toString()));

        int classeId = (Integer)devoir.getOrDefault("classe", -1);

        if (classeId >= 0)
        {
            Classe nvlleClasse = null;
            try
            {
                nvlleClasse = classeService.GetOneClasse(classeId);
            } catch (Exception e)
            {
                System.out.println(e.getMessage());
                return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
            }
            if (nvlleClasse != null)
                nvDevoir.setClasse(nvlleClasse);
            else
            {
                System.out.println("Classe " + classeId + " not found");
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        }

        int MatiereId = (Integer)devoir.getOrDefault("matiere", -1);

        if (classeId >= 0)
        {
            Matiere newMatiere;

            try
            {
                newMatiere = matiereService.GetOneMatiere(MatiereId);
            }
            catch (Exception e)
            {
                System.out.println(e.getMessage());
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            if (newMatiere != null)
                nvDevoir.setMatiere(newMatiere);
            else
            {
                System.out.println("Matiere " + MatiereId + " not found");
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        }

        devoirService.CreateDevoir(nvDevoir);

        ArrayList<Map<String, Object>> parties = (ArrayList<Map<String, Object>>) devoir.get("partiedevoirs");

        if (parties != null)
        {
            try
            {
                InitPartiesOfDevoir(nvDevoir, parties);
            } catch (Exception e)
            {
                System.out.println(e.getMessage());
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        }

        return new ResponseEntity<>(nvDevoir, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    @ResponseBody
    public ResponseEntity<?> DeleteDevoir(@PathVariable int id)
    {
        Devoir toDeleteDevoir;

        try
        {
            toDeleteDevoir = devoirService.DeleteOneDevoir(id);
        }
        catch (Exception e)
        {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(toDeleteDevoir, HttpStatus.OK);
    }


    private void InitPartiesOfDevoir(Devoir devoir,  ArrayList<Map<String, Object>> parties) throws Exception
    {
        float note = 0;
        StringBuilder errors = new StringBuilder();

        for (Map<String, Object> partie : parties)
        {
            float points = Float.parseFloat(partie.getOrDefault("points", 0).toString());
            if (points + note > 20) points = 20 - note;
            note = Math.clamp(note + points, 0,20);

            try
            {
                partieDevoirService.AddPartieDevoir(devoir.getId(), points);
            }
            catch (Exception e)
            {
                System.out.println(e.getMessage());
                errors.append(e.getMessage());
            }
        }

        if (!errors.isEmpty())
            throw new Exception(errors.toString());
    }

    private void ClearPartiesOfDevoir(Devoir devoir)
    {
        StringBuilder erreurs = new StringBuilder();

        for (PartieDevoir partieDevoir : devoir.getPartiedevoirs())
        {
            try
            {
                partieDevoirService.DeletePartieDevoir(partieDevoir.getId());
            }
            catch (Exception e)
            {
                erreurs.append(e.getMessage());
            }
        }

        if (!erreurs.isEmpty())
        {
            System.out.println(erreurs);
        }

        devoir.getPartiedevoirs().clear();
        devoirService.CreateDevoir(devoir);
    }
}