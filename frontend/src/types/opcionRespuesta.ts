import { ItemOpcionRespuestaDTO } from "./itemOpcionRespuesta";

export interface OpcionRespuestaDTO{
    id: number;
    nombre: string;
    descripcion: string;
    activo: boolean;
    items: ItemOpcionRespuestaDTO[];
}