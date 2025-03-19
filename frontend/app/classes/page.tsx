import {apiUrl} from "@/lib/utils/api";
import {Classe} from '@/lib/types/classe';
import ClasseItem from "@/app/classes/components/classe-item";

export default async function ClassesPage() {

    let response = await fetch(apiUrl("/api/classes/all"));

    if (!response.ok) {
        return <>
            {await response.json()}
        </>
    }

    let classes: Classe[] = await response.json();

    return <>
        <div className={"p-2"}>
            <h1>Liste des Classes</h1>
            <div className={"flex flex-col"}>
                {classes.map(classe => (
                    <ClasseItem key={classe.id} classe={classe}/>
                ))}
            </div>
        </div>
    </>
}