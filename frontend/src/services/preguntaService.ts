//Cuando haya API, cambiar el interior de cada función y las pantallas no se tocan.

import { PreguntaDTO } from "@/types/pregunta";
import { COMPETENCIAS, FACTORES, OPCIONES_RESPUESTA } from "./catalogoService";

export interface FiltrosPregunta {
    competencia: string;
    factor: string;
    pregunta: string;
}

export interface NuevaPreguntaRequest {
    idCompetencia: number;
    idFactor: number;
    nombre: string;
    textoPregunta: string;
    descripcion: string | null;
    idOpcionRespuesta: number;
    ponderaciones: number[]; // en el orden de la grilla de opciones
}

// El back va a responder 409;
export class PreguntaDuplicadaError extends Error {
    constructor(public campo: "nombre" | "texto") {
        super("La pregunta ya existe");
    }
}

// Datos de prueba: cuando exista la API, se reemplazan por un fetch
const PREGUNTAS_MOCK: PreguntaDTO[] = [
    {
        idPregunta: 1,
        nombre: "PLANIFICACIÓN",
        textoPregunta: "¿Cómo evalúa la planificación estratégica?",
        descripcion: null,
        activo: true,
        nombreCompetencia: "Liderazgo",
        codigoCompetencia: "LID",
        nombreFactor: "Estratégico",
        codigoFactor: "EST",
        idOpcionRespuesta: 1,
        nombreOpcionRespuesta: "Opción 1",
        opcionesPregunta: [],
    },
    {
        idPregunta: 2,
        nombre: "ALCANCE",
        textoPregunta: "¿Cómo gestiona el alcance del proyecto?",
        descripcion: null,
        activo: true,
        nombreCompetencia: "Gestión",
        codigoCompetencia: "GES",
        nombreFactor: "Alcance",
        codigoFactor: "ALC",
        idOpcionRespuesta: 1,
        nombreOpcionRespuesta: "Opción 1",
        opcionesPregunta: [],
    },
];

// El CU020 pide criterio "contiene"
const contiene = (valor: string, filtro: string) =>
    valor.toLowerCase().includes(filtro.trim().toLowerCase());

const igual = (a: string, b: string) => a.trim().toUpperCase() === b.trim().toUpperCase();

export async function buscarPreguntas(f: FiltrosPregunta): Promise<PreguntaDTO[]> {
    return PREGUNTAS_MOCK.filter(
        (p) =>
            contiene(p.nombreCompetencia, f.competencia) &&
            contiene(p.nombreFactor, f.factor) &&
            contiene(p.nombre, f.pregunta) // el CU020 filtra por "Nombre de Pregunta"
    );
}

export async function listarCompetencias(): Promise<string[]> {
    return [...new Set(PREGUNTAS_MOCK.map((p) => p.nombreCompetencia))];
}

export async function listarFactores(): Promise<string[]> {
    return [...new Set(PREGUNTAS_MOCK.map((p) => p.nombreFactor))];
}

export async function crearPregunta(req: NuevaPreguntaRequest): Promise<PreguntaDTO> {
    const competencia = COMPETENCIAS.find((c) => c.id === req.idCompetencia);
    const factor = FACTORES.find((f) => f.id === req.idFactor);

    // CU021 obs. (1): la existencia se valida por nombre y por texto, dentro de competencia y factor
    const duplicada = PREGUNTAS_MOCK.find(
        (p) =>
            p.codigoCompetencia === competencia?.codigo &&
            p.codigoFactor === factor?.codigo &&
            (igual(p.nombre, req.nombre) || igual(p.textoPregunta, req.textoPregunta))
    );
    if (duplicada) {
        throw new PreguntaDuplicadaError(igual(duplicada.nombre, req.nombre) ? "nombre" : "texto");
    }

    const nueva: PreguntaDTO = {
        idPregunta: Math.max(0, ...PREGUNTAS_MOCK.map((p) => p.idPregunta)) + 1,
        nombre: req.nombre,
        textoPregunta: req.textoPregunta,
        descripcion: req.descripcion,
        activo: true,
        nombreCompetencia: competencia?.nombre ?? "",
        codigoCompetencia: competencia?.codigo ?? "",
        nombreFactor: factor?.nombre ?? "",
        codigoFactor: factor?.codigo ?? "",
        idOpcionRespuesta: req.idOpcionRespuesta,
        nombreOpcionRespuesta:
            OPCIONES_RESPUESTA.find((o) => o.id === req.idOpcionRespuesta)?.nombre ?? "",
        opcionesPregunta: [], // el mock no guarda las ponderaciones
    };
    PREGUNTAS_MOCK.push(nueva);
    return nueva;
}