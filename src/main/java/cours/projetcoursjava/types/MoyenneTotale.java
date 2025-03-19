package cours.projetcoursjava.types;

import cours.projetcoursjava.entities.Etudiant;
import cours.projetcoursjava.entities.Matiere;

public class MoyenneTotale {
    private Etudiant etudiant;
    private Matiere matiere; // Null pour la moyenne générale
    private Float moyenne;

    public MoyenneTotale() {
    }

    public MoyenneTotale(Etudiant etudiant, Matiere matiere, Float moyenne) {
        this.etudiant = etudiant;
        this.matiere = matiere;
        this.moyenne = moyenne;
    }

    public Etudiant getEtudiant() {
        return etudiant;
    }

    public void setEtudiant(Etudiant etudiant) {
        this.etudiant = etudiant;
    }

    public Matiere getMatiere() {
        return matiere;
    }

    public void setMatiere(Matiere matiere) {
        this.matiere = matiere;
    }

    public Float getMoyenne() {
        return moyenne;
    }

    public void setMoyenne(Float moyenne) {
        this.moyenne = moyenne;
    }
}