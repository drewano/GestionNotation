package cours.projetcoursjava.repositories;

import cours.projetcoursjava.entities.Notation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NotationRepository extends JpaRepository<Notation, Integer> {

    // Récupérer toutes les notations d’un étudiant pour un devoir donné
    @Query("SELECT n FROM Notation n " +
            "WHERE n.etudiant.id = :etudiantId " +
            "AND n.partieDevoir.devoir.id = :devoirId")
    List<Notation> findByEtudiantAndDevoir(
            @Param("etudiantId") Integer etudiantId,
            @Param("devoirId") Integer devoirId
    );

    // Récupérer toutes les notations d’un étudiant (tous devoirs confondus)
    @Query("SELECT n FROM Notation n " +
            "WHERE n.etudiant.id = :etudiantId")
    List<Notation> findByEtudiant(@Param("etudiantId") Integer etudiantId);

    // Supprimer toutes les notations d’un étudiant pour un devoir donné
    @Modifying
    @Query("DELETE FROM Notation n WHERE n.etudiant.id = :etudiantId AND n.partieDevoir.devoir.id = :devoirId")
    int deleteNotationsByEtudiantAndDevoir(@Param("etudiantId") Integer etudiantId, @Param("devoirId") Integer devoirId);

    // Récupérer les notes totales de chaque devoir pour une matière donnée
    @Query("SELECT pd.devoir.id, SUM(n.note) FROM Notation n JOIN n.partieDevoir pd JOIN pd.devoir d WHERE d.matiere.id = :matiereId GROUP BY pd.devoir.id")
    List<Object[]> findTotalNotesByMatiereId(@Param("matiereId") Integer matiereId);

    // Récupérer les notes totales de chaque devoir pour une classe donnée
    @Query("SELECT pd.devoir.id, SUM(n.note) FROM Notation n JOIN n.partieDevoir pd JOIN pd.devoir d WHERE d.classe.id = :classeId GROUP BY pd.devoir.id")
    List<Object[]> findTotalNotesByClasseId(@Param("classeId") Integer classeId);

    // Récupérer les notes totales de chaque devoir pour une matière et une classe données
    @Query("SELECT pd.devoir.id, SUM(n.note) FROM Notation n JOIN n.partieDevoir pd JOIN pd.devoir d WHERE d.matiere.id = :matiereId AND d.classe.id = :classeId GROUP BY pd.devoir.id")
    List<Object[]> findTotalNotesByMatiereIdAndClasseId(@Param("matiereId") Integer matiereId, @Param("classeId") Integer classeId);

    // Récupérer toutes les notes pour un devoir donné, groupées par étudiant.
    @Query("SELECT n.etudiant.id, SUM(n.note) FROM Notation n " +
            "WHERE n.partieDevoir.devoir.id = :devoirId " +
            "GROUP BY n.etudiant.id")
    List<Object[]> findTotalNotesByDevoirGroupByEtudiant(@Param("devoirId") Integer devoirId);
}