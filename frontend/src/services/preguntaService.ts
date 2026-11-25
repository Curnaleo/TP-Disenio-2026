//Cuando haya API, cambiar el interior de cada función y las pantallas no se tocan.

import { PreguntaDTO } from "@/types/pregunta";

export interface FiltrosPregunta {
    competencia: string;
    factor: string;
    pregunta: string;
}

// Datos de prueba: cuando exista la API, se reemplazan por un fetch
const PREGUNTAS_MOCK: PreguntaDTO[] = [
    {
        idPregunta: 1,
        nombre: "PLANIFICACION",
        textoPregunta: "¿Cómo evalúa la planificación estratégica?",
        descripcion: null,
        activo: true,
        nombreCompetencia: "Liderazgo",
        idCompetencia: 1,
        nombreFactor: "Estratégico",
        idFactor: 1,
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
        idCompetencia: 2,
        nombreFactor: "Alcance",
        idFactor: 2,
        idOpcionRespuesta: 1,
        nombreOpcionRespuesta: "Opción 1",
        opcionesPregunta: [],
    },
];

// El CU020 pide criterio "contiene"
const contiene = (valor: string, filtro: string) =>
    valor.toLowerCase().includes(filtro.trim().toLowerCase());

export async function buscarPreguntas(f: FiltrosPregunta): Promise<PreguntaDTO[]> {
    return PREGUNTAS_MOCK.filter(
        (p) =>
            contiene(p.nombreCompetencia, f.competencia) &&
            contiene(p.nombreFactor, f.factor) &&
            contiene(p.textoPregunta, f.pregunta)
    );
}

export async function listarCompetencias(): Promise<string[]> {
    return [...new Set(PREGUNTAS_MOCK.map((p) => p.nombreCompetencia))];
}

export async function listarFactores(): Promise<string[]> {
    return [...new Set(PREGUNTAS_MOCK.map((p) => p.nombreFactor))];
}