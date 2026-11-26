import {ItemOpcionRespuestaDTO} from "@/types/itemOpcionRespuesta";

export interface OpcionPreguntaDTO{
    idOpcionPregunta: number;
    orden: number;
    descripcion: string;
    ponderacion: number;
    referenciaTexto: ItemOpcionRespuestaDTO;

}