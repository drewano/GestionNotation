package cours.projetcoursjava.controllers;

import cours.projetcoursjava.entities.Classe;
import cours.projetcoursjava.entities.Etudiant;
import cours.projetcoursjava.services.ClasseService;
import cours.projetcoursjava.services.EtudiantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/etudiants")
public class EtudiantController {
    @Autowired
    private EtudiantService etudiantService;
    @Autowired
    private ClasseService classeService;

    @GetMapping("/all")
    @ResponseBody
    public ResponseEntity<List<Etudiant>> GetAllEtudiant() {
        return new ResponseEntity<>(etudiantService.GetAllEtudiants(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @ResponseBody
    public ResponseEntity<?> GetOneEtudiant(@PathVariable int id) {
        Etudiant etudiant;
        try
        {
            etudiant = etudiantService.GetOneEtudiant(id);
        } catch (Exception e)
        {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(etudiant, HttpStatus.OK);
    }

    @PutMapping("/update/{id}")
    @ResponseBody
    public ResponseEntity<?> UpdateEtudiant(@PathVariable int id, @RequestBody Map<String, Object> etudiant) {

        Etudiant etudiantToUpdate;
        try
        {
            etudiantToUpdate = etudiantService.GetOneEtudiant(id);
        } catch (Exception e)
        {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        etudiantToUpdate.setNom(etudiant.getOrDefault("nom", etudiantToUpdate.getNom()).toString());
        etudiantToUpdate.setPrenom(etudiant.getOrDefault("prenom", etudiantToUpdate.getPrenom()).toString());
        etudiantToUpdate.setPhoto(etudiant.getOrDefault("photo", etudiantToUpdate.getPhoto()).toString());

        Classe classe = (Classe) etudiant.getOrDefault("classe", null);

        if (classe != null) {
            Classe newClasse = null;
            try
            {
                newClasse = classeService.GetOneClasse(classe.getId());
            } catch (Exception e)
            {
                return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
            }
            if (newClasse != null)
                etudiantToUpdate.setClasse(newClasse);
        }

        return new ResponseEntity<>(etudiantService.CreateEtudiant(etudiantToUpdate), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    @ResponseBody
    public ResponseEntity<Etudiant> DeleteEtudiant(@PathVariable int id) {
        Etudiant toDeleteEtudiant = null;

        try {
            toDeleteEtudiant = etudiantService.DeleteOneEtudiant(id);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(toDeleteEtudiant, HttpStatus.OK);
    }

    @PostMapping("/new")
    @ResponseBody
    public ResponseEntity<?> CreateEtudiant(@RequestBody Map<String, Object> etudiant)
    {
        Etudiant nvEtudiant = new Etudiant();

        // Récupération des champs "nom", "prenom" et "photo"
        nvEtudiant.setPrenom(etudiant.get("prenom").toString());
        nvEtudiant.setNom(etudiant.get("nom").toString());
        nvEtudiant.setPhoto(etudiant.get("photo").toString());

        // Vérification de la présence ou de la valeur de "classe"
        // On autorise le champ à être absent ou null.
        if (etudiant.containsKey("classe") && etudiant.get("classe") != null) {
            int classeId = (Integer) etudiant.get("classe");
            // On récupère la classe depuis la base
            Classe classe = null;
            try
            {
                classe = classeService.GetOneClasse(classeId);
            } catch (Exception e)
            {
                return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
            }
            // Si la classe existe, on l’associe, sinon on laisse null
            if (classe != null) {
                nvEtudiant.setClasse(classe);
            }
        }

        // Sauvegarde en base de données via le service
        Etudiant savedEtudiant = etudiantService.CreateEtudiant(nvEtudiant);

        return new ResponseEntity<>(savedEtudiant, HttpStatus.OK);
    }

    @GetMapping("/disponibles")
    @ResponseBody
    public ResponseEntity<List<Etudiant>> GetEtudiantsDisponibles()
    {
        return new ResponseEntity<>(etudiantService.GetEtudiantsDisponibles(), HttpStatus.OK);
    }
}