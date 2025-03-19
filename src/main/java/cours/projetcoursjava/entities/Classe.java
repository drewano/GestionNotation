package cours.projetcoursjava.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "classe")
public class Classe
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "nom", nullable = false, length = 100)
    private String nom;

    @OneToMany(mappedBy = "classe")
    @JsonIgnore
    private Set<Devoir> devoirs = new LinkedHashSet<>();

    @OneToMany(mappedBy = "classe", fetch = FetchType.EAGER)
    @JsonIgnore
    private Set<Etudiant> etudiants = new LinkedHashSet<>();

    public Integer getId()
    {
        return id;
    }

    public String getNom()
    {
        return nom;
    }

    public void setNom(String nom)
    {
        this.nom = nom;
    }

    public Set<Devoir> getDevoirs()
    {
        return devoirs;
    }

    public void setDevoirs(Set<Devoir> devoirs)
    {
        this.devoirs = devoirs;
    }

    public Set<Etudiant> getEtudiants()
    {
        return etudiants;
    }

    public void setEtudiants(Set<Etudiant> etudiants)
    {
        this.etudiants = etudiants;
    }
}