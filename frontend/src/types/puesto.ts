export interface CaracteristicaPuestoDTO {
    codigoCompetencia: string;
    nombreCompetencia: string;
    ponderacionMinima: number;
}

export interface PuestoDTO {
    puestoId: number;
    codigo: string;
    nombre: string;
    descripcion: string | null;
    empresa: string;
    activo: boolean | null;
    caracteristicas: CaracteristicaPuestoDTO[];
    cantidadCandidatos: number | null;
    evaluacionesCompletas: number | null;
}
