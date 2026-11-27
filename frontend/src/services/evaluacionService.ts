import { apiFetch } from "@/lib/apiClient";

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

export function confirmarEvaluacion(idPuesto: number, idVista: string): Promise<void> {
    return apiFetch(`/evaluacion/${idPuesto}/confirmar/${idVista}`, { method: "POST" });
}
