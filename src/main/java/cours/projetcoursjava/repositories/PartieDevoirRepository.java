package cours.projetcoursjava.repositories;

import cours.projetcoursjava.entities.PartieDevoir;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PartieDevoirRepository extends JpaRepository<PartieDevoir, Integer>
{
}
