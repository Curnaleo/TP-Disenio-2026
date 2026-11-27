import { apiFetch, apiFetchRespuesta } from "@/lib/apiClient";

export interface CandidatoGestionDTO {
    nroCandidato: number;
    apellido: string;
    nombre: string;
    tipoDocumento: string;
    nroDocumento: string;
}

export interface VistaPrevia {
    id: string;
    candidatos: { candidato: CandidatoGestionDTO; clave: string }[];
}

export function buscarCandidatos(filtros: URLSearchParams): Promise<CandidatoGestionDTO[]> {
    return apiFetch(`/evaluacion/candidatos?${filtros}`);
}

export function validarCandidatos(candidatos: CandidatoGestionDTO[]): Promise<CandidatoGestionDTO[]> {
    return apiFetch("/evaluacion/candidatos/validar", {
        method: "POST",
        body: JSON.stringify(candidatos),
    });
}

export function validarPuesto(idPuesto: number): Promise<void> {
    return apiFetch(`/evaluacion/puestos/${idPuesto}/validar`);
}

export function prepararEvaluacion(
    idPuesto: number,
    candidatos: CandidatoGestionDTO[],
): Promise<VistaPrevia> {
    return apiFetch(`/evaluacion/${idPuesto}/vista-previa`, {
        method: "POST",
        body: JSON.stringify(candidatos),
    });
}

export function confirmarEvaluacion(idPuesto: number, idVista: string): Promise<{ idEvaluacion: number }> {
    return apiFetch(`/evaluacion/${idPuesto}/confirmar/${idVista}`, { method: "POST" });
}

export async function exportarClavesExcel(idEvaluacion: number): Promise<{ archivo: Blob; nombre: string }> {
    const respuesta = await apiFetchRespuesta(`/evaluacion/${idEvaluacion}/claves`, { cache: "no-store" });
    const disposicion = respuesta.headers.get("Content-Disposition");
    const nombreCodificado = disposicion?.match(/filename\*=UTF-8''([^;]+)/i)?.[1];
    if (!nombreCodificado) throw new Error("El servidor no informó el nombre del archivo Excel.");

    return { archivo: await respuesta.blob(), nombre: decodeURIComponent(nombreCodificado) };
}
