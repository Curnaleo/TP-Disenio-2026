import { CandidatoCuestionarioDTO } from "./candidatoCuestionario";

export type TipoDocumento = "DNI" | "LE" | "LC" | "PP";

export interface UsuarioDTO {
    idUsuario: number;
    nombre: string;
    activo: boolean | null;
}

export interface LoginConsultorRequest {
    usuario: string;
    contrasena: string;
}

export interface LoginCandidatoRequest {
    tipoDocumento: TipoDocumento;
    nroDocumento: string;
    clave: string;
}

export interface AuthResponseDTO {
    token: string;
    tipoToken: string;
    rol: "CONSULTOR" | "CANDIDATO";
    consultor: UsuarioDTO | null;
    candidato: CandidatoCuestionarioDTO | null;
}