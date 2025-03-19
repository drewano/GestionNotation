'use client';

import {Select, SelectContent, SelectItem, SelectTrigger, SelectValue} from "@/components/ui/Selects/select";
import {useState} from "react";
import {Label} from "@/components/ui/label";

export interface EntitySelectProps<T extends string[]> {
    enumeration: T;
    label?: string;
    initialValue: string;
    onChange?: (value: T[number] ) => any;
}

export default function EnumSelect<T extends string[]>(
    {enumeration, label, initialValue, onChange, ...props}: EntitySelectProps<T>) {

    const [value, setValue] = useState(initialValue);

    const onSelected = (value: string) => {

        setValue(value);
        if (onChange) {
            onChange(value)
        }
    }

    return <>
        {label && <Label>{label}</Label>}
        <Select onValueChange={onSelected}>
            <SelectTrigger>
                <SelectValue placeholder={value ? value : "selectionner une entrée"}/>
            </SelectTrigger>
            <SelectContent>
                {enumeration.map((enumValue, index) => (
                    <SelectItem key={index} value={enumValue}>
                        {enumValue}
                    </SelectItem>
                ))}
            </SelectContent>
        </Select>
    </>
}