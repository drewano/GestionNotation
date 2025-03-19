import {Etudiant} from "@/lib/types/etudiant";
import {apiUrl} from "@/lib/utils/api";
import {Button} from "@/components/ui/button";
import {Input} from "@/components/ui/Input/input";
import InstantInput from "@/components/ui/Input/instant-input";
import {updateEtudiant} from "@/app/actions/etudiant.action";
import EtudiantHeader from "@/app/etudiants/[etudiantId]/components/etudiant-header";
import {Classe} from "@/lib/types/classe";

export default async function EtudiantPage({params}: {params: Promise<{etudiantId: number}>}) {
    const etudiantId = (await params).etudiantId;

    const responseEtudiant = await fetch(apiUrl(`/api/etudiants/${etudiantId}`))
    const responseClasses = await fetch(apiUrl(`/api/classes/all`))

    if (!responseEtudiant.ok) {
        return <>
            {JSON.stringify(await responseEtudiant.json())}
        </>
    }

    if (!responseClasses.ok) {
        return <>
            {JSON.stringify(await responseClasses.json())}
        </>
    }

    const etudiant: Etudiant = await responseEtudiant.json();
    const classes: Classe[] = await responseClasses.json();

    return <div className={"p-2"}>
        <a href={"/etudiants"}><Button>{"< retour"}</Button></a>
        <h1>Portail Etudiant</h1>
        <EtudiantHeader etudiant={etudiant} classes={classes}/>
    </div>
}