const CLAVE = "token";

export function guardarToken(token: string) {
    try { sessionStorage.setItem(CLAVE, token); } catch {}
}

export function obtenerToken(): string | null {
    try { return sessionStorage.getItem(CLAVE); } catch { return null; }
}

export function borrarToken() {
    try { sessionStorage.removeItem(CLAVE); } catch {}
}