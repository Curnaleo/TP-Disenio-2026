package com.grupo30.capitalhumano.evaluaciones.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.grupo30.capitalhumano.evaluaciones.dao.ICuestionarioDAO;
import com.grupo30.capitalhumano.evaluaciones.dao.IPuestoDAO;
import com.grupo30.capitalhumano.evaluaciones.dto.PuestoDTO;
import com.grupo30.capitalhumano.evaluaciones.entity.CaracteristicaPuesto;
import com.grupo30.capitalhumano.evaluaciones.entity.Competencia;
import com.grupo30.capitalhumano.evaluaciones.entity.Cuestionario;
import com.grupo30.capitalhumano.evaluaciones.entity.EstadoCuestionario;
import com.grupo30.capitalhumano.evaluaciones.entity.Puesto;

@ExtendWith(MockitoExtension.class)
class GestorPuestosTest {

	@Mock
	private IPuestoDAO puestoDAO;

	@Mock
	private ICuestionarioDAO cuestionarioDAO;

	@InjectMocks
	private GestorPuestos gestorPuestos;

	@Test
	void buscarPuestos_sinCriterios_lanzaExcepcion() {
		assertThatThrownBy(() -> gestorPuestos.buscarPuestos(" ", null, ""))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("al menos un criterio");
	}

	@Test
	void buscarPuestos_conResultados_calculaCandidatosYCompletas() {
		Competencia comp = new Competencia("LID", "LIDERAZGO", "Desc", true, List.of());
		Puesto puesto = new Puesto(10, "P001", "ANALISTA DE DATOS", "Desc", "GRINGOTTS", true, List.of());
		CaracteristicaPuesto car = new CaracteristicaPuesto(1, 7.0, puesto, comp);
		puesto.setCaracteristicas(List.of(car));

		Cuestionario c1 = Cuestionario.builder().idCuestionario(1).estado(EstadoCuestionario.COMPLETO).build();
		Cuestionario c2 = Cuestionario.builder().idCuestionario(2).estado(EstadoCuestionario.INCOMPLETO).build();
		Cuestionario c3 = Cuestionario.builder().idCuestionario(3).estado(EstadoCuestionario.COMPLETO).build();

		when(puestoDAO.buscar(null, "ANALISTA", null)).thenReturn(List.of(puesto));
		when(cuestionarioDAO.listarPorPuestoYEvaluacion(10, null)).thenReturn(List.of(c1, c2, c3));

		List<PuestoDTO> resultado = gestorPuestos.buscarPuestos(null, "ANALISTA", null);

		assertThat(resultado).hasSize(1);
		PuestoDTO dto = resultado.getFirst();
		assertThat(dto.getPuestoId()).isEqualTo(10);
		assertThat(dto.getCodigo()).isEqualTo("P001");
		assertThat(dto.getCantidadCandidatos()).isEqualTo(3);
		assertThat(dto.getEvaluacionesCompletas()).isEqualTo(2);
		assertThat(dto.getCaracteristicas()).hasSize(1);
	}

	@Test
	void metodosPendientes_arrojanUnsupportedOperationException() {
		assertThatThrownBy(() -> gestorPuestos.altaPuesto(new PuestoDTO()))
			.isInstanceOf(UnsupportedOperationException.class);
		assertThatThrownBy(() -> gestorPuestos.modificarPuesto(new PuestoDTO()))
			.isInstanceOf(UnsupportedOperationException.class);
		assertThatThrownBy(() -> gestorPuestos.bajaPuesto(1))
			.isInstanceOf(UnsupportedOperationException.class);
		assertThatThrownBy(() -> gestorPuestos.validarPuestoEnUso(1))
			.isInstanceOf(UnsupportedOperationException.class);
	}
}
