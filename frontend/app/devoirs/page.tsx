'use client';


import {useEffect, useState} from "react";
import {Devoir} from "@/lib/types/devoir";
import {getAllDevoirs} from "@/app/actions/devoir.action";
import DevoirItem from "@/app/devoirs/components/devoir-item";

export default function NotationHomePage() {

    const [loading, setLoading] = useState(false);
    const [devoirs, setDevoirs] = useState<Devoir[]>([]);
    const [error, setError] = useState<Error | null>(null);

    useEffect(() => {
        setLoading(true);
        const fetchDevoirs = async () => {
            const response = await getAllDevoirs();

            setLoading(false);

            if (response instanceof Error) {
                setError(response);
                return
            }

            setDevoirs(response);
        }

        fetchDevoirs();
    }, [])

    if (error) return <div> {error.message} </div>

    if (loading) return <div>Loading...</div>;

    return <div className={"p-2"}>
        <h1>Liste des devoirs</h1>
        {devoirs && devoirs.map(devoir => (
            <DevoirItem key={devoir.id} devoir={devoir}/>
        ))}
    </div>
}