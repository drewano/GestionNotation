'use client';

import {Classe} from "@/lib/types/classe";
import React, {HTMLAttributes} from "react";
import {Input} from "@/components/ui/Input/input";
import {updateClasse} from "@/app/actions/classe.action";
import InstantInput from "@/components/ui/Input/instant-input";

export interface ClasseNameProps {
    classe: Classe;
}

export default function ClasseName({classe, ...props}: ClasseNameProps & HTMLAttributes<HTMLDivElement>) {

    const [localClasse, setLocalClasse] = React.useState<Classe>(classe);

    return <div {...props}>
        <InstantInput label={"nom de la classe"} initialValue={classe.nom}
                      onValidate={(value: string) => updateClasse(classe.id, value).then(
                          response => {
                              if (response instanceof Error) {
                                  console.error(response.message);
                              }
                              else {
                                  setLocalClasse(response);
                              }
                          }
                      )}/>
    </div>
}