package com.grupo30.capitalhumano.evaluaciones.service;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.grupo30.capitalhumano.evaluaciones.dao.IBloquePreguntasDAO;
import com.grupo30.capitalhumano.evaluaciones.dao.IConfiguracionSistemaDAO;
import com.grupo30.capitalhumano.evaluaciones.dao.ICuestionarioDAO;
import com.grupo30.capitalhumano.evaluaciones.dao.IPreguntaCuestionarioDAO;
import com.grupo30.capitalhumano.evaluaciones.dto.PreguntaExamenDTO;
import com.grupo30.capitalhumano.evaluaciones.dto.RespuestaCandidatoDTO;
import com.grupo30.capitalhumano.evaluaciones.entity.BloquePreguntas;
import com.grupo30.capitalhumano.evaluaciones.entity.CaracteristicaPuesto;
import com.grupo30.capitalhumano.evaluaciones.entity.Competencia;
import com.grupo30.capitalhumano.evaluaciones.entity.Cuestionario;
import com.grupo30.capitalhumano.evaluaciones.entity.EstadoCuestionario;
import com.grupo30.capitalhumano.evaluaciones.entity.Pregunta;
import com.grupo30.capitalhumano.evaluaciones.entity.PreguntaCuestionario;
import com.grupo30.capitalhumano.evaluaciones.entity.Puesto;
import com.grupo30.capitalhumano.evaluaciones.exception.PlazoInicioVencidoException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GestorCuestionario {
    private final ICuestionarioDAO cuestionarioDAO;
    private final IBloquePreguntasDAO bloquePreguntasDAO;
    private final IPreguntaCuestionarioDAO preguntaCuestionarioDAO;
    private final IConfiguracionSistemaDAO configuracionSistemaDAO;

    @Transactional(noRollbackFor = PlazoInicioVencidoException.class)
    public void iniciarCuestionario(int cuestionarioId) {
        Cuestionario cuestionario = cuestionarioDAO.findById(cuestionarioId)
        .orElseThrow(() -> new IllegalArgumentException(
                "No existe cuestionario con ID " + cuestionarioId
        ));
        
        // El cuestionario se encuentra en el estado activo
        switch(cuestionario.getEstado()){
            case EstadoCuestionario.COMPLETO -> {
                throw new IllegalStateException("El cuestionario ya fue completado");
            }
            case EstadoCuestionario.INCOMPLETO -> {
                throw new IllegalStateException("El cuestionario quedó incompleto y ya no se puede continuar");
            }
            case EstadoCuestionario.SIN_COMPLETAR -> {
                throw new PlazoInicioVencidoException();
            }
            case EN_PROCESO -> {
                verificarTiempoParaCompletar(cuestionario);

                // Ya tiene sus preguntas asignadas. El flujo retoma el bloque pendiente.

                // 1.C El actor ya había ingresado a completar el cuestionario previamente

                /*
                    1.A.1 El sistema recupera de la base de datos el cuestionario que está en proceso
                    de llenado por parte del usuario y se posiciona en el bloque que aún no ha sido respondido.
                    1.A.2 El CU continúa por el paso 5.
                */
            }
            case ACTIVO -> {
                verificarPlazoParaIniciar(cuestionario);
                prepararCuestionario(cuestionario);
            }
        }

    }

    private void verificarPlazoParaIniciar(Cuestionario cuestionario) {
        String valor = configuracionSistemaDAO.obtener("DIAS_MAXIMO_CUESTIONARIO_ACTIVO")
            .orElseThrow(() -> new IllegalStateException(
                "Falta configurar DIAS_MAXIMO_CUESTIONARIO_ACTIVO"
            ))
            .getValor();

        int diasMaximos;
        try {
            diasMaximos = Integer.parseInt(valor.strip());
        } catch (NumberFormatException ex) {
            throw new IllegalStateException("El plazo para iniciar debe ser un número entero de días", ex);
        }
        if (diasMaximos <= 0) {
            throw new IllegalStateException("El plazo para iniciar debe ser mayor a cero días");
        }

        // CU026 1.B. El plazo se cuenta desde el día de creación de la evaluación.
        LocalDateTime vencimiento = cuestionario.getEvaluacion().getFechaCreacion()
            .plusDays(diasMaximos)
            .atStartOfDay();
        LocalDateTime ahora = LocalDateTime.now();
        if (!ahora.isBefore(vencimiento)) {
            cuestionario.setEstado(EstadoCuestionario.SIN_COMPLETAR);
            cuestionario.setFechaHoraFin(ahora);
            cuestionarioDAO.actualizar(cuestionario);
            throw new PlazoInicioVencidoException();
        }
    }

    private void verificarTiempoParaCompletar(Cuestionario cuestionario) {
        // 1.A El usuario superó el tiempo máximo para completarel cuestionario (2)
        Duration transcurrido = Duration.between(
            cuestionario.getFechaHoraInicio(),
            LocalDateTime.now()
        );

        boolean vencido = transcurrido.compareTo(Duration.ofMinutes(cuestionario.getTiempoMaximo())) >= 0;

        if(vencido){
            cuestionario.setEstado(EstadoCuestionario.INCOMPLETO);
            cuestionario.setFechaHoraFin(LocalDateTime.now());
            // Avisar situación
            //FINALIZAR CU
        }
    }

    private void prepararCuestionario(Cuestionario cuestionario) {
        /*
        El sistema recupera el cuestionario y recupera las competencias que serán
        evaluadas. Por cada competencia recupera los factores asociados a las
        mismas y por cada factor (que disponga de al menos 2 preguntas),
        recupera las preguntas, con sus respectivas opciones de respuestas.
        */

        Puesto puesto = cuestionario.getEvaluacion().getPuesto();
        List<Competencia> competencias = new ArrayList<>();

        for (CaracteristicaPuesto caracteristica : puesto.getCaracteristicas()) {
            competencias.add(caracteristica.getCompetencia());
        }

        List<Pregunta> preguntas = seleccionarPreguntasAleatorias(competencias);
        
        int preguntasPorBloque = 2; // Recuperar de la BDD
        int nroBloque = 1;

        for (int inicio = 0; inicio < preguntas.size(); inicio += preguntasPorBloque) {
            int fin = Math.min(inicio + preguntasPorBloque, preguntas.size());

            BloquePreguntas bloque = BloquePreguntas.builder()
                .cuestionario(cuestionario)
                .nroBloque(nroBloque)
                .build();

            bloquePreguntasDAO.guardar(bloque);

            for (int i = inicio; i < fin; i++) {
                PreguntaCuestionario preguntaCuestionario = PreguntaCuestionario.builder()
                    .bloque(bloque)
                    .pregunta(preguntas.get(i))
                    .nroOrden(i + 1)
                    .build();

                preguntaCuestionarioDAO.guardar(preguntaCuestionario);
            }

            nroBloque++;
        }  
        cuestionario.setFechaHoraInicio(LocalDateTime.now());
        cuestionario.setEstado(EstadoCuestionario.EN_PROCESO);

        cuestionarioDAO.guardar(cuestionario);
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
