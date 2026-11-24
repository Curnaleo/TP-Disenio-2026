import { OpcionPreguntaDTO } from "./opcionPregunta";

export interface PreguntaDTO{
    idPregunta: number;
    nombre: string;
    textoPregunta: string;
    descripcion: string | null;
    activo: boolean;
    nombreCompetencia: string;
    idCompetencia: number;
    nombreFactor: string;
    idFactor: number;
    idOpcionRespuesta: number;
    nombreOpcionRespuesta: string;
    opcionesPregunta: OpcionPreguntaDTO[];
}