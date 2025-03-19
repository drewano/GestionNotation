import {Classe} from "@/lib/types/classe";
import {Commun} from "@/lib/types/commun";

export interface Etudiant extends Commun{
    prenom: string;
    nom: string;
    photo?: string;
    classe?: Classe;
}