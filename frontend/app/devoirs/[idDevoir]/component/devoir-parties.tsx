'use client';

import {Devoir} from "@/lib/types/devoir";
import InstantInput from "@/components/ui/Input/instant-input";
import {useEffect, useState} from "react";
import {updateDevoir} from "@/app/actions/devoir.action";
import {Button} from "@/components/ui/button";
import {TrashIcon} from "lucide-react";
import {useRouter} from "next/navigation";

export interface DevoirPartiesProps {
    devoir: Devoir;
}

export default function DevoirParties({devoir}: DevoirPartiesProps) {

    const [haveToReload, setHaveToReload] = useState(false);
    const [parties, setParties] = useState(devoir.partiedevoirs);
    const router = useRouter();

    useEffect(() => {
        updateDevoir(devoir.id, {partiedevoirs: parties}).then(response => {
            if (response instanceof Error) {
                console.error(response.message);
            }
            else {
                setParties(response.partiedevoirs);
            }
            setHaveToReload(false);
        })
    }, [haveToReload])

    return <>
        <h2>Parties du devoir</h2>
        <p className={"text-gray-500 text-xs"}>(le total des points ne peut pas excéder 20 points)</p>
        <Button variant={"outline"}
                onClick={_ => {
                    setParties([...parties, {id: 0, points: 0}]);
                    setHaveToReload(true);
                }}
        >
            <span className={"text-xs"}>ajouter une partie</span>
        </Button>
        <div className={"p-2 flex flex-col gap-2"}>
            {parties.map((partie, index) => (
                <div key={partie.id}
                     className={"flex items-center gap-2 p-2 bg-gray-200 rounded-md"}>
                    <span className={"w-16"}>Partie {index + 1}</span>
                    <InstantInput initialValue={partie.points}
                                  label={"Points"}
                                  onValidate={(value: number) => {
                                      parties[index].points = value;
                                      setParties([...parties]);
                                      setHaveToReload(true);
                                  }}
                    />
                    <Button variant={"outline"} onClick={_ => {
                        setParties([...(parties.filter(item => item.id !== partie.id))]);
                        setHaveToReload(true);
                    }}>
                        <TrashIcon/>
                    </Button>
                </div>
            ))}
        </div>
    </>
}