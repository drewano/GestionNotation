import {Sidebar, SidebarContent, SidebarGroup, SidebarHeader} from "@/components/ui/sidebar";
import {Button} from "@/components/ui/button";
import {Separator} from "@radix-ui/react-select";

export default function NavSidebar() {

    return <div className={" p-4 flex flex-col w-48 h-screen bg-gray-200"}>
        <p>Gestionnaire de classe</p>
        <br/>
        <p className={"text-xs text-gray-500"}>navigation</p>
        <div className={"flex flex-col w-full gap-1"}>
            <div className={"flex flex-row w-full"}>
                <a href={"/classes"}><Button variant={"ghost"}>
                    <span>Classes</span>
                </Button></a>
            </div>
            <div className={"flex flex-row w-full"}>
                <a href={"/devoirs"}><Button variant={"ghost"}>
                    <span>Devoirs</span>
                </Button></a>
            </div>
            <div className={"flex flex-row w-full"}>
                <a href={"/etudiants"}><Button variant={"ghost"}>
                    <span>Etudiants</span>
                </Button></a>
            </div>
            <div className={"flex flex-row w-full"}>
                <a href={"/matieres"}><Button variant={"ghost"}>
                    <span>Matieres</span>
                </Button></a>
            </div>
        </div>
    </div>

}
