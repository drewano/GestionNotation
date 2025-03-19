import {apiUrl} from "@/lib/utils/api";
import {Classe} from "@/lib/types/classe";
import {Button} from "@/components/ui/button";
import ClasseHeader from "@/app/classes/[classeId]/components/classe-name";

export default async function ClassePage({params} :{
    params: Promise<{classeId: number}>
}) {
    const classeId = (await params).classeId;

    const response = await fetch(apiUrl(`/api/classes/${classeId}`));

    if (!response.ok) {
        return <>
            {await response.json()}
        </>
    }

    const classe: Classe = await response.json();

    return <div>
        <a href={"/classes"}><Button>{"< retour"}</Button></a>
        <h1>Portail Classe</h1>
        <ClasseHeader classe={classe}/>
        {/*todo: liste des étudiants*/}
        {/*todo: liste des devoirs*/}
    </div>
}