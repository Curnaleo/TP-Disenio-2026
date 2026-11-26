// Cuando haya API, cambiar el interior de cada función por apiFetch y las pantallas no se tocan.

import { EvaluacionDTO } from "@/types/evaluacion";
import { CandidatoOrdenMeritoDTO } from "@/types/ordenMerito";
import { PuestoDTO } from "@/types/puesto";

export interface FiltrosPuesto {
    codigo: string;
    nombre: string;
    empresa: string;
}

const PUESTOS_MOCK: PuestoDTO[] = [
    {
        puestoId: 1,
        codigo: "DEV-SR",
        nombre: "Desarrollador Backend Senior",
        descripcion: "Desarrollo de microservicios en Java y Spring Boot",
        empresa: "TechCorp S.A.",
        activo: true,
        cantidadCandidatos: 4,
        evaluacionesCompletas: 3,
    },
    {
        puestoId: 2,
        codigo: "QA-SSR",
        nombre: "Analista QA Semi Senior",
        descripcion: "Automatización de pruebas funcionales y de integración",
        empresa: "SoftSolutions",
        activo: true,
        cantidadCandidatos: 2,
        evaluacionesCompletas: 1,
    },
    {
        puestoId: 3,
        codigo: "PM-JR",
        nombre: "Project Manager Junior",
        descripcion: "Coordinación de equipos ágiles",
        empresa: "TechCorp S.A.",
        activo: true,
        cantidadCandidatos: 0,
        evaluacionesCompletas: 0,
    },
];

const EVALUACIONES_MOCK: Record<number, EvaluacionDTO[]> = {
    1: [
        {
            idEvaluacion: 10,
            nombreEvaluacion: "Evaluación Técnica Q3",
            nombrePuesto: "Desarrollador Backend Senior",
            fechaCreacion: "2026-10-15",
            cantidadCandidatos: 3,
        },
        {
            idEvaluacion: 11,
            nombreEvaluacion: "Evaluación Liderazgo",
            nombrePuesto: "Desarrollador Backend Senior",
            fechaCreacion: "2026-11-02",
            cantidadCandidatos: 2,
        },
    ],
    2: [
        {
            idEvaluacion: 20,
            nombreEvaluacion: "Evaluación QA 2026",
            nombrePuesto: "Analista QA Semi Senior",
            fechaCreacion: "2026-11-10",
            cantidadCandidatos: 2,
        },
    ],
    3: [],
};

interface RegistroMeritoMock {
    idEvaluacion: number;
    candidato: CandidatoOrdenMeritoDTO;
}

const ORDEN_MERITO_MOCK: Record<number, RegistroMeritoMock[]> = {
    1: [
        {
            idEvaluacion: 10,
            candidato: {
                tipoDocumento: "DNI",
                nroDocumento: "38451290",
                apellido: "Gómez",
                nombre: "Lucía",
                puntajeObtenido: 92.5,
                estadoCuestionario: "COMPLETO",
                cumplePonderacionMinima: true,
                fechaInicio: "2026-10-16T09:00:00",
                fechaFin: "2026-10-16T09:42:00",
                fechaHoraUltimoIngreso: "2026-10-16T09:42:00",
                cantidadAccesos: 1,
            },
        },
        {
            idEvaluacion: 10,
            candidato: {
                tipoDocumento: "DNI",
                nroDocumento: "40112345",
                apellido: "Pérez",
                nombre: "Martín",
                puntajeObtenido: 78.0,
                estadoCuestionario: "COMPLETO",
                cumplePonderacionMinima: true,
                fechaInicio: "2026-10-17T14:10:00",
                fechaFin: "2026-10-17T15:05:00",
                fechaHoraUltimoIngreso: "2026-10-17T15:05:00",
                cantidadAccesos: 2,
            },
        },
        {
            idEvaluacion: 11,
            candidato: {
                tipoDocumento: "DNI",
                nroDocumento: "35987654",
                apellido: "Fernández",
                nombre: "Sofía",
                puntajeObtenido: 54.0,
                estadoCuestionario: "COMPLETO",
                cumplePonderacionMinima: false,
                fechaInicio: "2026-11-03T11:00:00",
                fechaFin: "2026-11-03T11:35:00",
                fechaHoraUltimoIngreso: "2026-11-03T11:35:00",
                cantidadAccesos: 1,
            },
        },
        {
            idEvaluacion: 11,
            candidato: {
                tipoDocumento: "LE",
                nroDocumento: "29456123",
                apellido: "Rodríguez",
                nombre: "Carlos",
                puntajeObtenido: null,
                estadoCuestionario: "EN_PROCESO",
                cumplePonderacionMinima: null,
                fechaInicio: "2026-11-04T16:20:00",
                fechaFin: null,
                fechaHoraUltimoIngreso: "2026-11-04T16:30:00",
                cantidadAccesos: 3,
            },
        },
    ],
    2: [
        {
            idEvaluacion: 20,
            candidato: {
                tipoDocumento: "DNI",
                nroDocumento: "41222333",
                apellido: "Sánchez",
                nombre: "Valentina",
                puntajeObtenido: 85.0,
                estadoCuestionario: "COMPLETO",
                cumplePonderacionMinima: true,
                fechaInicio: "2026-11-11T10:00:00",
                fechaFin: "2026-11-11T10:40:00",
                fechaHoraUltimoIngreso: "2026-11-11T10:40:00",
                cantidadAccesos: 1,
            },
        },
        {
            idEvaluacion: 20,
            candidato: {
                tipoDocumento: "DNI",
                nroDocumento: "39888777",
                apellido: "López",
                nombre: "Diego",
                puntajeObtenido: null,
                estadoCuestionario: "SIN_COMPLETAR",
                cumplePonderacionMinima: null,
                fechaInicio: null,
                fechaFin: null,
                fechaHoraUltimoIngreso: null,
                cantidadAccesos: 0,
            },
        },
    ],
    3: [],
};

const contiene = (valor: string, filtro: string) =>
    valor.toLowerCase().includes(filtro.trim().toLowerCase());

export async function buscarPuestos(f: FiltrosPuesto): Promise<PuestoDTO[]> {
    return PUESTOS_MOCK.filter(
        (p) =>
            p.activo !== false &&
            contiene(p.codigo, f.codigo) &&
            contiene(p.nombre, f.nombre) &&
            contiene(p.empresa, f.empresa)
    );
}

export async function listarEvaluaciones(idPuesto: number): Promise<EvaluacionDTO[]> {
    return EVALUACIONES_MOCK[idPuesto] ?? [];
}

export async function generarOrdenMerito(
    idPuesto: number,
    evaluacionIds?: number[]
): Promise<CandidatoOrdenMeritoDTO[]> {
    const registros = ORDEN_MERITO_MOCK[idPuesto] ?? [];
    const filtrados =
        evaluacionIds && evaluacionIds.length > 0
            ? registros.filter((r) => evaluacionIds.includes(r.idEvaluacion))
            : registros;

    return [...filtrados]
        .map((r) => r.candidato)
        .sort((a, b) => {
            if (a.puntajeObtenido === null && b.puntajeObtenido === null) return 0;
            if (a.puntajeObtenido === null) return 1;
            if (b.puntajeObtenido === null) return -1;
            return b.puntajeObtenido - a.puntajeObtenido;
        });
}
