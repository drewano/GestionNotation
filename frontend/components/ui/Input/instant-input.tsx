'use client';

import React, {HTMLAttributes} from "react";
import {cn} from "@/lib/utils";
import {Input} from "@/components/ui/Input/input";
import {Label} from "@/components/ui/label";

export interface InstantInputProps {
    label?: string;
}

export interface InstantInputPropsNumber {
    initialValue: number;
    onValidate?: (value: number) => void;
}

export interface InstantInputPropsString {
    initialValue: string;
    onValidate?: (value: string) => void;
}

export default function InstantInput({label, initialValue, onValidate, ...props}:
    InstantInputProps & (InstantInputPropsNumber | InstantInputPropsString) & HTMLAttributes<HTMLDivElement>
) {

    const [previousValue, setPreviousValue] = React.useState(initialValue);
    const [value, setValue] = React.useState(initialValue);

    const validate = async () => {
        console.log("validation!!!!!")
        setPreviousValue(value);
        if (onValidate) {
            // @ts-ignore
            onValidate(value);
        }
    }

    const onKeyDown = (event: React.KeyboardEvent<HTMLInputElement>) => {
        if (event.key === "Enter") {
            validate();
        }
        else if (event.key === "Escape") {
            setValue(previousValue);
        }
    }

    return <>
        <div className={cn("", props.className)} {...props} >
            {label && <Label>{label}</Label>}
            <Input value={value} onChange={e => setValue(e.target.value)}
                   onBlur={_ => validate()}
                   onKeyDown={onKeyDown}
            />
        </div>
    </>
}