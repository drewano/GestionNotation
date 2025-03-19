'use server';

import {apiUrl, messageErrorApi} from "@/lib/utils/api";
import {Devoir} from "@/lib/types/devoir";
import {Notation} from "@/lib/types/notation";
import {NotationTotale} from "@/lib/types/notation-totale";

export async function getAllDevoirs() {
    const response = await fetch(apiUrl("/api/devoirs/all"));

    if (!response.ok) {
        return messageErrorApi(response);
    }

    const data = await response.json();
    const devoirs: Devoir[] = data.map((datum: any) =>
        ({...datum, dateDevoir: new Date(datum.dateDevoir)} as Devoir));

    return devoirs;
}

export async function updateDevoir(idDevoir: number, devoir: Omit<Partial<Devoir>, "id">) {
    console.log(JSON.stringify(devoir));
    const response = await fetch(apiUrl(`/api/devoirs/update/${idDevoir}`), {
        method: "PUT",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify(devoir),
    });

    if (!response.ok) {
        return messageErrorApi(response);
    }

    const data = await response.json();
    return {...data, dateDevoir: new Date(data.dateDevoir)} as Devoir;
}

export async function getTotalNotationByEtudiant(idDevoir: number, idEtudiant: number) {
    const response = await fetch(apiUrl(`/api/notation/etudiant/${idDevoir}/devoir/{idEtudiant}/total`))
    if (!response.ok) {
        return messageErrorApi(response);
    }
    return await response.json() as Notation;
}

export async function getTotalNotesForDevoir(idDevoir: number) {
    const response = await fetch(apiUrl(`/api/notation/devoir/${idDevoir}/notes`))
    if (!response.ok) {
        return messageErrorApi(response);
    }
    return await response.json() as NotationTotale[];
}