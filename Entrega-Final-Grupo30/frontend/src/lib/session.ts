import { CandidatoCuestionarioDTO } from "@/types/candidatoCuestionario";

const CLAVE = "token";
const CLAVE_CANDIDATO = "candidato";

export function guardarToken(token: string) {
    try { sessionStorage.setItem(CLAVE, token); } catch {}
}

export function obtenerToken(): string | null {
    try { return sessionStorage.getItem(CLAVE); } catch { return null; }
}

export function borrarToken() {
    try {
        sessionStorage.removeItem(CLAVE);
        sessionStorage.removeItem(CLAVE_CANDIDATO);
    } catch {}
}

// Claims del JWT (sin verificar la firma, eso lo hace el back).
// Sirve cuando el token se cargó a mano desde Postman y no pasó por loginCandidato.
export function obtenerClaimsToken(): Record<string, unknown> | null {
    const token = obtenerToken();
    const payload = token?.split(".")[1];
    if (!payload) return null;
    try {
        const base64 = payload.replace(/-/g, "+").replace(/_/g, "/");
        return JSON.parse(atob(base64.padEnd(Math.ceil(base64.length / 4) * 4, "=")));
    } catch {
        return null;
    }
}

// Datos del candidato logueado (idCuestionario, estado, nombre) para el CU026
export function guardarCandidato(candidato: CandidatoCuestionarioDTO) {
    try { sessionStorage.setItem(CLAVE_CANDIDATO, JSON.stringify(candidato)); } catch {}
}

export function obtenerCandidato(): CandidatoCuestionarioDTO | null {
    try {
        const valor = sessionStorage.getItem(CLAVE_CANDIDATO);
        return valor ? (JSON.parse(valor) as CandidatoCuestionarioDTO) : null;
    } catch {
        return null;
    }
}
