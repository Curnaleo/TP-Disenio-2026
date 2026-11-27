import { TipoDocumento } from "./auth";
import { EstadoCuestionario } from "./estadoCuestionario";

export interface CandidatoOrdenMeritoDTO {
    tipoDocumento: TipoDocumento;
    nroDocumento: string;
    apellido: string;
    nombre: string;
    puntajeObtenido: number | null;
    estadoCuestionario: EstadoCuestionario;
    cumplePonderacionMinima: boolean | null;
    fechaInicio: string | null;
    fechaFin: string | null;
    fechaHoraUltimoIngreso: string | null;
    cantidadAccesos: number | null;
}
