import { apiFetch } from "@/lib/apiClient";
import { CompetenciaDTO } from "@/types/competencia";
import { FactorDTO } from "@/types/factor";
import { OpcionRespuestaDTO } from "@/types/opcionRespuesta";

export function listarCompetenciasCatalogo(): Promise<CompetenciaDTO[]> {
    return apiFetch<CompetenciaDTO[]>("/competencias");
}

// CU021: al elegir una competencia se cargan sus factores (ordenados por nroOrden en el backend)
export function listarFactoresDeCompetencia(codigoCompetencia: string): Promise<FactorDTO[]> {
    return apiFetch<FactorDTO[]>(
        `/competencias/${encodeURIComponent(codigoCompetencia)}/factores`
    );
}

export function listarFactoresCatalogo(): Promise<FactorDTO[]> {
    return apiFetch<FactorDTO[]>("/factores");
}

export function listarOpcionesRespuesta(): Promise<OpcionRespuestaDTO[]> {
    return apiFetch<OpcionRespuestaDTO[]>("/opciones-respuesta");
}
