import { obtenerToken, borrarToken } from "./session";
import { ErrorResponseDTO } from "@/types/error";

// const API_URL = process.env.NEXT_PUBLIC_API_URL; por defecto es el puerto 3000
const API_URL = process.env.NEXT_PUBLIC_API_URL ?? "http://localhost:8080/api";

export class ApiError extends Error {
    constructor(
        public status: number,
        public body: ErrorResponseDTO | null
    ) {
        // .message queda con el texto que mandó el back
        super(body?.mensaje ?? `Error ${status}`);
    }

    // Todos los errores por campo, o {} si no hay
    get errores(): Record<string, string> {
        return this.body?.errores ?? {};
    }
}

export async function apiFetch<T>(path: string, options: RequestInit = {}): Promise<T> {
    const token = obtenerToken();

    const res = await fetch(`${API_URL}${path}`, {
        ...options,
        headers: {
            "Content-Type": "application/json",
            ...(token ? { Authorization: `Bearer ${token}` } : {}),
            ...options.headers,
        },
    });

    if (!res.ok) {
        let body: ErrorResponseDTO | null = null;
        try { body = await res.json(); } catch {}
        if (res.status === 401 && token) {
            borrarToken();
            window.location.href = "/login";
        }
        throw new ApiError(res.status, body);
    }

    const body = await res.text();
    return body ? JSON.parse(body) as T : undefined as T;
}
