// CU026 - Completar Cuestionario.
// El back todavía no expone endpoints de cuestionario (solo existe GestorCuestionario, sin controller),
// así que por ahora todo es mock
// y la pantalla no se toca.
//
// Para probar los flujos alternativos en modo mock, abrir /cuestionario?escenario=<valor>:
//   activo (default) | en-proceso (1.C) | vencido-activo (1.B) | vencido-tiempo (1.A) | accesos (precondición)

import { obtenerCandidato, obtenerClaimsToken } from "@/lib/session";
import {
    BloqueCuestionario,
    CuestionarioActual,
    MotivoCierre,
    PreguntaExamenDTO,
    RespuestaCandidatoDTO,
} from "@/types/cuestionario";
import { EstadoCuestionario } from "@/types/estadoCuestionario";

// Se lanza cuando el cuestionario ya no se puede responder (1.A, 1.B o accesos superados)
export class CuestionarioCerradoError extends Error {
    constructor(public motivo: MotivoCierre) {
        super(`Cuestionario cerrado: ${motivo}`);
    }
}

// ---------------------------------------------------------------- mock

type EscenarioMock = "activo" | "en-proceso" | "vencido-activo" | "vencido-tiempo" | "accesos";

const INSTRUCCIONES_MOCK =
    "El cuestionario está dividido en bloques de preguntas. En cada bloque tenés que responder " +
    "todas las preguntas antes de avanzar, eligiendo una sola opción por pregunta.\n\n" +
    "Una vez que avances al siguiente bloque no vas a poder modificar las respuestas del bloque anterior. " +
    "Si salís antes de terminar, se guardan los bloques ya completados y podrás continuar más tarde " +
    "desde el bloque pendiente, siempre que no se supere el tiempo máximo ni la cantidad máxima de accesos.\n\n" +
    "Respondé con sinceridad: no hay respuestas correctas o incorrectas.";

const OPCIONES_FRECUENCIA = ["Nunca", "A veces", "Casi siempre", "Siempre"];

const TEXTOS_MOCK: string[][] = [
    [
        "Cuando un proyecto se atrasa, reorganizo las tareas para cumplir con la fecha comprometida.",
        "Planifico mis actividades de la semana antes de comenzarla.",
        "Comunico al equipo los riesgos que detecto, aunque no me los hayan pedido.",
    ],
    [
        "Respeto las decisiones de la organización aunque no esté de acuerdo con ellas.",
        "Defiendo a mis compañeros ante críticas injustas.",
        "Mantengo la confidencialidad de la información que manejo en mi trabajo.",
    ],
    [
        "Busco nuevas formas de resolver problemas habituales.",
        "Pido opinión a otras personas antes de tomar una decisión importante.",
        "Acepto con facilidad los cambios en la forma de trabajar.",
    ],
];

const BLOQUES_MOCK: PreguntaExamenDTO[][] = TEXTOS_MOCK.map((textos, b) =>
    textos.map((texto, i) => {
        const idPregCuest = (b + 1) * 100 + i + 1;
        return {
            idPregCuest,
            nroOrden: b * textos.length + i + 1,
            textoPregunta: texto,
            descripcion: null,
            opciones: OPCIONES_FRECUENCIA.map((descripcion, o) => ({
                idOpcionPregunta: idPregCuest * 10 + o + 1,
                orden: o + 1,
                descripcion,
            })),
        };
    })
);

// Bloques ya guardados en esta sesión del navegador (se pierde al recargar)
const bloquesGuardadosMock = new Set<number>();

function escenarioMock(): EscenarioMock {
    if (typeof window === "undefined") return "activo";
    const valor = new URLSearchParams(window.location.search).get("escenario");
    const validos: EscenarioMock[] = ["activo", "en-proceso", "vencido-activo", "vencido-tiempo", "accesos"];
    return validos.includes(valor as EscenarioMock) ? (valor as EscenarioMock) : "activo";
}

function estadoMock(escenario: EscenarioMock): EstadoCuestionario {
    return escenario === "activo" || escenario === "vencido-activo" ? "ACTIVO" : "EN_PROCESO";
}

const esperar = (ms = 250) => new Promise((r) => setTimeout(r, ms));

// ---------------------------------------------------------------- API del servicio

// Datos del cuestionario del candidato logueado. El estado e id salen del login (CandidatoCuestionarioDTO).
// TODO back: las instrucciones no existen todavía en el back.
export async function obtenerCuestionarioActual(): Promise<CuestionarioActual> {
    await esperar();
    const escenario = escenarioMock();
    const candidato = obtenerCandidato();
    // Sin login en el front: el token se carga a mano desde Postman y trae idCuestionario en los claims
    const claims = obtenerClaimsToken();
    const idDelToken = typeof claims?.idCuestionario === "number" ? claims.idCuestionario : null;

    return {
        idCuestionario: candidato?.idCuestionario ?? idDelToken ?? 1,
        nombreCandidato: candidato
            ? `${candidato.nombre} ${candidato.apellido}`
            : typeof claims?.sub === "string" ? claims.sub : "Candidato de prueba",
        estado: estadoMock(escenario),
        instrucciones: INSTRUCCIONES_MOCK,
    };
}

// GestorCuestionario.iniciarCuestionario: verifica plazos (1.A / 1.B), arma el cuestionario
// si estaba ACTIVO (paso 3) y devuelve el bloque desde el que hay que seguir (1.C).
// Ojo al conectar: el back devuelve 0 cuando arranca desde el principio, y los bloques empiezan en 1.
export async function iniciarCuestionario(idCuestionario: number): Promise<number> {
    void idCuestionario;
    await esperar();

    switch (escenarioMock()) {
        case "vencido-activo":
            throw new CuestionarioCerradoError("SIN_CONTESTAR");
        case "vencido-tiempo":
            throw new CuestionarioCerradoError("INCOMPLETO");
        case "accesos":
            throw new CuestionarioCerradoError("ACCESOS_SUPERADOS");
        case "en-proceso":
            // Simula que el bloque 1 ya se había respondido en un ingreso anterior
            bloquesGuardadosMock.add(1);
            break;
    }

    const pendiente = BLOQUES_MOCK.findIndex((_, i) => !bloquesGuardadosMock.has(i + 1));
    return pendiente === -1 ? BLOQUES_MOCK.length : pendiente + 1;
}

// GestorCuestionario.recuperarBloque. TODO back: el back no devuelve el total de bloques,
// hace falta para saber si mostrar "Siguiente" o "Terminar".
export async function recuperarBloque(
    idCuestionario: number,
    nroBloque: number
): Promise<BloqueCuestionario> {
    void idCuestionario;
    await esperar();

    const preguntas = BLOQUES_MOCK[nroBloque - 1];
    if (!preguntas) throw new Error(`No existe el bloque ${nroBloque}.`);

    return { nroBloque, totalBloques: BLOQUES_MOCK.length, preguntas };
}

// GestorCuestionario.guardarBloque (paso 7). Pendiente de implementar en el back.
export async function guardarBloque(
    idCuestionario: number,
    nroBloque: number,
    respuestas: RespuestaCandidatoDTO[]
): Promise<void> {
    void idCuestionario;
    void respuestas;
    await esperar();
    bloquesGuardadosMock.add(nroBloque);
}

// GestorCuestionario.finalizarCuestionario (paso 8). Pendiente de implementar en el back.
export async function finalizarCuestionario(idCuestionario: number): Promise<void> {
    void idCuestionario;
    await esperar();
    bloquesGuardadosMock.clear();
}
