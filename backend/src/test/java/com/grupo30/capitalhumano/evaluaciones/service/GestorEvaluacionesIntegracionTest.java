package com.grupo30.capitalhumano.evaluaciones.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.transaction.annotation.Transactional;

import com.grupo30.capitalhumano.evaluaciones.dao.ICuestionarioDAO;
import com.grupo30.capitalhumano.evaluaciones.dao.IPuestoDAO;
import com.grupo30.capitalhumano.evaluaciones.dto.CandidatoGestionDTO;
import com.grupo30.capitalhumano.evaluaciones.entity.Candidato;
import com.grupo30.capitalhumano.evaluaciones.entity.EstadoCuestionario;

import jakarta.persistence.EntityManager;

@SpringBootTest
@Transactional
@WithMockUser(username = "ADMIN", roles = "CONSULTOR")
class GestorEvaluacionesIntegracionTest {

    @Autowired private GestorEvaluaciones gestorEvaluaciones;
    @Autowired private ICuestionarioDAO cuestionarios;
    @Autowired private IPuestoDAO puestos;
    @Autowired private EntityManager entityManager;

    @Test
    void deberiaGenerarEvaluacion() {
        Candidato candidato = DatosEvaluacionPrueba.crearCandidato(entityManager, 990001);
        int puestoId = DatosEvaluacionPrueba.buscarPuestoEvaluable(puestos).getPuestoId();

        List<CandidatoGestionDTO> seleccion = gestorEvaluaciones.buscarCandidatos(
                "ALVAREZ", null, candidato.getNroCandidato());
        assertThat(seleccion).extracting(CandidatoGestionDTO::getNroCandidato)
                .containsExactly(candidato.getNroCandidato());

        gestorEvaluaciones.generarEvaluacion(puestoId, seleccion);
        entityManager.flush();
        entityManager.clear();

        assertThat(cuestionarios.buscarActivoPorCandidato(candidato.getNroCandidato()))
                .hasValueSatisfying(cuestionario -> {
                    assertThat(cuestionario.getEstado()).isEqualTo(EstadoCuestionario.ACTIVO);
                    assertThat(cuestionario.getEvaluacion().getPuesto().getPuestoId()).isEqualTo(puestoId);
                    assertThat(cuestionario.getClave()).hasSize(8);
                });
    }

    @Test
    void cu025_unCuestionarioEnProcesoImpideOtraEvaluacionParaElCandidato() {
        Candidato candidato = DatosEvaluacionPrueba.crearCandidato(entityManager, 990001);
        int puestoId = DatosEvaluacionPrueba.buscarPuestoEvaluable(puestos).getPuestoId();
        CandidatoGestionDTO seleccionado = CandidatoGestionDTO.builder().nroCandidato(candidato.getNroCandidato())
                .apellido(candidato.getApellido()).nombre(candidato.getNombre()).build();
        gestorEvaluaciones.generarEvaluacion(puestoId, List.of(seleccionado));
        var vigente = cuestionarios.buscarActivoPorCandidato(candidato.getNroCandidato()).orElseThrow();
        vigente.setEstado(EstadoCuestionario.EN_PROCESO);
        entityManager.flush();
        entityManager.clear();

        assertThatThrownBy(() -> gestorEvaluaciones.prepararEvaluacion(puestoId, List.of(seleccionado)))
                .isInstanceOf(IllegalArgumentException.class).hasMessageContaining("ALVAREZ, ANA");
    }

    @ParameterizedTest
    @EnumSource(value = EstadoCuestionario.class, names = {"COMPLETO", "INCOMPLETO", "SIN_CONTESTAR"})
    void cu025_unCuestionarioCerradoPermiteCrearOtraEvaluacion(EstadoCuestionario estado) {
        Candidato candidato = DatosEvaluacionPrueba.crearCandidato(entityManager, 990001);
        int puestoId = DatosEvaluacionPrueba.buscarPuestoEvaluable(puestos).getPuestoId();
        CandidatoGestionDTO seleccionado = CandidatoGestionDTO.builder().nroCandidato(candidato.getNroCandidato())
                .build();
        gestorEvaluaciones.generarEvaluacion(puestoId, List.of(seleccionado));
        var anterior = cuestionarios.buscarActivoPorCandidato(candidato.getNroCandidato()).orElseThrow();
        anterior.setEstado(estado);
        entityManager.flush();
        entityManager.clear();

        gestorEvaluaciones.generarEvaluacion(puestoId, List.of(seleccionado));

        assertThat(cuestionarios.buscarActivoPorCandidato(candidato.getNroCandidato()))
                .hasValueSatisfying(nuevo -> {
                    assertThat(nuevo.getEstado()).isEqualTo(EstadoCuestionario.ACTIVO);
                    assertThat(nuevo.getIdCuestionario()).isNotEqualTo(anterior.getIdCuestionario());
                });
    }

    @Test
    void deberiaFallarSiNoHayCandidatos() {
        int puestoId = DatosEvaluacionPrueba.buscarPuestoEvaluable(puestos).getPuestoId();
        assertThatThrownBy(() -> gestorEvaluaciones.generarEvaluacion(puestoId, List.of()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("al menos un candidato");
    }

    @Test
    void deberiaFallarSiNoHayPuesto() {
        assertThatThrownBy(() -> gestorEvaluaciones.generarEvaluacion(null, List.of()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("puesto");
    }
}
