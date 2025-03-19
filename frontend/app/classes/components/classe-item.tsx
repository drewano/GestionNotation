'use client';

import {Classe} from "@/lib/types/classe";
import {HTMLAttributes} from "react";
import {cn} from "@/lib/utils";
import {Button} from "@/components/ui/button";

export interface ClasseItemProps {
    classe: Classe
}

export default function ClasseItem({classe, ...props}:
    ClasseItemProps & HTMLAttributes<HTMLDivElement>) {

    return <div className={cn("flex", props.className)} {...props}>
        <a href={`/classes/${classe.id}`}><Button variant={"ghost"} className={"p-1 w-32 rounded shadow"}>{classe.nom}</Button></a>
    </div>
}