package com.grupo30.capitalhumano.evaluaciones.autenticacion.service;

import com.grupo30.capitalhumano.evaluaciones.autenticacion.exception.AutenticacionException;
import com.grupo30.capitalhumano.evaluaciones.autenticacion.ldap.LdapAuthService;

import com.grupo30.capitalhumano.evaluaciones.dto.CandidatoCuestionarioDTO;
import com.grupo30.capitalhumano.evaluaciones.dto.UsuarioDTO;
import com.grupo30.capitalhumano.evaluaciones.entity.Candidato;
import com.grupo30.capitalhumano.evaluaciones.entity.Cuestionario;
import com.grupo30.capitalhumano.evaluaciones.entity.EstadoCuestionario;
import com.grupo30.capitalhumano.evaluaciones.entity.TipoDocumento;
import com.grupo30.capitalhumano.evaluaciones.entity.Usuario;
import com.grupo30.capitalhumano.evaluaciones.repository.CandidatoRepository;
import com.grupo30.capitalhumano.evaluaciones.repository.CuestionarioRepository;
import com.grupo30.capitalhumano.evaluaciones.repository.UsuarioRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GestorAutenticacionImplTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private CandidatoRepository candidatoRepository;

    @Mock
    private CuestionarioRepository cuestionarioRepository;

    @Mock
    private LdapAuthService ldapAuthService;

    @InjectMocks
    private GestorAutenticacionImpl gestorAutenticacion;

    @AfterEach
    void limpiarContexto() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void autenticarConsultor_conCredencialesValidas_normalizaMayusculasYRetornaDTO() {
        Usuario usuario = Usuario.builder()
                .idUsuario(1)
                .nombre("CONSULTOR1")
                .activo(true)
                .build();

        when(ldapAuthService.autenticar("CONSULTOR1", "admin123")).thenReturn(true);
        when(usuarioRepository.buscarPorUser("CONSULTOR1")).thenReturn(Optional.of(usuario));

        UsuarioDTO resultado = gestorAutenticacion.autenticarConsultor("  consultor1 ", "admin123");

        assertThat(resultado.getIdUsuario()).isEqualTo(1);
        assertThat(resultado.getNombre()).isEqualTo("CONSULTOR1");
        assertThat(resultado.getActivo()).isTrue();
    }

    @Test
    void cu001_consultorConLongitudesMaximasPuedeAutenticarse() {
        String usuario = "A".repeat(15);
        String clave = "X".repeat(20);
        when(ldapAuthService.autenticar(usuario, clave)).thenReturn(true);
        when(usuarioRepository.buscarPorUser(usuario)).thenReturn(Optional.of(
                Usuario.builder().idUsuario(1).nombre(usuario).activo(true).build()));

        assertThat(gestorAutenticacion.autenticarConsultor(usuario, clave).getNombre()).isEqualTo(usuario);
    }

    @Test
    void autenticarConsultor_conUsuarioMayorA15Caracteres_lanzaExcepcion() {
        assertThatThrownBy(() -> gestorAutenticacion.autenticarConsultor("A".repeat(16), "admin123"))
                .isInstanceOf(AutenticacionException.class)
                .hasMessage("El usuario o la contraseña no son válidos");

        verifyNoInteractions(ldapAuthService, usuarioRepository);
    }

    @Test
    void autenticarConsultor_conPasswordMayorA20Caracteres_lanzaExcepcion() {
        assertThatThrownBy(() -> gestorAutenticacion.autenticarConsultor("CONSULTOR1", "123456789012345678901"))
                .isInstanceOf(AutenticacionException.class)
                .hasMessage("El usuario o la contraseña no son válidos");

        verifyNoInteractions(ldapAuthService, usuarioRepository);
    }

    @Test
    void autenticarConsultor_cuandoLdapFalla_lanzaExcepcion() {
        when(ldapAuthService.autenticar("CONSULTOR1", "incorrecta")).thenReturn(false);

        assertThatThrownBy(() -> gestorAutenticacion.autenticarConsultor("consultor1", "incorrecta"))
                .isInstanceOf(AutenticacionException.class)
                .hasMessage("El usuario o la contraseña no son válidos");

        verifyNoInteractions(usuarioRepository);
    }

    @Test
    void autenticarConsultor_cuandoUsuarioEstaInactivo_lanzaExcepcion() {
        Usuario inactivo = Usuario.builder()
                .idUsuario(2)
                .nombre("CONSULTOR2")
                .activo(false)
                .build();

        when(ldapAuthService.autenticar("CONSULTOR2", "admin123")).thenReturn(true);
        when(usuarioRepository.buscarPorUser("CONSULTOR2")).thenReturn(Optional.of(inactivo));

        assertThatThrownBy(() -> gestorAutenticacion.autenticarConsultor("consultor2", "admin123"))
                .isInstanceOf(AutenticacionException.class)
                .hasMessage("El usuario o la contraseña no son válidos");
    }

    @Test
    void autenticarCandidato_conDatosValidos_retornaCandidatoCuestionarioDTO() {
        Candidato candidato = Candidato.builder()
                .nroCandidato(100)
                .apellido("Pérez")
                .nombre("Juan")
                .tipoDocumento(TipoDocumento.DNI)
                .nroDocumento("40123456")
                .eliminado(false)
                .build();

        Cuestionario cuestionario = Cuestionario.builder()
                .idCuestionario(55)
                .clave("CLAVE123")
                .estado(EstadoCuestionario.ACTIVO)
                .candidato(candidato)
                .build();

        when(candidatoRepository.buscarPorDocumento(TipoDocumento.DNI, "40123456"))
                .thenReturn(Optional.of(candidato));
        when(cuestionarioRepository.buscarPorClaveYDoc(TipoDocumento.DNI, "40123456", "CLAVE123"))
                .thenReturn(Optional.of(cuestionario));

        CandidatoCuestionarioDTO resultado = gestorAutenticacion.autenticarCandidato(
                TipoDocumento.DNI, " 40123456 ", "CLAVE123");

        assertThat(resultado.getIdCuestionario()).isEqualTo(55);
        assertThat(resultado.getNroCandidato()).isEqualTo(100);
        assertThat(resultado.getApellido()).isEqualTo("Pérez");
        assertThat(resultado.getNombre()).isEqualTo("Juan");
        assertThat(resultado.getEstado()).isEqualTo(EstadoCuestionario.ACTIVO);
    }

    @Test
    void autenticarCandidato_cuandoNoExisteCuestionarioOClaveIncorrecta_lanzaExcepcion() {
        Candidato candidato = Candidato.builder()
                .nroCandidato(100)
                .tipoDocumento(TipoDocumento.DNI)
                .nroDocumento("40123456")
                .build();

        when(candidatoRepository.buscarPorDocumento(TipoDocumento.DNI, "40123456"))
                .thenReturn(Optional.of(candidato));
        when(cuestionarioRepository.buscarPorClaveYDoc(TipoDocumento.DNI, "40123456", "MAL"))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> gestorAutenticacion.autenticarCandidato(
                TipoDocumento.DNI, "40123456", "MAL"))
                .isInstanceOf(AutenticacionException.class)
                .hasMessage("Los datos ingresados no son válidos o no existe un cuestionario para el Candidato");
    }

    @Test
    void cerrarSesion_limpiaSecurityContext() {
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken("CONSULTOR1", null));

        gestorAutenticacion.cerrarSesion();

        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
    }
}
