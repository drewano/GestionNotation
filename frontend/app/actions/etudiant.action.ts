'use server';

import {Etudiant} from "@/lib/types/etudiant";
import {apiUrl, messageErrorApi} from "@/lib/utils/api";

export async function updateEtudiant(idEtudiant: number, etudiant: Omit<Partial<Etudiant>, "id">) {
    const response = await fetch(apiUrl(`/api/etudiants/update/${idEtudiant}`), {
        method: "PUT",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(etudiant)
    });

    if (!response.ok) {
        return await messageErrorApi(response);
    }

    return await response.json() as Etudiant;
}

export async function getEtudiantsOfClasse(idClasse: number) {
    const response = await fetch(apiUrl(`/api/etudiants/classe/${idClasse}`));
    if (!response.ok) {
        return await messageErrorApi(response);
    }
    return await response.json() as Etudiant[];
}