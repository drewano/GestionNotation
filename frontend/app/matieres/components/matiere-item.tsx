'use client';

import {Matiere} from "@/lib/types/matiere";
import InstantInput from "@/components/ui/Input/instant-input";
import {deleteMatiere, updateMatiere} from "@/app/actions/matiere.action";
import {TrashIcon} from "lucide-react";
import {Button} from "@/components/ui/button";

export interface MatiereItemProps {
    matiere: Matiere;
    onDelete?: () => void;
}

export default function MatiereItem({matiere, onDelete}: MatiereItemProps) {

    return <div className={"flex floex-col align-center gap-2"}>
        <InstantInput initialValue={matiere.nom}
                      label={"nom de la matière"}
                      onValidate={
            (nom: string) => updateMatiere(matiere.id, {nom})}
        />
        <Button variant={"outline"}
                onClick={_ => {
                    deleteMatiere(matiere.id).then(response => {
                        if (response instanceof Error) {
                            console.error(response);
                        }
                        else {
                            if (onDelete) onDelete();
                        }
                    });
                }}>
            <TrashIcon/>
        </Button>

    </div>
}