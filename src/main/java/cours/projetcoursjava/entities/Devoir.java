package cours.projetcoursjava.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import cours.projetcoursjava.types.TypeDevoir;
import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "devoir")
public class Devoir
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Lob
    @Column(name = "type_devoir", nullable = false)
    @Enumerated(EnumType.STRING)
    private TypeDevoir typeDevoir;

//    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="Europe/Paris")
    @Column(name = "date_devoir", nullable = false)
//    @JsonSerialize(using = LocalDateTimeSerializer.class)
//    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
//    @JsonFormat(pattern = "yyyy-MM-dd'T'hh:mm:ss.SSS'Z'")
    private ZonedDateTime dateDevoir;

    @Column(name = "coefficient", nullable = false)
    private Float coefficient;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "classe", nullable = false)
    private Classe classe;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "matiere", nullable = false)
    private Matiere matiere;

    @OneToMany(mappedBy = "devoir", fetch = FetchType.EAGER, cascade=CascadeType.ALL)
    private Set<PartieDevoir> partiedevoirs = new LinkedHashSet<>();

    public Integer getId()
    {
        return id;
    }

    public TypeDevoir getTypeDevoir()
    {
        return typeDevoir;
    }

    public void setTypeDevoir(TypeDevoir typeDevoir)
    {
        this.typeDevoir = typeDevoir;
    }

    public ZonedDateTime getDateDevoir()
    {
        return dateDevoir;
    }

    public void setDateDevoir(ZonedDateTime dateDevoir)
    {
        this.dateDevoir = dateDevoir;
    }

    public Float getCoefficient()
    {
        return coefficient;
    }

    public void setCoefficient(Float coefficient)
    {
        this.coefficient = coefficient;
    }

    public Classe getClasse()
    {
        return classe;
    }

    public void setClasse(Classe classe)
    {
        this.classe = classe;
    }

    public Matiere getMatiere()
    {
        return matiere;
    }

    public void setMatiere(Matiere matiere)
    {
        this.matiere = matiere;
    }

    public Set<PartieDevoir> getPartiedevoirs()
    {
        return partiedevoirs;
    }

    public void setPartiedevoirs(Set<PartieDevoir> partiedevoirs)
    {
        this.partiedevoirs = partiedevoirs;
    }
}