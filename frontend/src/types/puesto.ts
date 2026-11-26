export interface PuestoDTO {
    puestoId: number;
    codigo: string;
    nombre: string;
    descripcion: string | null;
    empresa: string;
    activo: boolean | null;
    cantidadCandidatos: number | null;
    evaluacionesCompletas: number | null;
}
