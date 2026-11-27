import { ItemOpcionRespuestaDTO } from "./itemOpcionRespuesta";

export interface OpcionRespuestaDTO{
    id: number;
    nombre: string;
    descripcion: string | null;
    activo: boolean;
    items: ItemOpcionRespuestaDTO[];
}