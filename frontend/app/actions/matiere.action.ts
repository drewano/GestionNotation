'use server';


import {Matiere} from "@/lib/types/matiere";
import {apiUrl, messageErrorApi} from "@/lib/utils/api";

export async function updateMatiere(idMatiere: number, matiere: Omit<Partial<Matiere>, "id">) {
    const response = await fetch(apiUrl(`/api/matieres/update/${idMatiere}`), {
        method: 'PUT',
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify(matiere),
    });

    if (!response.ok) {
        return messageErrorApi(response);
    }

    return await response.json() as Matiere;
}

export async function addMatiere(matiere: Omit<Partial<Matiere>, "id">) {
    console.log(JSON.stringify(matiere));
    const response = await fetch(apiUrl(`/api/matieres/create`), {
        method: 'POST',
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify(matiere),
    });

    if (!response.ok) {
        return messageErrorApi(response);
    }

    return await response.json() as Matiere;
}

export async function getAllMatieres() {
    const response = await fetch(apiUrl(`/api/matieres/all`));
    if (!response.ok) {
        return messageErrorApi(response);
    }
    return await response.json() as Matiere[];
}

export async function deleteMatiere(idMatiere: number) {
    const response = await fetch(apiUrl(`/api/matieres/delete/${idMatiere}`), {
        method: 'DELETE',
        headers: {
            "Content-Type": "application/json",
        }
    });

    if (!response.ok) {
        return messageErrorApi(response);
    }

    return await response.json() as Matiere;
}