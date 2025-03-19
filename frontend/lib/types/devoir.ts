import {Matiere} from "@/lib/types/matiere";
import {Classe} from "@/lib/types/classe";
import {Commun} from "@/lib/types/commun";

export interface Devoir extends Commun {
    dateDevoir: Date;
    coefficient: number;
    typeDevoir: "CC" | "Examen";
    partiedevoirs: PartieDevoir[];
    matiere: Matiere;
    classe: Classe;
}

export interface PartieDevoir extends Commun {
    points: number;

}