import { EstadoCuestionario } from "./estadoCuestionario";

export interface CandidatoCuestionarioDTO{
    idCuestionario: number;
    nroCandidato: number;
    apellido: string;
    nombre: string;
    estado: EstadoCuestionario;
}