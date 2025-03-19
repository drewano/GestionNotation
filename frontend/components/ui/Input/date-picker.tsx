"use client"

import * as React from "react"
import { format } from "date-fns"
import { CalendarIcon } from "lucide-react"

import { cn } from "@/lib/utils"
import { Button } from "@/components/ui/button"
import {Calendar} from "@/components/ui/calendar"
import {
    Popover,
    PopoverContent,
    PopoverTrigger,
} from "@/components/ui/popover"
import {DayPickerSingleProps} from "react-day-picker";
import {useEffect} from "react";

export interface DatePickerProps {
    initialDate?: Date
    label?: string
    onDateChange?: (date: Date) => void
}

export function DatePicker(
    { initialDate, label, onDateChange, ...props }: DatePickerProps) {
    const [date, setDate] = React.useState<Date | undefined>(initialDate ??  new Date())

    useEffect(() => {
        if (date && onDateChange) onDateChange(date);
    }, [date])

    return (
        <Popover>
            {label && <label className="block mb-1 text-sm font-medium text-foreground">{label}</label>}
            <PopoverTrigger asChild>
                <Button
                    variant={"outline"}
                    className={cn(
                        "w-[280px] justify-start text-left font-normal",
                        !date && "text-muted-foreground"
                    )}
                >
                    <CalendarIcon />
                    {date ? format(date, "PPP") : <span>Pick a date</span>}
                </Button>
            </PopoverTrigger>
            <PopoverContent className="w-auto p-0">
                <Calendar

                    mode="single"
                    selected={date}
                    onSelect={setDate}
                    initialFocus
                />
            </PopoverContent>
        </Popover>
    )
}
