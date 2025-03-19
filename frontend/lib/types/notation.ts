import {Commun} from "@/lib/types/commun";
import {Etudiant} from "@/lib/types/etudiant";
import {Devoir, PartieDevoir} from "@/lib/types/devoir";

export interface Notation extends Commun{
    note: number;
    etudiant: Etudiant;
    partieDevoir: PartieDevoir;
}