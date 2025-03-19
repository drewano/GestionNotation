import {Etudiant} from "@/lib/types/etudiant";
import {Devoir} from "@/lib/types/devoir";

export interface NotationTotale {
    etudiant: Etudiant;
    devoir: Devoir;
    noteTotale: number;
}