package cours.projetcoursjava.controllers;

import cours.projetcoursjava.entities.Devoir;
import cours.projetcoursjava.entities.Matiere;
import cours.projetcoursjava.services.DevoirService;
import cours.projetcoursjava.services.MatiereService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.function.EntityResponse;

import javax.swing.text.html.parser.Entity;
import java.util.List;

@RestController
@RequestMapping("api/matieres")
public class MatiereController
{
    @Autowired
    private MatiereService matiereService;

    @Autowired
    private DevoirService devoirService;

    @GetMapping("/all")
    @ResponseBody
    public ResponseEntity<List<Matiere>> GetAllMatieres()
    {
        return new ResponseEntity<>(matiereService.GetAllMatieres(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @ResponseBody
    public ResponseEntity<?> GetOneMatiere(@PathVariable int id)
    {
        Matiere matiere;

        try
        {
            matiere = matiereService.GetOneMatiere(id);
        } catch (Exception e)
        {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(new RuntimeException(e.getMessage()), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(matiere, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    @ResponseBody
    public ResponseEntity<?> DeleteOneMatiere(@PathVariable int id) throws Exception
    {
        Matiere matiere = matiereService.GetOneMatiere(id);
        if (matiere == null)
        {
            return new ResponseEntity<>(new RuntimeException("la matière d'id " + id + " n'existe pas"), HttpStatus.BAD_REQUEST);
        }

        List<Devoir> devoirs = devoirService.GetAllDevoirs().stream().filter(devoir -> devoir.getMatiere().getId() == id).toList();
        if (!devoirs.isEmpty())
        {
            return new ResponseEntity<>(new RuntimeException("impossible de supprimer la matière " + matiere.getNom() +
                    " car il y a des devoirs assignés à cette matière :\n" + devoirs.toString()), HttpStatus.BAD_REQUEST);
        }

        try
        {
            matiere = matiereService.DeleteOneMatiere(id);
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(new RuntimeException(e.getMessage()), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(matiere, HttpStatus.OK);
    }

    @PutMapping("/update/{id}")
    @ResponseBody
    public ResponseEntity<?> UpdateMatiere(@PathVariable int id, @RequestBody Matiere matiere)
    {
        Matiere nvlleMatiere;
        try {
            nvlleMatiere = matiereService.UpdateMatiere(id, matiere.getNom());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>( new RuntimeException(e.getMessage()), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(nvlleMatiere, HttpStatus.OK);

    }

    @PostMapping("/new")
    @ResponseBody
    public ResponseEntity<Matiere> CreateMatiere(@RequestBody Matiere matiere)
    {
        return new ResponseEntity<>(matiereService.CreateMatiere(matiere), HttpStatus.OK);
    }
}