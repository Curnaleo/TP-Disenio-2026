package com.grupo30.capitalhumano.evaluaciones.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.grupo30.capitalhumano.evaluaciones.dao.ICandidatoDAO;
import com.grupo30.capitalhumano.evaluaciones.dao.ICuestionarioDAO;
import com.grupo30.capitalhumano.evaluaciones.dao.IEvaluacionDAO;
import com.grupo30.capitalhumano.evaluaciones.dao.IPuestoDAO;
import com.grupo30.capitalhumano.evaluaciones.dto.CandidatoGestionDTO;
import com.grupo30.capitalhumano.evaluaciones.entity.Candidato;
import com.grupo30.capitalhumano.evaluaciones.entity.Cuestionario;
import com.grupo30.capitalhumano.evaluaciones.entity.TipoDocumento;

@ExtendWith(MockitoExtension.class)
class GestorEvaluacionesTest {

    @Mock private ICandidatoDAO candidatos;
    @Mock private IPuestoDAO puestos;
    @Mock private IEvaluacionDAO evaluaciones;
    @Mock private ICuestionarioDAO cuestionarios;
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
}
