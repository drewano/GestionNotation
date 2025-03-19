'use client';

import {Commun} from "@/lib/types/commun";
import {Select, SelectContent, SelectItem, SelectTrigger, SelectValue} from "@/components/ui/Selects/select";
import {useState} from "react";
import {Label} from "@/components/ui/label";

export interface EntitySelectProps<T extends Commun> {
    entities: T[];
    label?: string;
    initialValue: T | undefined;
    getNom?: (value: T) => string;
    onEntitySelected?: (entity: T) => any;
}

export default function EntitySelect<T extends Commun>(
    {entities, label, initialValue, getNom, onEntitySelected, ...props}: EntitySelectProps<T>) {

    const [value, setValue] = useState(initialValue);

    const onSelected = (value: string) => {
        const entitySelected = entities.find(entity => entity.id === Number.parseInt(value));

        if (!entitySelected) return;

        setValue(entitySelected);
        if (onEntitySelected) {
            onEntitySelected(entitySelected)
        }
    }

    return <>
        {label && <Label>{label}</Label>}
        <Select onValueChange={onSelected}>
            <SelectTrigger>
                <SelectValue placeholder={value ? getNom  ? getNom(value) : value.id : "selectionner une entrée"}/>
            </SelectTrigger>
            <SelectContent>
                {entities.map(entity => (
                    <SelectItem key={entity.id} value={entity.id.toString()}>
                        {getNom ? getNom(entity) : entity.id}
                    </SelectItem>
                ))}
            </SelectContent>
        </Select>
    </>
}