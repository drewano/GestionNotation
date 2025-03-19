'use client'

import {Notation} from "@/lib/types/notation";
import {Button} from "@/components/ui/button";
import {useEffect, useState} from "react";
import {getEtudiantsOfClasse} from "@/app/actions/etudiant.action";
import {Devoir} from "@/lib/types/devoir";
import {getTotalNotesForDevoir} from "@/app/actions/devoir.action";
import {NotationTotale} from "@/lib/types/notation-totale";

export interface DevoirNotationProps {
    devoir: Devoir;
}

export default function DevoirNotations({devoir}: DevoirNotationProps) {
    const [notations, setNotations] = useState<NotationTotale[]>([]);
    const [errors, setErrors] = useState<string | undefined>(undefined);
    useEffect(() => {
        (async () => {
            await getTotalNotesForDevoir(devoir.id).then(
                response => {
                    if (response instanceof Error) {
                        console.error(response.message);
                        setErrors(response.message);
                    }
                    else {
                        setNotations(response);
                    }
                }
            );
        })();
    })

    if (errors) {
        return <>
            {errors}
        </>
    }

    return <>
        <div className={"p-2"}>
            <h2>Notations du devoir</h2>
            {notations.map((notation, index) => (
                <div key={index}>
                    <a href={`/devoirs/detail-etudiant?etudiant=${notation.etudiant.id}&devoir=${devoir.id}`}>
                        <Button variant={"ghost"}>
                            <span className={"w-16"}>{notation.etudiant.prenom}</span>
                            <span className={"w-16"}>{notation.etudiant.nom}</span>
                            <span className={"w-16"}>{notation.noteTotale}</span>
                        </Button>
                    </a>
                </div>
            ))}
        </div>
    </>
}