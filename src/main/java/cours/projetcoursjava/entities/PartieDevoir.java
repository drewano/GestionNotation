package cours.projetcoursjava.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "partiedevoir")
public class PartieDevoir
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "devoir")
    @JsonIgnore
    private Devoir devoir;

    @ColumnDefault("0")
    @Column(name = "points", nullable = false)
    private Float points;

    public Integer getId()
    {
        return id;
    }

    public Devoir getDevoir()
    {
        return devoir;
    }

    public void setDevoir(Devoir devoir)
    {
        this.devoir = devoir;
    }

    public Float getPoints()
    {
        return points;
    }

    public void setPoints(Float points)
    {
        this.points = points;
    }

}