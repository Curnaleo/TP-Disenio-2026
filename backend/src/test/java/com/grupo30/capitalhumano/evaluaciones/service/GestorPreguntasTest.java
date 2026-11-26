package com.grupo30.capitalhumano.evaluaciones.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.grupo30.capitalhumano.evaluaciones.dao.ICompetenciaDAO;
import com.grupo30.capitalhumano.evaluaciones.dao.IFactorDAO;
import com.grupo30.capitalhumano.evaluaciones.dao.IOpcionRespuestaDAO;
import com.grupo30.capitalhumano.evaluaciones.dao.IPreguntaDAO;
import com.grupo30.capitalhumano.evaluaciones.dto.OpcionPreguntaDTO;
import com.grupo30.capitalhumano.evaluaciones.dto.PreguntaDTO;
import com.grupo30.capitalhumano.evaluaciones.entity.Competencia;
import com.grupo30.capitalhumano.evaluaciones.entity.Factor;
import com.grupo30.capitalhumano.evaluaciones.entity.ItemOpcionRespuesta;
import com.grupo30.capitalhumano.evaluaciones.entity.OpcionPregunta;
import com.grupo30.capitalhumano.evaluaciones.entity.OpcionRespuesta;
import com.grupo30.capitalhumano.evaluaciones.entity.Pregunta;
import com.grupo30.capitalhumano.evaluaciones.exception.PreguntaDuplicadaException;

@ExtendWith(MockitoExtension.class)
class GestorPreguntasTest {

    @Mock private IPreguntaDAO preguntas;
    @Mock private IFactorDAO factores;
    @Mock private ICompetenciaDAO competencias;
    @Mock private IOpcionRespuestaDAO escalas;
    @Mock private AuditoriaBajas auditoria;
    @InjectMocks private GestorPreguntas gestor;

    private Competencia competencia;
    private Factor factor;
    private OpcionRespuesta escala;

    @BeforeEach
    void prepararDatos() {
        competencia = new Competencia();
        competencia.setCodigo("C1");
        competencia.setNombre("COMUNICACION");
        factor = new Factor();
        factor.setCodigo("F1");
        factor.setNombre("ESCUCHA");
        factor.setCompetencia(competencia);
        escala = new OpcionRespuesta(7, "FRECUENCIA", "ESCALA DE FRECUENCIA", true, new ArrayList<>());
        escala.getItems().add(new ItemOpcionRespuesta(21, 1, "NUNCA", escala));
        escala.getItems().add(new ItemOpcionRespuesta(22, 2, "SIEMPRE", escala));
    }

    @Test
    void cu020_busquedaDevuelveDatosDePreguntaFactorCompetenciaYEscala() {
        Pregunta pregunta = preguntaExistente();
        when(preguntas.buscarPreguntas("COM", "ESCU", "REUNION")).thenReturn(List.of(pregunta));

        assertThat(gestor.buscarPreguntas("COM", "ESCU", "REUNION")).singleElement().satisfies(dto -> {
            assertThat(dto.getIdPregunta()).isEqualTo(8);
            assertThat(dto.getNombre()).isEqualTo("REUNION");
            assertThat(dto.getCodigoFactor()).isEqualTo("F1");
            assertThat(dto.getNombreCompetencia()).isEqualTo("COMUNICACION");
            assertThat(dto.getIdOpcionRespuesta()).isEqualTo(7);
            assertThat(dto.getOpcionesPregunta()).extracting(OpcionPreguntaDTO::getPonderacion)
                    .containsExactly(4.0, 6.0);
        });
    }

    @Test
    void cu020_busquedaSinCoincidenciasDevuelveListaVacia() {
        when(preguntas.buscarPreguntas(null, null, "INEXISTENTE")).thenReturn(List.of());

        assertThat(gestor.buscarPreguntas(null, null, "INEXISTENTE")).isEmpty();
    }

    @ParameterizedTest
    @CsvSource({"0, 10", "3.5, 6.5", "10, 0"})
    void cu021_guardaUnaPonderacionPorItemCuandoSumanDiez(double primerPeso, double segundoPeso) {
        prepararReferenciasDeAlta();

        gestor.altaPregunta(solicitud(primerPeso, segundoPeso));

        ArgumentCaptor<Pregunta> captor = ArgumentCaptor.forClass(Pregunta.class);
        verify(preguntas).guardar(captor.capture());
        Pregunta guardada = captor.getValue();
        assertThat(guardada.getId()).isNull();
        assertThat(guardada.getActivo()).isTrue();
        assertThat(guardada.getFactor()).isSameAs(factor);
        assertThat(guardada.getOpcionRespuesta()).isSameAs(escala);
        assertThat(guardada.getOpcionesPregunta()).extracting(OpcionPregunta::getPonderacion)
                .containsExactly(primerPeso, segundoPeso);
        assertThat(guardada.getOpcionesPregunta()).extracting(OpcionPregunta::getItemOpcionRespuesta)
                .containsExactlyElementsOf(escala.getItems());
        assertThat(guardada.getOpcionesPregunta()).allSatisfy(opcion ->
                assertThat(opcion.getPregunta()).isSameAs(guardada));
    }

    @Test
    void cu021_informaLosObligatoriosOmitidosAntesDeConsultarLosDatos() {
        assertThatThrownBy(() -> gestor.altaPregunta(PreguntaDTO.builder().build()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContainingAll("Nombre de la pregunta", "Texto de la pregunta",
                        "Código de competencia", "Código de factor", "escala de respuesta", "ponderaciones");
        verifyNoInteractions(preguntas, factores, competencias, escalas);
    }

    @ParameterizedTest
    @ValueSource(doubles = {9.99, 10.01})
    void cu021_rechazaSumaDistintaDeDiez(double total) {
        prepararReferenciasDeAlta();

        assertAltaRechazada(solicitud(0, total), "deben sumar 10 puntos");
    }

    @Test
    void cu021_rechazaPonderacionNegativaAunqueElTotalSeaDiez() {
        prepararReferenciasDeAlta();

        assertAltaRechazada(solicitud(-1, 11), "no negativos");
    }

    @Test
    void cu021_rechazaUnItemAjenoALaEscala() {
        prepararReferenciasDeAlta();
        PreguntaDTO dto = solicitud(4, 6);
        dto.setOpcionesPregunta(List.of(peso(1, 4), peso(3, 6)));

        assertAltaRechazada(dto, "no pertenece a la escala");
    }

    @Test
    void cu021_rechazaEscalaInactiva() {
        escala.setActivo(false);
        when(escalas.findById(7)).thenReturn(Optional.of(escala));

        assertAltaRechazada(solicitud(4, 6), "escala seleccionada está inactiva");
    }

    @Test
    void cu021_rechazaFactorDeOtraCompetencia() {
        prepararReferenciasDeAlta();
        Competencia otra = new Competencia();
        otra.setCodigo("C2");
        factor.setCompetencia(otra);

        assertAltaRechazada(solicitud(4, 6), "no pertenece a la competencia");
    }

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    void cu021_rechazaNombreOTextoDuplicadoDentroDelMismoFactor(boolean repetirNombre) {
        prepararReferenciasDeAlta();
        Pregunta existente = preguntaExistente();
        if (repetirNombre) {
            existente.setTextoPregunta("OTRO TEXTO");
        } else {
            existente.setNombre("OTRO NOMBRE");
        }
        when(preguntas.buscarPreguntas("COMUNICACION", "ESCUCHA", "")).thenReturn(List.of(existente));

        assertThatThrownBy(() -> gestor.altaPregunta(solicitud(4, 6)))
                .isInstanceOf(PreguntaDuplicadaException.class);
        verify(preguntas, never()).guardar(any());
    }

    @Test
    void cu022_modificaPonderacionesSinDetectarseASiMismaComoDuplicada() {
        Pregunta existente = preguntaExistente();
        when(preguntas.findById(8)).thenReturn(Optional.of(existente));
        when(escalas.findById(7)).thenReturn(Optional.of(escala));
        when(preguntas.buscarPreguntas("COMUNICACION", "ESCUCHA", "")).thenReturn(List.of(existente));
        PreguntaDTO dto = solicitud(2, 8);
        dto.setIdPregunta(8);

        gestor.modificarPregunta(dto);

        verify(preguntas).actualizar(existente);
        assertThat(existente.getOpcionesPregunta()).extracting(OpcionPregunta::getId).containsExactly(31, 32);
        assertThat(existente.getOpcionesPregunta()).extracting(OpcionPregunta::getPonderacion)
                .containsExactly(2.0, 8.0);
    }

    @Test
    void cu022_rechazaElCambioDeFactorSinModificarLaPregunta() {
        Pregunta existente = preguntaExistente();
        when(preguntas.findById(8)).thenReturn(Optional.of(existente));
        PreguntaDTO dto = solicitud(2, 8);
        dto.setIdPregunta(8);
        dto.setCodigoFactor("F2");

        assertThatThrownBy(() -> gestor.modificarPregunta(dto))
                .isInstanceOf(IllegalArgumentException.class).hasMessageContaining("No se puede cambiar el factor");
        verify(preguntas, never()).actualizar(any());
        assertThat(existente.getFactor()).isSameAs(factor);
    }

    @Test
    void cu023_bajaLogicaDeUnaPreguntaExistenteGeneraAuditoria() {
        when(preguntas.findById(8)).thenReturn(Optional.of(preguntaExistente()));

        gestor.bajaPregunta(8);

        verify(preguntas).eliminarLogico(8);
        verify(auditoria).registrar("Pregunta", 8);
    }

    @Test
    void cu023_preguntaInexistenteNoSeEliminaNiAudita() {
        assertThatThrownBy(() -> gestor.bajaPregunta(8))
                .isInstanceOf(IllegalArgumentException.class).hasMessageContaining("No existe la pregunta");
        verify(preguntas, never()).eliminarLogico(8);
        verifyNoInteractions(auditoria);
    }

    private void prepararReferenciasDeAlta() {
        when(escalas.findById(7)).thenReturn(Optional.of(escala));
        when(factores.findByCodigo("F1")).thenReturn(Optional.of(factor));
        when(competencias.findByCodigo("C1")).thenReturn(Optional.of(competencia));
    }

    private void assertAltaRechazada(PreguntaDTO dto, String mensaje) {
        assertThatThrownBy(() -> gestor.altaPregunta(dto))
                .isInstanceOf(IllegalArgumentException.class).hasMessageContaining(mensaje);
        verify(preguntas, never()).guardar(any());
    }

    private Pregunta preguntaExistente() {
        Pregunta pregunta = new Pregunta(8, "REUNION", "¿ESCUCHA DURANTE LA REUNION?", null,
                true, factor, escala, new ArrayList<>());
        pregunta.getOpcionesPregunta().add(new OpcionPregunta(31, 4.0, pregunta, escala.getItems().get(0)));
        pregunta.getOpcionesPregunta().add(new OpcionPregunta(32, 6.0, pregunta, escala.getItems().get(1)));
        return pregunta;
    }

    private static PreguntaDTO solicitud(double primerPeso, double segundoPeso) {
        return PreguntaDTO.builder().nombre("REUNION").textoPregunta("¿ESCUCHA DURANTE LA REUNION?")
                .codigoCompetencia("C1").codigoFactor("F1").idOpcionRespuesta(7)
                .opcionesPregunta(List.of(peso(1, primerPeso), peso(2, segundoPeso))).build();
    }

    private static OpcionPreguntaDTO peso(int orden, double ponderacion) {
        return OpcionPreguntaDTO.builder().orden(orden).ponderacion(ponderacion).build();
    }
}
