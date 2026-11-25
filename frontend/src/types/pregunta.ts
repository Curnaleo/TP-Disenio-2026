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
    codigoFactor: string;
    idOpcionRespuesta: number;
    nombreOpcionRespuesta: string;
    opcionesPregunta: OpcionPreguntaDTO[];
}
