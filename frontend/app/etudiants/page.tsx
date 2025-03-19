import {apiUrl} from "@/lib/utils/api";
import {Etudiant} from "@/lib/types/etudiant";
import EtudiantItem from "@/app/etudiants/components/etudiant-item";

export default async function EtudiantHomePage() {
    const response = await fetch(apiUrl("/api/etudiants/all"));

    if (!response.ok) {
        return <>
            {JSON.stringify(await response.json())}
        </>
    }

    const etudiants: Etudiant[] = await response.json();

    return <div className={"p-2"}>
        <h1>Liste des étudiants</h1>
        {etudiants.map(etudiant => (
            <EtudiantItem key={etudiant.id} etudiant={etudiant} />
        ))}
    </div>
}