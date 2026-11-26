import { CompetenciaDTO } from "@/types/competencia";
import { FactorDTO } from "@/types/factor";
import { OpcionRespuestaDTO } from "@/types/opcionRespuesta";

// Datos de prueba: se reemplazan por fetch cuando exista la API
export const COMPETENCIAS: CompetenciaDTO[] = [
    { id: 1, codigo: "LID", nombre: "Liderazgo", descripcion: null, activo: true },
    { id: 2, codigo: "GES", nombre: "Gestión", descripcion: null, activo: true },
    { id: 3, codigo: "COM", nombre: "Comunicación", descripcion: null, activo: true },
];

export const FACTORES: FactorDTO[] = [
    { id: 1, codigo: "EST", nombre: "Estratégico", descripcion: null, nroOrden: 1, activo: true, nombreCompetencia: "Liderazgo", idCompetencia: 1 },
    { id: 5, codigo: "CRE", nombre: "Credibilidad", descripcion: null, nroOrden: 2, activo: true, nombreCompetencia: "Liderazgo", idCompetencia: 1 },
    { id: 2, codigo: "ALC", nombre: "Alcance", descripcion: null, nroOrden: 1, activo: true, nombreCompetencia: "Gestión", idCompetencia: 2 },
    { id: 3, codigo: "TIE", nombre: "Tiempo", descripcion: null, nroOrden: 2, activo: true, nombreCompetencia: "Gestión", idCompetencia: 2 },
    { id: 4, codigo: "INT", nombre: "Interpersonal", descripcion: null, nroOrden: 1, activo: true, nombreCompetencia: "Comunicación", idCompetencia: 3 },
];

export const OPCIONES_RESPUESTA: OpcionRespuestaDTO[] = [
    {
        id: 1,
        nombre: "Verdadero / Falso",
        descripcion: null,
        activo: true,
        items: [
            { orden: 1, descripcion: "Verdadero" },
            { orden: 2, descripcion: "Falso" },
        ],
    },
    {
        id: 2,
        nombre: "Malo a Excelente",
        descripcion: null,
        activo: true,
        items: [
            { orden: 1, descripcion: "Malo" },
            { orden: 2, descripcion: "Regular" },
            { orden: 3, descripcion: "Bueno" },
            { orden: 4, descripcion: "Muy bueno" },
            { orden: 5, descripcion: "Excelente" },
        ],
    },
];

export async function listarCompetenciasCatalogo(): Promise<CompetenciaDTO[]> {
    return COMPETENCIAS.filter((c) => c.activo !== false);
}

// CU021: al elegir una competencia se cargan sus factores (por nroOrden)
export async function listarFactoresDeCompetencia(idCompetencia: number): Promise<FactorDTO[]> {
    return FACTORES.filter((f) => f.idCompetencia === idCompetencia && f.activo !== false).sort(
        (a, b) => a.nroOrden - b.nroOrden
    );
}

export async function listarOpcionesRespuesta(): Promise<OpcionRespuestaDTO[]> {
    return OPCIONES_RESPUESTA.filter((o) => o.activo !== false);
}