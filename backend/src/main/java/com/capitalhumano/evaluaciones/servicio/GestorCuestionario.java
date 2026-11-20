package com.capitalhumano.evaluaciones.servicio;

import java.util.List;
import com.capitalhumano.evaluaciones.dto.PreguntaExamenDTO;
import com.capitalhumano.evaluaciones.dto.RespuestaCandidatoDTO;

/** Contrato del diagrama para implementar al desarrollar el caso de uso. */
public abstract class GestorCuestionario {

    public abstract void iniciarCuestionario(int cuestionarioId);

    public abstract List<PreguntaExamenDTO> recuperarBloque(int cuestionarioId, int nroBloque);

    public abstract void guardarBloque(int cuestionarioId, List<RespuestaCandidatoDTO> respuestas);

    public abstract void calcularPuntaje(int cuestionarioId);

    public abstract void finalizarCuestionario(int cuestionarioId);

    public abstract boolean verificarTiempoMaximo(int cuestionarioId);

    public abstract boolean verificarAccesosMaximos(int cuestionarioId);
}
