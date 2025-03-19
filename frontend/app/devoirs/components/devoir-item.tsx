import {HTMLProps} from "react";
import {Devoir} from "@/lib/types/devoir";
import {Button} from "@/components/ui/button";

export interface DevoirItemProps {
    devoir: Devoir;
}

export default function DevoirItem({devoir, ...props}: DevoirItemProps & HTMLProps<HTMLDivElement>) {

    // console.log(typeof devoir.dateDevoir);

    return <div {...props}>
        <a href={"/devoirs/" + devoir.id}>
            <Button variant={"ghost"} className={"p-1 rounded shadow"}>
                <div className={"flex flex-row gap-6 w-fit"}>
                    <span className={"w-16"}>{devoir.matiere.nom}</span>
                    <span className={"w-16"}>{devoir.classe.nom}</span>
                    <span className={"w-32"}>{devoir.dateDevoir.toLocaleDateString()}</span>
                </div>
            </Button>
        </a>
    </div>
}