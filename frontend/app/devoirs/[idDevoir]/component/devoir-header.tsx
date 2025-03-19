'use client';

import {HTMLAttributes} from "react";
import {Devoir} from "@/lib/types/devoir";
import EnumSelect from "@/components/ui/Selects/enum-select";
import {updateDevoir} from "@/app/actions/devoir.action";
import EntitySelect from "@/components/ui/Selects/entity-select";
import {Matiere} from "@/lib/types/matiere";
import {Classe} from "@/lib/types/classe";
import InstantInput from "@/components/ui/Input/instant-input";
import {DatePicker} from "@/components/ui/Input/date-picker";
import {cn} from "@/lib/utils";

export interface DevoirHeaderProps {
    devoir: Devoir;
    matieres: Matiere[];
    classes: Classe[];
}

export default function DevoirHeader({devoir, matieres, classes, ...props} : DevoirHeaderProps & HTMLAttributes<HTMLDivElement>) {

    return <>
        <div className={cn(props.className, "flex flex-col gap-2")} {...props}>
            <EnumSelect enumeration={["CC", "Examen"]} initialValue={devoir.typeDevoir} label={"type de devoir"}
                        onChange={value => updateDevoir(devoir.id, {typeDevoir: value as "CC" | "Examen"})}
            />
            <EntitySelect label={"matiere"} entities={matieres} initialValue={devoir.matiere}
                          getNom={(matiere: Matiere) => matiere.nom}
                          onEntitySelected={matiere => updateDevoir(devoir.id, {matiere: matiere})}
            />
            <EntitySelect label={"classe"} entities={classes} initialValue={devoir.classe}
                          getNom={(classe: Classe) => classe.nom}
                          onEntitySelected={classe => updateDevoir(devoir.id, {classe: classe})}
            />
            <InstantInput label={"Coefficient"} initialValue={devoir.coefficient}
                          onValidate={(value: number) => updateDevoir(devoir.id, {coefficient: value})}
            />
            <DatePicker initialDate={devoir.dateDevoir} label={"Date de devoir"}
                        onDateChange={(date) => updateDevoir(devoir.id, {dateDevoir: date})}
            />
        </div>
    </>
}