package cours.projetcoursjava.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "matiere")
public class Matiere
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "nom", nullable = false, length = 100)
    private String nom;

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
}