package com.grupo30.capitalhumano.evaluaciones.controller;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.grupo30.capitalhumano.evaluaciones.dao.IBloquePreguntasDAO;
import com.grupo30.capitalhumano.evaluaciones.dao.IConfiguracionSistemaDAO;
import com.grupo30.capitalhumano.evaluaciones.dao.ICuestionarioDAO;
import com.grupo30.capitalhumano.evaluaciones.entity.BloquePreguntas;
import com.grupo30.capitalhumano.evaluaciones.entity.Candidato;
import com.grupo30.capitalhumano.evaluaciones.entity.Cuestionario;
import com.grupo30.capitalhumano.evaluaciones.entity.EstadoCuestionario;
import com.grupo30.capitalhumano.evaluaciones.exception.GlobalExceptionHandler;
import com.grupo30.capitalhumano.evaluaciones.service.GestorCuestionario;

@ExtendWith(MockitoExtension.class)
class CuestionarioControllerTest {

    private static final int ID_CUESTIONARIO = 7;
    private static final int NRO_CANDIDATO = 42;
    private static final String INICIO = "/api/cuestionario/iniciar";
    private static final String BLOQUE = "/api/cuestionario/bloques/{nroBloque}";

    @Mock private GestorCuestionario gestor;
    @Mock private ICuestionarioDAO cuestionarios;
    @Mock private IBloquePreguntasDAO bloques;
    @Mock private IConfiguracionSistemaDAO configuracion;
    @InjectMocks private CuestionarioController controller;
    private MockMvc mvc;

    @BeforeEach
    void prepararMvc() {
        mvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler()).build();
    }

    @ParameterizedTest
    @EnumSource(value = EstadoCuestionario.class, names = {"INCOMPLETO", "SIN_CONTESTAR"})
    void cu026_explicaPorQueNoPuedeAbrirseUnCuestionarioCerrado(EstadoCuestionario estado) throws Exception {
        asignarCuestionario(estado);

        mvc.perform(post(INICIO).principal(sesionCandidato()))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.motivo").value(estado.name()));
        verifyNoInteractions(gestor);
    }

    @Test
    void cu026_rechazaOtroIngresoSiYaSeConsumieronLosAccesos() throws Exception {
        asignarCuestionario(EstadoCuestionario.EN_PROCESO);
        when(gestor.verificarAccesosMaximos(ID_CUESTIONARIO)).thenReturn(true);

        mvc.perform(post(INICIO).principal(sesionCandidato()))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.motivo").value("ACCESOS_SUPERADOS"));
        verify(gestor, never()).iniciarCuestionario(ID_CUESTIONARIO);
    }

    @Test
    void cu026_noPermiteConsultarNiGuardarElSiguienteBloqueAntesDeResponderElActual() throws Exception {
        asignarCuestionario(EstadoCuestionario.EN_PROCESO);
        when(bloques.recuperarBloquesPorCuestionario(ID_CUESTIONARIO))
                .thenReturn(List.of(bloque(1, false), bloque(2, false)));

        mvc.perform(get(BLOQUE, 2).principal(sesionCandidato()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mensaje").value("Solo podés consultar el bloque pendiente."));
        mvc.perform(put(BLOQUE, 2).principal(sesionCandidato())
                .contentType(MediaType.APPLICATION_JSON).content("[]"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mensaje").value("Solo podés guardar el bloque pendiente."));
        verify(gestor, never()).recuperarBloque(ID_CUESTIONARIO, 2);
        verify(gestor, never()).guardarBloque(eq(ID_CUESTIONARIO), anyList());
    }

    @Test
    void cu026_rechazaElJsonInvalidoAntesDeGuardarRespuestas() throws Exception {
        mvc.perform(put(BLOQUE, 1).principal(sesionCandidato())
                .contentType(MediaType.APPLICATION_JSON).content("[{"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mensaje").value("El formato de la solicitud no es válido."));
        verifyNoInteractions(gestor, cuestionarios, bloques);
    }

    @Test
    void cu026_repetirElGuardadoDeUnBloqueYaContestadoDevuelveElPendiente() throws Exception {
        asignarCuestionario(EstadoCuestionario.EN_PROCESO);
        when(bloques.recuperarBloquesPorCuestionario(ID_CUESTIONARIO))
                .thenReturn(List.of(bloque(1, true), bloque(2, false)));

        mvc.perform(put(BLOQUE, 1).principal(sesionCandidato())
                .contentType(MediaType.APPLICATION_JSON).content("[]"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estado").value("EN_PROCESO"))
                .andExpect(jsonPath("$.siguienteBloque").value(2));
        verify(gestor, never()).guardarBloque(eq(ID_CUESTIONARIO), anyList());
    }

    private void asignarCuestionario(EstadoCuestionario estado) {
        Cuestionario cuestionario = Cuestionario.builder().idCuestionario(ID_CUESTIONARIO).estado(estado)
                .candidato(Candidato.builder().nroCandidato(NRO_CANDIDATO).build()).build();
        when(cuestionarios.findById(ID_CUESTIONARIO)).thenReturn(Optional.of(cuestionario));
    }

    private static Authentication sesionCandidato() {
        return UsernamePasswordAuthenticationToken.authenticated("CANDIDATO",
                Map.of("idCuestionario", ID_CUESTIONARIO, "nroCandidato", NRO_CANDIDATO), List.of());
    }

    private static BloquePreguntas bloque(int numero, boolean completado) {
        return BloquePreguntas.builder().nroBloque(numero).completado(completado).build();
    }
}
