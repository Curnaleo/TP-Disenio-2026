import { EstadoCuestionario } from "./estadoCuestionario";

// Opción tal como la manda el back al candidato (sin ponderación)
export interface OpcionExamenDTO {
    idOpcionPregunta: number;
    orden: number;
    descripcion: string;
}

// Igual a PreguntaExamenDTO del back
export interface PreguntaExamenDTO {
    idPregCuest: number;
    nroOrden: number;
    textoPregunta: string;
    descripcion: string | null;
    opciones: OpcionExamenDTO[];
}

// Igual a RespuestaCandidatoDTO del back
export interface RespuestaCandidatoDTO {
    idPregCuest: number;
    idOpcionPregunta: number;
}

export interface BloqueCuestionario {
    nroBloque: number;
    totalBloques: number;
    preguntas: PreguntaExamenDTO[];
}

export interface CuestionarioActual {
    idCuestionario: number;
    nombreCandidato: string;
    estado: EstadoCuestionario;
    instrucciones: string;
}

// Por qué no se puede seguir con el cuestionario (1.A, 1.B y precondición de accesos)
export type MotivoCierre = "SIN_CONTESTAR" | "INCOMPLETO" | "ACCESOS_SUPERADOS";
