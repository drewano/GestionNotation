package cours.projetcoursjava.entities;

import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "notation")
public class Notation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "etudiant_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Etudiant etudiant;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "partiedevoir_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private PartieDevoir partieDevoir;

    @Column(name = "note", nullable = false)
    private Float note;

    public Integer getId() {
        return id;
    }

    public Etudiant getEtudiant() {
        return etudiant;
    }

    public void setEtudiant(Etudiant etudiant) {
        this.etudiant = etudiant;
    }

    public PartieDevoir getPartieDevoir() {
        return partieDevoir;
    }

    public void setPartieDevoir(PartieDevoir partieDevoir) {
        this.partieDevoir = partieDevoir;
    }

    public Float getNote() {
        return note;
    }

    public void setNote(Float note) {
        this.note = note;
    }
}
