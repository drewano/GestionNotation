import {apiUrl} from "@/lib/utils/api";
import {Devoir} from "@/lib/types/devoir";
import DevoirHeader from "@/app/devoirs/[idDevoir]/component/devoir-header";
import {Matiere} from "@/lib/types/matiere";
import {Classe} from "@/lib/types/classe";
import DevoirParties from "@/app/devoirs/[idDevoir]/component/devoir-parties";
import {Separator} from "@/components/ui/separator";
import DevoirNotations from "@/app/devoirs/[idDevoir]/component/devoir-notations";

export default async function DevoirDashboard(
    {params}: {params: Promise<{idDevoir: number}>}) {
    const idDevoir = (await params).idDevoir;

    const responseDevoir = await fetch(apiUrl(`/api/devoirs/${idDevoir}`));
    const responseMatieres = await fetch(apiUrl(`/api/matieres/all`));
    const responseClasses = await fetch(apiUrl(`/api/classes/all`));

    if (!responseDevoir.ok) {
        return <p>
            {await responseDevoir.json()}
        </p>
    }
    if (!responseMatieres.ok) {
        return <p>
            {await responseMatieres.json()}
        </p>
    }
    if (!responseClasses.ok) {
        return <p>
            {await responseClasses.json()}
        </p>
    }

    const data = await responseDevoir.json();
    const devoir: Devoir = {...data, dateDevoir: new Date(data.dateDevoir)};
    const matieres: Matiere[] = await responseMatieres.json();
    const classes: Classe[] = await responseClasses.json();


    return <>
        <div className={"p-2"}>
            <h1>Portail Devoir</h1>
            <DevoirHeader devoir={devoir} matieres={matieres} classes={classes}/>
            <br/>
            <Separator/>
            <DevoirParties devoir={devoir}/>
            <br/>
            <Separator/>
            <DevoirNotations devoir={devoir}/>
        </div>
    </>
}