package cours.projetcoursjava.repositories;

import cours.projetcoursjava.entities.Etudiant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EtudiantRepository extends JpaRepository<Etudiant, Integer>
{

    //rechercher les etudiants disponibles c'est à dire qui n'ont pas de classe
    List<Etudiant> findByClasseIsNull();

}
