package cours.projetcoursjava.types;

import cours.projetcoursjava.entities.Devoir;
import cours.projetcoursjava.entities.Etudiant;

public class NotationTotale
{
    private float noteTotale;
    private Etudiant etudiant;
    private Devoir devoir;

    public float getNoteTotale()
    {
        return noteTotale;
    }

    public void setNoteTotale(float noteTotale)
    {
        this.noteTotale = noteTotale;
    }

    public Etudiant getEtudiant()
    {
        return etudiant;
    }

    public void setEtudiant(Etudiant etudiant)
    {
        this.etudiant = etudiant;
    }

    public Devoir getDevoir()
    {
        return devoir;
    }

    public void setDevoir(Devoir devoir)
    {
        this.devoir = devoir;
    }
}
