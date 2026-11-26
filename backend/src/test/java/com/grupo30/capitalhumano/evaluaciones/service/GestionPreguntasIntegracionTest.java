package com.grupo30.capitalhumano.evaluaciones.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.transaction.annotation.Transactional;

import com.grupo30.capitalhumano.evaluaciones.dto.ItemOpcionRespuestaDTO;
import com.grupo30.capitalhumano.evaluaciones.dto.OpcionPreguntaDTO;
import com.grupo30.capitalhumano.evaluaciones.dto.OpcionRespuestaDTO;
import com.grupo30.capitalhumano.evaluaciones.dto.PreguntaDTO;
import com.grupo30.capitalhumano.evaluaciones.entity.RegistroAuditoria;

@SpringBootTest
@Transactional
@WithMockUser(username = "CONSULTOR_PRUEBA")
class GestionPreguntasIntegracionTest {

    @Autowired private GestorPreguntas preguntas;
    @Autowired private GestorOpcionesRespuesta escalas;
    @Autowired private EntityManager entityManager;

    @Test
    void cu021_y_cu022_persistenPonderacionesYPermitenRecuperarLaPreguntaPorFiltrosParciales() {
        int idEscala = crearEscala("ESCALA TEST EDICION");
        PreguntaDTO solicitud = crearSolicitud("SEGUIMIENTO DEL PROYECTO", idEscala);
        preguntas.altaPregunta(solicitud);
        entityManager.flush();
        entityManager.clear();

        List<PreguntaDTO> resultados = preguntas.buscarPreguntas("proyecto", "alcance", "proyecto");
        assertThat(resultados).singleElement().satisfies(dto -> {
            assertThat(dto.getCodigoFactor()).isEqualTo("FAC001");
            assertThat(dto.getIdOpcionRespuesta()).isEqualTo(idEscala);
            assertThat(dto.getOpcionesPregunta()).extracting(OpcionPreguntaDTO::getPonderacion)
                    .containsExactlyInAnyOrder(2.0, 8.0);
        });

        solicitud.setIdPregunta(resultados.getFirst().getIdPregunta());
        solicitud.setOpcionesPregunta(List.of(ponderacion(1, 4), ponderacion(2, 6)));
        preguntas.modificarPregunta(solicitud);
        entityManager.flush();
        entityManager.clear();

        assertThat(preguntas.buscarPreguntas(null, null, solicitud.getNombre())).singleElement().satisfies(dto ->
                assertThat(dto.getOpcionesPregunta()).extracting(OpcionPreguntaDTO::getPonderacion)
                        .containsExactlyInAnyOrder(4.0, 6.0));
    }

    @Test
    void cu023_y_cu031_persistenLaBajaLogicaYLaAuditoriaDelConsultor() {
        String nombreEscala = "ESCALA TEST BAJA";
        int idEscala = crearEscala(nombreEscala);
        PreguntaDTO solicitud = crearSolicitud("CUESTION SOBRE EL ALCANCE", idEscala);
        preguntas.altaPregunta(solicitud);
        int idPregunta = preguntas.buscarPreguntas(null, null, solicitud.getNombre()).getFirst().getIdPregunta();

        LocalDateTime antesDeLaBaja = LocalDateTime.now().minusSeconds(1);
        preguntas.bajaPregunta(idPregunta);
        escalas.bajaOpcion(idEscala);
        entityManager.flush();
        entityManager.clear();

        assertThat(preguntas.buscarPreguntas(null, null, solicitud.getNombre())).isEmpty();
        assertThat(escalas.buscarOpciones(nombreEscala)).isEmpty();
        assertThat(entityManager.createQuery(
                "SELECT r FROM RegistroAuditoria r WHERE r.usuario = :usuario", RegistroAuditoria.class)
                .setParameter("usuario", "CONSULTOR_PRUEBA").getResultList())
                .allSatisfy(registro -> {
                    assertThat(registro.getFechaHora()).isBetween(antesDeLaBaja, LocalDateTime.now().plusSeconds(1));
                    assertThat(registro.getAccion()).isEqualTo("BAJA_LOGICA");
                })
                .extracting(registro -> registro.getEntidad() + "/" + registro.getIdEntidad())
                .containsExactlyInAnyOrder("Pregunta/" + idPregunta, "OpcionRespuesta/" + idEscala);
    }

    @Test
    void cu031_unaPreguntaActivaImpideDarDeBajaSuEscala() {
        String nombreEscala = "ESCALA TEST EN USO";
        int idEscala = crearEscala(nombreEscala);
        preguntas.altaPregunta(crearSolicitud("USO DE ESCALA", idEscala));

        assertThat(escalas.validarOpcionEnUso(idEscala)).isTrue();
        assertThatThrownBy(() -> escalas.bajaOpcion(idEscala))
                .isInstanceOf(IllegalArgumentException.class).hasMessageContaining("está en uso");
    }

    private int crearEscala(String nombre) {
        escalas.altaOpcion(new OpcionRespuestaDTO(null, nombre, "DESCRIPCION DE PRUEBA", null,
                List.of(new ItemOpcionRespuestaDTO(1, "NUNCA", null), new ItemOpcionRespuestaDTO(2, "SIEMPRE", null))));
        return escalas.buscarOpciones(nombre).getFirst().getId();
    }

    private static PreguntaDTO crearSolicitud(String nombre, int idEscala) {
        return PreguntaDTO.builder().nombre(nombre).textoPregunta("¿SE VERIFICA EL ALCANCE DEL PROYECTO?")
                .codigoCompetencia("COMP001").codigoFactor("FAC001").idOpcionRespuesta(idEscala)
                .opcionesPregunta(List.of(ponderacion(1, 2), ponderacion(2, 8))).build();
    }

    private static OpcionPreguntaDTO ponderacion(int orden, double peso) {
        return OpcionPreguntaDTO.builder().orden(orden).ponderacion(peso).build();
    }
}
