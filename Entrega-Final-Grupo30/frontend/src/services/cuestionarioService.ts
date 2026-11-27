import { ApiError, apiFetch } from "@/lib/apiClient";
import type { BloqueCuestionario, CuestionarioActual, MotivoCierre, RespuestaCandidatoDTO } from "@/types/cuestionario";
import type { EstadoCuestionario } from "@/types/estadoCuestionario";

export class CuestionarioCerradoError extends Error {
    constructor(public motivo: MotivoCierre) {
        super(`Cuestionario cerrado: ${motivo}`);
    }
}

async function solicitar<T>(ruta: string, opciones?: RequestInit): Promise<T> {
    try {
        return await apiFetch<T>(`/cuestionario${ruta}`, opciones);
    } catch (error) {
        const motivo = error instanceof ApiError ? error.body?.motivo : undefined;
        if (motivo === "SIN_CONTESTAR" || motivo === "INCOMPLETO" || motivo === "ACCESOS_SUPERADOS") {
            throw new CuestionarioCerradoError(motivo);
        }
        throw error;
    }
}

export function obtenerCuestionarioActual(): Promise<CuestionarioActual> {
    return solicitar("/actual");
}

export async function iniciarCuestionario(): Promise<number> {
    const { nroBloque } = await solicitar<{ nroBloque: number }>("/iniciar", { method: "POST" });
    return nroBloque;
}

export function recuperarBloque(nroBloque: number): Promise<BloqueCuestionario> {
    return solicitar(`/bloques/${nroBloque}`);
}

export function guardarBloque(
    nroBloque: number,
    respuestas: RespuestaCandidatoDTO[],
): Promise<{ estado: EstadoCuestionario; siguienteBloque?: number }> {
    return solicitar(`/bloques/${nroBloque}`, { method: "PUT", body: JSON.stringify(respuestas) });
}
