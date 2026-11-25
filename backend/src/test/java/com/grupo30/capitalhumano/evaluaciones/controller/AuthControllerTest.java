package com.grupo30.capitalhumano.evaluaciones.controller;

import com.grupo30.capitalhumano.evaluaciones.dto.CandidatoCuestionarioDTO;
import com.grupo30.capitalhumano.evaluaciones.dto.UsuarioDTO;
import com.grupo30.capitalhumano.evaluaciones.entity.EstadoCuestionario;
import com.grupo30.capitalhumano.evaluaciones.entity.TipoDocumento;
import com.grupo30.capitalhumano.evaluaciones.exception.AutenticacionException;
import com.grupo30.capitalhumano.evaluaciones.exception.GlobalExceptionHandler;
import com.grupo30.capitalhumano.evaluaciones.security.JwtTokenProvider;
import com.grupo30.capitalhumano.evaluaciones.service.AutenticacionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import tools.jackson.databind.ObjectMapper;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private AutenticacionService autenticacionService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        JwtTokenProvider jwtTokenProvider = new JwtTokenProvider(
                new ObjectMapper(),
                "ClaveSecretaDePruebaParaTestsUnitarios2026!",
                3600000L);
        AuthController controller = new AuthController(autenticacionService, jwtTokenProvider);
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void autenticarConsultor_conCredencialesValidas_retorna200YToken() throws Exception {
        UsuarioDTO usuario = UsuarioDTO.builder()
                .idUsuario(1)
                .nombre("CONSULTOR1")
                .activo(true)
                .build();
        when(autenticacionService.autenticarConsultor("consultor1", "admin123")).thenReturn(usuario);

        mockMvc.perform(post("/api/auth/consultor")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"usuario\":\"consultor1\",\"contrasena\":\"admin123\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").isNotEmpty())
                .andExpect(jsonPath("$.tipoToken").value("Bearer"))
                .andExpect(jsonPath("$.rol").value("CONSULTOR"))
                .andExpect(jsonPath("$.consultor.nombre").value("CONSULTOR1"));
    }

    @Test
    void autenticarConsultor_conCredencialesInvalidas_retorna401ConMensajeCU001() throws Exception {
        when(autenticacionService.autenticarConsultor("consultor1", "mal"))
                .thenThrow(new AutenticacionException("El usuario o la contraseña no son válidos"));

        mockMvc.perform(post("/api/auth/consultor")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"usuario\":\"consultor1\",\"contrasena\":\"mal\"}"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.status").value(401))
                .andExpect(jsonPath("$.mensaje").value("El usuario o la contraseña no son válidos"));
    }

    @Test
    void autenticarConsultor_conUsuarioMayorA15Caracteres_retorna400() throws Exception {
        mockMvc.perform(post("/api/auth/consultor")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"usuario\":\"DEMASIADO_LARGO_16\",\"contrasena\":\"admin123\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.errores.usuario").exists());
    }

    @Test
    void autenticarCandidato_conDatosValidos_retorna200YToken() throws Exception {
        CandidatoCuestionarioDTO dto = CandidatoCuestionarioDTO.builder()
                .idCuestionario(10)
                .nroCandidato(200)
                .apellido("Gómez")
                .nombre("Ana")
                .estado(EstadoCuestionario.ACTIVO)
                .build();
        when(autenticacionService.autenticarCandidato(TipoDocumento.DNI, "30111222", "CLAVE"))
                .thenReturn(dto);

        mockMvc.perform(post("/api/auth/candidato")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"tipoDocumento\":\"DNI\",\"nroDocumento\":\"30111222\",\"clave\":\"CLAVE\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").isNotEmpty())
                .andExpect(jsonPath("$.rol").value("CANDIDATO"))
                .andExpect(jsonPath("$.candidato.idCuestionario").value(10));
    }

    @Test
    void autenticarCandidato_conDatosInvalidos_retorna401ConMensajeCU001() throws Exception {
        when(autenticacionService.autenticarCandidato(TipoDocumento.DNI, "30111222", "MAL"))
                .thenThrow(new AutenticacionException(
                        "Los datos ingresados no son válidos o no existe un cuestionario para el Candidato"));

        mockMvc.perform(post("/api/auth/candidato")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"tipoDocumento\":\"DNI\",\"nroDocumento\":\"30111222\",\"clave\":\"MAL\"}"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.status").value(401))
                .andExpect(jsonPath("$.mensaje")
                        .value("Los datos ingresados no son válidos o no existe un cuestionario para el Candidato"));
    }

    @Test
    void cerrarSesion_retorna204() throws Exception {
        mockMvc.perform(post("/api/auth/logout"))
                .andExpect(status().isNoContent());

        verify(autenticacionService).cerrarSesion();
    }
}
