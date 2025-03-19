'use client';

import {Etudiant} from "@/lib/types/etudiant";
import {HTMLAttributes} from "react";
import InstantInput from "@/components/ui/Input/instant-input";
import {updateEtudiant} from "@/app/actions/etudiant.action";
import {Classe} from "@/lib/types/classe";
import EntitySelect from "@/components/ui/Selects/entity-select";

export interface EtudiantHeaderProps {
    etudiant: Etudiant;
    classes: Classe[];
}

export default function EtudiantHeader(
    {etudiant, classes, ...props}: EtudiantHeaderProps & HTMLAttributes<HTMLDivElement>,) {

    return <div {...props}>
        <InstantInput label={"prénom"} initialValue={etudiant.prenom}
                      onValidate={(value: string) => updateEtudiant(etudiant.id, {prenom: value})} />
        <InstantInput label={"nom"} initialValue={etudiant.nom}
                      onValidate={(value: string) => updateEtudiant(etudiant.id, {nom: value})} />
        <EntitySelect entities={classes}
                      label={"Classe"}
                      onEntitySelected={classe => updateEtudiant(etudiant.id, {classe: classe})}
                      getNom={(classe: Classe) => classe.nom}
                      initialValue={etudiant.classe ?? undefined}/>
    </div>
}