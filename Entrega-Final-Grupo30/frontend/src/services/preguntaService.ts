import { ApiError, apiFetch } from "@/lib/apiClient";
import { PreguntaDTO } from "@/types/pregunta";

export interface FiltrosPregunta {
    competencia: string;
    factor: string;
    pregunta: string;
}

export interface OpcionPreguntaInput {
    orden: number;
    ponderacion: number;
}

export interface NuevaPreguntaRequest {
    codigoCompetencia: string;
    codigoFactor: string;
    nombre: string;
    textoPregunta: string;
    descripcion: string | null;
    idOpcionRespuesta: number;
    opcionesPregunta: OpcionPreguntaInput[];
}

export interface ModificarPreguntaRequest {
    nombre: string;
    textoPregunta: string;
    descripcion: string | null;
    idOpcionRespuesta: number;
    opcionesPregunta: OpcionPreguntaInput[];
}

export class PreguntaDuplicadaError extends Error {
    constructor(public campo: "nombre" | "texto") {
        super("La pregunta ya existe");
    }
}

export function buscarPreguntas(f: FiltrosPregunta): Promise<PreguntaDTO[]> {
    const params = new URLSearchParams();
    if (f.competencia.trim()) params.set("competencia", f.competencia.trim());
    if (f.factor.trim()) params.set("factor", f.factor.trim());
    if (f.pregunta.trim()) params.set("nombre", f.pregunta.trim());

    const query = params.toString();
    return apiFetch<PreguntaDTO[]>(`/preguntas${query ? `?${query}` : ""}`);
}

export async function crearPregunta(req: NuevaPreguntaRequest): Promise<void> {
    try {
        await apiFetch<void>("/preguntas", {
            method: "POST",
            body: JSON.stringify(req),
        });
    } catch (err) {
        if (err instanceof ApiError && err.status === 409) {
            const campo = err.body?.campo === "texto" ? "texto" : "nombre";
            throw new PreguntaDuplicadaError(campo);
        }
        throw err;
    }
}

export async function modificarPregunta(id: number, req: ModificarPreguntaRequest): Promise<void> {
    try {
        await apiFetch<void>(`/preguntas/${id}`, {
            method: "PUT",
            body: JSON.stringify(req),
        });
    } catch (err) {
        if (err instanceof ApiError && err.status === 409) {
            const campo = err.body?.campo === "texto" ? "texto" : "nombre";
            throw new PreguntaDuplicadaError(campo);
        }
        throw err;
    }
}

export function eliminarPregunta(id: number): Promise<void> {
    return apiFetch<void>(`/preguntas/${id}`, {
        method: "DELETE",
    });
}
