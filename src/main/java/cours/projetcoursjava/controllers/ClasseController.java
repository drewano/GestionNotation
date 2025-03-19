package cours.projetcoursjava.controllers;

import cours.projetcoursjava.entities.Classe;
import cours.projetcoursjava.services.ClasseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/classes")
public class ClasseController
{
    @Autowired
    private ClasseService classeService;

    @GetMapping("/all")
    @ResponseBody
    public ResponseEntity<List<Classe>> GetAllClasses()
    {
        return new ResponseEntity<>(classeService.GetAllClasses(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @ResponseBody
    public ResponseEntity<?> GetOneClasse(@PathVariable int id)
    {
        Classe classe = null;
        try
        {
            classe = classeService.GetOneClasse(id);
        } catch (Exception e)
        {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(classe, classe == null ? HttpStatus.NOT_FOUND : HttpStatus.OK);
    }

    @PutMapping("/update/{id}")
    @ResponseBody
    public ResponseEntity<Classe> UpdateClasse(@PathVariable int id, @RequestBody Map<String, Object> classe)
    {
        Classe toUpdateClasse = null;
        try
        {
            toUpdateClasse = classeService.UpdateClasse(id, classe.get("nom").toString());
        }
        catch (Exception e)
        {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(toUpdateClasse, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    @ResponseBody
    public ResponseEntity<Classe> DeleteClasse(@PathVariable int id)
    {

        Classe toDeleteClasse = null;

        try
        {
            toDeleteClasse = classeService.DeleteOneClasse(id);
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(toDeleteClasse, HttpStatus.OK);
    }

    @PostMapping("/new")
    @ResponseBody
    public ResponseEntity<Classe> CreateClasse(@RequestBody Map<String, Object> classe)
    {
        Classe nvlleClasse = new Classe();
        nvlleClasse.setNom(classe.get("nom").toString());

        return new ResponseEntity<>(classeService.CreateClasse(nvlleClasse), HttpStatus.OK);
    }
}
