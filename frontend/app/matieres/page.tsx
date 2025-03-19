'use client';

import {Matiere} from "@/lib/types/matiere";
import MatiereItem from "@/app/matieres/components/matiere-item";
import {Button} from "@/components/ui/button";
import {addMatiere, getAllMatieres} from "@/app/actions/matiere.action";
import {useState, useEffect} from "react";
import {Separator} from "@radix-ui/react-select";


export default function MatieresPageHome() {
    const [matieres, setMatieres] = useState<Matiere[]>([]);

    useEffect(() => {
        getAllMatieres().then(response => {
            if (response instanceof Error) {
                console.error(response)
            }
            else {
                setMatieres(response);
            }
        })

    }, [])

    return <>
        <div className={"p-2"}>
            <h1>Matieres scolaire</h1>
            <Button variant={"outline"}
                    onClick={_ => {
                        addMatiere({nom:"nouvelle matière"}).then(response => {
                            if (response instanceof Error) {
                                console.log(response)
                            }
                            else {
                                setMatieres([...matieres, response]);
                            }
                        })
                    }}>
                <span className={"text-xs"}>ajouter une matière</span>
            </Button>
            <div className={"flex flex-col gap-2"}>
                {matieres.map((matiere, index) => (
                    <div key={matiere.id}>
                        <MatiereItem matiere={matiere} onDelete={() => setMatieres([...(matieres.filter(item => item.id !== matiere.id))])}/>
                    </div>
                ))}
            </div>
        </div>
    </>
}