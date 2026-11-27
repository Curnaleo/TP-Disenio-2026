package com.grupo30.capitalhumano.evaluaciones.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;

import org.junit.jupiter.api.Test;
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
