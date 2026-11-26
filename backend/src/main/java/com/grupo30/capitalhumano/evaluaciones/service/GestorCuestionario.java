package com.grupo30.capitalhumano.evaluaciones.service;
import java.util.List;

import com.grupo30.capitalhumano.evaluaciones.dto.PreguntaExamenDTO;
import com.grupo30.capitalhumano.evaluaciones.dto.RespuestaCandidatoDTO;
import com.grupo30.capitalhumano.evaluaciones.entity.Competencia;
import com.grupo30.capitalhumano.evaluaciones.entity.Pregunta;


public class GestorCuestionario {

    public void iniciarCuestionario(int cuestionarioId) {
        

        
    }

    public List<PreguntaExamenDTO> recuperarBloque(int cuestionarioId, int nroBloque) {
        throw new UnsupportedOperationException("Pendiente de implementar");
    }

    public void guardarBloque(int cuestionarioId, List<RespuestaCandidatoDTO> respuestas) {
        throw new UnsupportedOperationException("Pendiente de implementar");
    }

    public void calcularPuntaje(int cuestionarioId) {
        throw new UnsupportedOperationException("Pendiente de implementar");
    }

    public void finalizarCuestionario(int cuestionarioId) {
        throw new UnsupportedOperationException("Pendiente de implementar");
    }

    public boolean verificarTiempoMaximo(int cuestionarioId) {
        throw new UnsupportedOperationException("Pendiente de implementar");
    }

    public boolean verificarAccesosMaximos(int cuestionarioId) {
        throw new UnsupportedOperationException("Pendiente de implementar");
    }

    private List<Pregunta> seleccionarPreguntasAleatorias(List<Competencia> competencias) {
        throw new UnsupportedOperationException("Pendiente de implementar");
    }
}
