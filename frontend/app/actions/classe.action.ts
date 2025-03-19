'use server';

import {apiUrl, messageErrorApi} from "@/lib/utils/api";
import {Classe} from "@/lib/types/classe";

export async function updateClasse(idClasse: number, nvNom: string) {
    const response = await fetch(apiUrl(`/api/classes/update/${idClasse}`),
        {
            method: "PUT",
            body: JSON.stringify({nom: nvNom}),
            headers: {
                "Content-Type": "application/json",
            }
        });

    if (!response.ok) {
        return await messageErrorApi(response);
    }

    return await response.json() as Classe;
}