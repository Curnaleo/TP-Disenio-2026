export interface ErrorResponseDTO {
    status: number;
    error: string;
    mensaje: string;
    errores?: Record<string, string>; // solo viene en errores de validación (400)
}