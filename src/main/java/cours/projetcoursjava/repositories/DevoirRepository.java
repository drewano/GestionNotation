package cours.projetcoursjava.repositories;

import cours.projetcoursjava.entities.Devoir;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DevoirRepository extends JpaRepository<Devoir, Integer>
{

}
