package com.grupo30.capitalhumano.evaluaciones.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import com.grupo30.capitalhumano.evaluaciones.dao.ICandidatoDAO;
import com.grupo30.capitalhumano.evaluaciones.dao.ICuestionarioDAO;
import com.grupo30.capitalhumano.evaluaciones.dao.IEvaluacionDAO;
import com.grupo30.capitalhumano.evaluaciones.dao.IFactorDAO;
import com.grupo30.capitalhumano.evaluaciones.dao.IPreguntaDAO;
import com.grupo30.capitalhumano.evaluaciones.dao.IPuestoDAO;
import com.grupo30.capitalhumano.evaluaciones.dao.IUsuarioDAO;
import com.grupo30.capitalhumano.evaluaciones.dto.CandidatoGestionDTO;
import com.grupo30.capitalhumano.evaluaciones.entity.Candidato;
import com.grupo30.capitalhumano.evaluaciones.entity.CaracteristicaPuesto;
import com.grupo30.capitalhumano.evaluaciones.entity.Competencia;
import com.grupo30.capitalhumano.evaluaciones.entity.Cuestionario;
import com.grupo30.capitalhumano.evaluaciones.entity.EstadoCuestionario;
import com.grupo30.capitalhumano.evaluaciones.entity.Factor;
import com.grupo30.capitalhumano.evaluaciones.entity.Pregunta;
import com.grupo30.capitalhumano.evaluaciones.entity.Puesto;
import com.grupo30.capitalhumano.evaluaciones.entity.TipoDocumento;
import com.grupo30.capitalhumano.evaluaciones.entity.Usuario;

@ExtendWith(MockitoExtension.class)
class GestorEvaluacionesTest {

    @Mock private ICandidatoDAO candidatos;
    @Mock private IPuestoDAO puestos;
    @Mock private IEvaluacionDAO evaluaciones;
    @Mock private ICuestionarioDAO cuestionarios;
    @Mock private IFactorDAO factores;
    @Mock private IPreguntaDAO preguntas;
    @Mock private IUsuarioDAO usuarios;
    @InjectMocks private GestorEvaluaciones gestor;

    @Test
    void cu025_busquedaDevuelveDatosIdentificatoriosDelCandidato() {
        Candidato candidato = new Candidato();
        candidato.setNroCandidato(42);
        candidato.setApellido("PEREZ");
        candidato.setNombre("ANA");
        candidato.setTipoDocumento(TipoDocumento.DNI);
        candidato.setNroDocumento("30123456");
        candidato.setEmail("ana@example.test");
        when(candidatos.buscar("PER", "ANA", 42)).thenReturn(List.of(candidato));

        assertThat(gestor.buscarCandidatos("PER", "ANA", 42)).singleElement().satisfies(dto -> {
            assertThat(dto.getNroCandidato()).isEqualTo(42);
            assertThat(dto.getApellido()).isEqualTo("PEREZ");
            assertThat(dto.getNombre()).isEqualTo("ANA");
            assertThat(dto.getTipoDocumento()).isEqualTo(TipoDocumento.DNI);
            assertThat(dto.getNroDocumento()).isEqualTo("30123456");
            assertThat(dto.getEmail()).isEqualTo("ana@example.test");
        });
    }

    @Test
    void cu025_busquedaSinCoincidenciasDevuelveListaVacia() {
        when(candidatos.buscar("INEXISTENTE", null, null)).thenReturn(List.of());

        assertThat(gestor.buscarCandidatos("INEXISTENTE", null, null)).isEmpty();
    }

    @Test
    void cu025_informaSoloLosSeleccionadosQueYaTienenCuestionarioActivo() {
        CandidatoGestionDTO sinActivo = CandidatoGestionDTO.builder().nroCandidato(41).build();
        CandidatoGestionDTO conActivo = CandidatoGestionDTO.builder().nroCandidato(42).build();
        when(cuestionarios.buscarActivoPorCandidato(41)).thenReturn(Optional.empty());
        when(cuestionarios.buscarActivoPorCandidato(42)).thenReturn(Optional.of(new Cuestionario()));

        assertThat(gestor.validarCandidatosActivos(List.of(sinActivo, conActivo))).containsExactly(conActivo);
        verifyNoInteractions(puestos, evaluaciones);
    }

    @Test
    void cu025_confirmaUnaSolaVezConLaMismaClaveQueMostro() {
        Candidato candidato = new Candidato();
        candidato.setNroCandidato(42);
        CandidatoGestionDTO seleccion = CandidatoGestionDTO.builder().nroCandidato(42).build();
        Usuario consultor = new Usuario();
        consultor.setIdUsuario(7);
        Competencia competencia = new Competencia();
        competencia.setCodigo("C01");
        CaracteristicaPuesto caracteristica = new CaracteristicaPuesto();
        caracteristica.setCompetencia(competencia);
        Puesto puesto = new Puesto();
        puesto.getCaracteristicas().add(caracteristica);
        Factor sinPreguntas = new Factor();
        sinPreguntas.setCodigo("F01");
        Factor evaluable = new Factor();
        evaluable.setCodigo("F02");

        when(usuarios.buscarPorUser("consultor")).thenReturn(Optional.of(consultor));
        when(puestos.findById(1)).thenReturn(puesto);
        when(candidatos.findById(42)).thenReturn(Optional.of(candidato));
        when(factores.buscarPorCompetencia("C01")).thenReturn(List.of(sinPreguntas, evaluable));
        when(preguntas.buscarActivasPorFactor("F01")).thenReturn(List.of());
        when(preguntas.buscarActivasPorFactor("F02")).thenReturn(List.of(new Pregunta(), new Pregunta()));

        SecurityContextHolder.getContext().setAuthentication(
            new UsernamePasswordAuthenticationToken("consultor", null));
        try {
            var vista = gestor.prepararEvaluacion(1, List.of(seleccion));
            String clave = vista.candidatos().getFirst().clave();
            assertThat(clave).matches("(?=.*[A-Z])(?=.*[0-9])[A-Z0-9]{8}");
            verifyNoInteractions(evaluaciones);

            gestor.confirmarEvaluacion(1, vista.id());

            var guardado = ArgumentCaptor.forClass(Cuestionario.class);
            verify(cuestionarios).guardar(guardado.capture());
            assertThat(guardado.getValue().getClave()).isEqualTo(clave);
            assertThat(guardado.getValue().getEstado()).isEqualTo(EstadoCuestionario.ACTIVO);
            assertThatThrownBy(() -> gestor.confirmarEvaluacion(1, vista.id()))
                .isInstanceOf(IllegalArgumentException.class);
        } finally {
            SecurityContextHolder.clearContext();
        }
    }
}
