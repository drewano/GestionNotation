import {Etudiant} from "@/lib/types/etudiant";
import {HTMLAttributes} from "react";
import {Button} from "@/components/ui/button";


export interface EtudiantItemProps {
    etudiant: Etudiant;
}

export default function EtudiantItem({etudiant, ...props}: EtudiantItemProps & HTMLAttributes<HTMLDivElement>) {

    return <div {...props}>
        <a href={"/etudiants/" + etudiant.id}>
            <Button variant={"ghost"} className={"p-1 rounded shadow"}>
                <div className={"flex flex-row gap-6"}>
                    <span className={"w-16"}>{etudiant.prenom}</span>
                    <span className={"w-16"}>{etudiant.nom}</span>
                    <span className={"w-32"}>{etudiant.classe ? etudiant.classe.nom : "sans classe"}</span>
                </div>
            </Button>
        </a>
    </div>
}