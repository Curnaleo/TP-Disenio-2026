export interface FactorDTO{
    id: number;
    codigo: string;
    nombre: string;
    descripcion: string | null;
    nroOrden: number;
    activo: boolean | null;
    nombreCompetencia: string;
    idCompetencia: number;

}