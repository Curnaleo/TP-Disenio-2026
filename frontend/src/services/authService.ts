import { apiFetch } from "@/lib/apiClient";
import { borrarToken, guardarCandidato, guardarToken } from "@/lib/session";
import {
    AuthResponseDTO,
    LoginCandidatoRequest,
    LoginConsultorRequest,
} from "@/types/auth";

export async function loginConsultor(req: LoginConsultorRequest): Promise<AuthResponseDTO> {
    const resp = await apiFetch<AuthResponseDTO>("/auth/consultor", {
        method: "POST",
        body: JSON.stringify(req),
    });

    guardarToken(resp.token);
    return resp;
}

export async function loginCandidato(req: LoginCandidatoRequest): Promise<AuthResponseDTO> {
    const resp = await apiFetch<AuthResponseDTO>("/auth/candidato", {
        method: "POST",
        body: JSON.stringify(req),
    });

    guardarToken(resp.token);
    if (resp.candidato) guardarCandidato(resp.candidato);
    return resp;
}

export async function cerrarSesion(): Promise<void> {
    try {
        await apiFetch<void>("/auth/logout", { method: "POST" });

    } finally {
        borrarToken(); // aunque falle el back, se cierra la sesión en el front

    }
}