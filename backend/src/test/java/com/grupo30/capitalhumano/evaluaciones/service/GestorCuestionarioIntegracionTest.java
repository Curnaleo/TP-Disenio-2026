package com.grupo30.capitalhumano.evaluaciones.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.transaction.annotation.Transactional;

import com.grupo30.capitalhumano.evaluaciones.dao.IBloquePreguntasDAO;
import com.grupo30.capitalhumano.evaluaciones.dao.ICuestionarioDAO;
import com.grupo30.capitalhumano.evaluaciones.dao.IPuestoDAO;
import com.grupo30.capitalhumano.evaluaciones.dao.IResultadoCompetenciaDAO;
import com.grupo30.capitalhumano.evaluaciones.dto.CandidatoGestionDTO;
import com.grupo30.capitalhumano.evaluaciones.dto.RespuestaCandidatoDTO;
import com.grupo30.capitalhumano.evaluaciones.entity.BloquePreguntas;
import com.grupo30.capitalhumano.evaluaciones.entity.Candidato;
import com.grupo30.capitalhumano.evaluaciones.entity.Cuestionario;
import com.grupo30.capitalhumano.evaluaciones.entity.EstadoCuestionario;

import jakarta.persistence.EntityManager;

@SpringBootTest
@Transactional
@WithMockUser(username = "ADMIN", roles = "CONSULTOR")
class GestorCuestionarioIntegracionTest {

    @Autowired private GestorCuestionario gestorCuestionario;
    @Autowired private GestorEvaluaciones gestorEvaluaciones;
    @Autowired private ICuestionarioDAO cuestionarios;
    @Autowired private IBloquePreguntasDAO bloques;
    @Autowired private IResultadoCompetenciaDAO resultados;
    @Autowired private IPuestoDAO puestos;
    @Autowired private EntityManager entityManager;

    private int cuestionarioId;

    @BeforeEach
    void prepararCuestionarioActivo() {
        Candidato candidato = DatosEvaluacionPrueba.crearCandidato(entityManager, 990002);
        int puestoId = DatosEvaluacionPrueba.buscarPuestoEvaluable(puestos).getPuestoId();
        gestorEvaluaciones.generarEvaluacion(puestoId, List.of(
                CandidatoGestionDTO.builder().nroCandidato(candidato.getNroCandidato()).build()));
        cuestionarioId = cuestionarios.buscarActivoPorCandidato(candidato.getNroCandidato())
                .orElseThrow().getIdCuestionario();
    }

    @Test
    void deberiaFallarSiNoExisteElCuestionario() {
        assertThatThrownBy(() -> gestorCuestionario.iniciarCuestionario(Integer.MAX_VALUE))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("No existe cuestionario");
    }

    @Test
    void deberiaFallarSiNoExisteBloque() {
        assertThatThrownBy(() -> gestorCuestionario.recuperarBloque(cuestionarioId, Integer.MAX_VALUE))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("No existe ese bloque");
    }

    @Test
    void deberiaIniciarCuestionario() {
        assertThat(gestorCuestionario.iniciarCuestionario(cuestionarioId)).isEqualTo(1);
        assertThat(cuestionario().getEstado()).isEqualTo(EstadoCuestionario.EN_PROCESO);
        assertThat(cuestionario().getFechaHoraInicio()).isNotNull();
    }

    @Test
    void deberiaPermitirReingresarACuestionarioEnProceso() {
        int primerBloque = gestorCuestionario.iniciarCuestionario(cuestionarioId);
        gestorCuestionario.guardarBloque(cuestionarioId, responder(primerBloque));

        assertThat(gestorCuestionario.iniciarCuestionario(cuestionarioId)).isEqualTo(primerBloque + 1);
        assertThat(cuestionario().getCantidadAccesos()).isEqualTo(2);
    }

    @Test
    void deberiaGuardarLasRespuestasDelBloque() {
        int primerBloque = gestorCuestionario.iniciarCuestionario(cuestionarioId);
        gestorCuestionario.guardarBloque(cuestionarioId, responder(primerBloque));
        entityManager.flush();
        entityManager.clear();

        assertThat(bloques.recuperarBloquesPorCuestionario(cuestionarioId))
                .filteredOn(bloque -> bloque.getNroBloque() == primerBloque)
                .singleElement().satisfies(bloque -> assertThat(bloque.getCompletado()).isTrue());
    }

    @Test
    void deberiaFallarSiFaltanRespuestas() {
        gestorCuestionario.iniciarCuestionario(cuestionarioId);

        assertThatThrownBy(() -> gestorCuestionario.guardarBloque(cuestionarioId, List.of()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Debe responder todas las preguntas del bloque");
    }

    @Test
    void deberiaCalcularResultados() {
        completarBloques();
        entityManager.flush();
        entityManager.clear();

        assertThat(resultados.listarPorCuestionario(cuestionarioId))
                .hasSize(cuestionario().getEvaluacion().getPuesto().getCaracteristicas().size())
                .allSatisfy(resultado -> assertThat(resultado.getPuntajeObtenido()).isBetween(0.0, 10.0));
    }

    @Test
    void deberiaCompletarCuestionario() {
        completarBloques();
        entityManager.flush();
        entityManager.clear();

        assertThat(cuestionario().getEstado()).isEqualTo(EstadoCuestionario.COMPLETO);
        assertThat(cuestionario().getFechaHoraFin()).isNotNull();
    }

    @Test
    void deberiaDetectarTiempoVencido() {
        gestorCuestionario.iniciarCuestionario(cuestionarioId);
        Cuestionario cuestionario = cuestionario();
        cuestionario.setFechaHoraInicio(LocalDateTime.now().minusHours(3));
        cuestionario.setTiempoMaximo(60);
        cuestionarios.actualizar(cuestionario);

        assertThat(gestorCuestionario.verificarTiempoMaximo(cuestionarioId)).isTrue();
        assertThat(cuestionario().getEstado()).isEqualTo(EstadoCuestionario.INCOMPLETO);
    }

    @Test
    void deberiaDetectarAccesosExcedidos() {
        gestorCuestionario.iniciarCuestionario(cuestionarioId);
        Cuestionario cuestionario = cuestionario();
        cuestionario.setMaximoAccesos(cuestionario.getCantidadAccesos());
        cuestionarios.actualizar(cuestionario);

        assertThat(gestorCuestionario.verificarAccesosMaximos(cuestionarioId)).isTrue();
        assertThat(cuestionario().getEstado()).isEqualTo(EstadoCuestionario.INCOMPLETO);
    }

    private Cuestionario cuestionario() {
        return cuestionarios.findById(cuestionarioId).orElseThrow();
    }

    private List<RespuestaCandidatoDTO> responder(int nroBloque) {
        return gestorCuestionario.recuperarBloque(cuestionarioId, nroBloque).stream()
                .map(pregunta -> new RespuestaCandidatoDTO(pregunta.getIdPregCuest(),
                        pregunta.getOpciones().getFirst().getIdOpcionPregunta()))
                .toList();
    }

    private void completarBloques() {
        gestorCuestionario.iniciarCuestionario(cuestionarioId);
        List<Integer> numeros = bloques.recuperarBloquesPorCuestionario(cuestionarioId).stream()
                .map(BloquePreguntas::getNroBloque).sorted().toList();
        assertThat(numeros).isNotEmpty();
        for (int numero : numeros) {
            gestorCuestionario.guardarBloque(cuestionarioId, responder(numero));
        }
    }
}
