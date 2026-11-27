import { apiFetch } from "@/lib/apiClient";
import { EvaluacionDTO } from "@/types/evaluacion";
import { CandidatoOrdenMeritoDTO } from "@/types/ordenMerito";
import { PuestoDTO } from "@/types/puesto";

export interface FiltrosPuesto {
    codigo: string;
    nombre: string;
    empresa: string;
}

export async function buscarPuestos(filtros: FiltrosPuesto): Promise<PuestoDTO[]> {
    const params = new URLSearchParams();

    if (filtros.codigo.trim()) params.set("codigo", filtros.codigo.trim());
    if (filtros.nombre.trim()) params.set("nombre", filtros.nombre.trim());
    if (filtros.empresa.trim()) params.set("empresa", filtros.empresa.trim());

    const query = params.toString();
    return apiFetch<PuestoDTO[]>(`/reportes/puestos${query ? `?${query}` : ""}`);
}

export async function listarEvaluaciones(idPuesto: number): Promise<EvaluacionDTO[]> {
    return apiFetch<EvaluacionDTO[]>(`/reportes/puestos/${idPuesto}/evaluaciones`);
}

export async function generarOrdenMerito(
    idPuesto: number,
    evaluacionIds?: number[]
): Promise<CandidatoOrdenMeritoDTO[]> {
    const params = new URLSearchParams();
    evaluacionIds?.forEach((id) => params.append("evaluacionIds", String(id)));

    const query = params.toString();
    return apiFetch<CandidatoOrdenMeritoDTO[]>(
        `/reportes/puestos/${idPuesto}/orden-merito${query ? `?${query}` : ""}`
    );
}
