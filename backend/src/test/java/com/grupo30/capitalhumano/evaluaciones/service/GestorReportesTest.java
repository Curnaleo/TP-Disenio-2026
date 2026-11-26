package com.grupo30.capitalhumano.evaluaciones.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.grupo30.capitalhumano.evaluaciones.dao.ICuestionarioDAO;
import com.grupo30.capitalhumano.evaluaciones.dao.IEvaluacionDAO;
import com.grupo30.capitalhumano.evaluaciones.dao.IPuestoDAO;
import com.grupo30.capitalhumano.evaluaciones.dao.IResultadoCompetenciaDAO;
import com.grupo30.capitalhumano.evaluaciones.dto.CandidatoOrdenMeritoDTO;
import com.grupo30.capitalhumano.evaluaciones.dto.EvaluacionDTO;
import com.grupo30.capitalhumano.evaluaciones.entity.Candidato;
import com.grupo30.capitalhumano.evaluaciones.entity.CaracteristicaPuesto;
import com.grupo30.capitalhumano.evaluaciones.entity.Competencia;
import com.grupo30.capitalhumano.evaluaciones.entity.Cuestionario;
import com.grupo30.capitalhumano.evaluaciones.entity.EstadoCuestionario;
import com.grupo30.capitalhumano.evaluaciones.entity.Evaluacion;
import com.grupo30.capitalhumano.evaluaciones.entity.Puesto;
import com.grupo30.capitalhumano.evaluaciones.entity.ResultadoCompetencia;
import com.grupo30.capitalhumano.evaluaciones.entity.TipoDocumento;

@ExtendWith(MockitoExtension.class)
class GestorReportesTest {

	@Mock
	private ICuestionarioDAO cuestionarioDAO;

	@Mock
	private IPuestoDAO puestoDAO;

	@Mock
	private IEvaluacionDAO evaluacionDAO;

	@Mock
	private IResultadoCompetenciaDAO resultadoCompetenciaDAO;

	@InjectMocks
	private GestorReportes gestorReportes;

	@Test
	void listarEvaluaciones_retornaDTOsConCantidadCandidatos() {
		Puesto puesto = new Puesto(10, "P001", "ANALISTA", "Desc", "EMPRESA", true, List.of());
		Evaluacion ev = new Evaluacion(100, "EVAL 1", "Desc", LocalDate.of(2026, 10, 1), true, puesto, null, List.of());

		when(puestoDAO.findById(10)).thenReturn(puesto);
		when(evaluacionDAO.listarPorPuesto(10)).thenReturn(List.of(ev));
		when(cuestionarioDAO.listarPorPuestoYEvaluacion(10, 100))
			.thenReturn(List.of(new Cuestionario(), new Cuestionario()));

		List<EvaluacionDTO> resultado = gestorReportes.listarEvaluaciones(10);

		assertThat(resultado).hasSize(1);
		assertThat(resultado.getFirst().getIdEvaluacion()).isEqualTo(100);
		assertThat(resultado.getFirst().getNombrePuesto()).isEqualTo("ANALISTA");
		assertThat(resultado.getFirst().getCantidadCandidatos()).isEqualTo(2);
	}

	@Test
	void generarOrdenMerito_ordenaAprobadosPorPuntajeYAgrupaRestoPorEstado() {
		Competencia comp1 = new Competencia("LID", "LIDERAZGO", "Desc", true, List.of());
		Competencia comp2 = new Competencia("COM", "COMUNICACION", "Desc", true, List.of());
		Puesto puesto = new Puesto(10, "P001", "ANALISTA", "Desc", "EMPRESA", true, List.of());
		puesto.setCaracteristicas(List.of(
			new CaracteristicaPuesto(1, 7.0, puesto, comp1),
			new CaracteristicaPuesto(2, 6.0, puesto, comp2)
		));

		Candidato candAprobadoMayor = Candidato.builder()
			.nroCandidato(1).tipoDocumento(TipoDocumento.DNI).nroDocumento("111")
			.apellido("LOPEZ").nombre("ANDREA").build();
		Candidato candAprobadoMenor = Candidato.builder()
			.nroCandidato(2).tipoDocumento(TipoDocumento.DNI).nroDocumento("222")
			.apellido("SANCHEZ").nombre("DIEGO").build();
		Candidato candNoAlcanza = Candidato.builder()
			.nroCandidato(3).tipoDocumento(TipoDocumento.DNI).nroDocumento("333")
			.apellido("GOMEZ").nombre("LAURA").build();
		Candidato candIncompleto = Candidato.builder()
			.nroCandidato(4).tipoDocumento(TipoDocumento.DNI).nroDocumento("444")
			.apellido("RODRIGUEZ").nombre("MARTIN").build();

		LocalDateTime ahora = LocalDateTime.of(2026, 11, 1, 10, 0);

		Cuestionario q1 = Cuestionario.builder()
			.idCuestionario(1).candidato(candAprobadoMenor).estado(EstadoCuestionario.COMPLETO)
			.fechaHoraInicio(ahora).fechaHoraFin(ahora.plusHours(1)).fechaHoraUltimoIngreso(ahora.plusHours(1))
			.cantidadAccesos(2).build();
		Cuestionario q2 = Cuestionario.builder()
			.idCuestionario(2).candidato(candAprobadoMayor).estado(EstadoCuestionario.COMPLETO)
			.fechaHoraInicio(ahora).fechaHoraFin(ahora.plusHours(1)).fechaHoraUltimoIngreso(ahora.plusHours(1))
			.cantidadAccesos(1).build();
		Cuestionario q3 = Cuestionario.builder()
			.idCuestionario(3).candidato(candNoAlcanza).estado(EstadoCuestionario.COMPLETO)
			.fechaHoraInicio(ahora).fechaHoraFin(ahora.plusHours(1)).fechaHoraUltimoIngreso(ahora.plusHours(1))
			.cantidadAccesos(3).build();
		Cuestionario q4 = Cuestionario.builder()
			.idCuestionario(4).candidato(candIncompleto).estado(EstadoCuestionario.INCOMPLETO)
			.fechaHoraInicio(ahora).fechaHoraFin(null).fechaHoraUltimoIngreso(ahora.plusMinutes(30))
			.cantidadAccesos(2).build();

		when(puestoDAO.findById(10)).thenReturn(puesto);
		when(cuestionarioDAO.listarPorPuestoYEvaluacion(10, null)).thenReturn(List.of(q4, q3, q1, q2));
		when(resultadoCompetenciaDAO.listarPorCuestionarios(List.of(3, 1, 2))).thenReturn(List.of(
			// q1: cumple ambas (7.5 y 7.5 -> prom 7.5)
			ResultadoCompetencia.builder().cuestionario(q1).competencia(comp1).puntajeObtenido(7.5).build(),
			ResultadoCompetencia.builder().cuestionario(q1).competencia(comp2).puntajeObtenido(7.5).build(),
			// q2: cumple ambas (9.0 y 9.0 -> prom 9.0)
			ResultadoCompetencia.builder().cuestionario(q2).competencia(comp1).puntajeObtenido(9.0).build(),
			ResultadoCompetencia.builder().cuestionario(q2).competencia(comp2).puntajeObtenido(9.0).build(),
			// q3: no cumple comp2 (9.0 y 5.0 < 6.0)
			ResultadoCompetencia.builder().cuestionario(q3).competencia(comp1).puntajeObtenido(9.0).build(),
			ResultadoCompetencia.builder().cuestionario(q3).competencia(comp2).puntajeObtenido(5.0).build()
		));

		List<CandidatoOrdenMeritoDTO> orden = gestorReportes.generarOrdenMerito(10, null);

		assertThat(orden).hasSize(4);
		assertThat(orden.get(0).getApellido()).isEqualTo("LOPEZ");
		assertThat(orden.get(0).getCumplePonderacionMinima()).isTrue();
		assertThat(orden.get(0).getPuntajeObtenido()).isEqualTo(9.0);

		assertThat(orden.get(1).getApellido()).isEqualTo("SANCHEZ");
		assertThat(orden.get(1).getCumplePonderacionMinima()).isTrue();
		assertThat(orden.get(1).getPuntajeObtenido()).isEqualTo(7.5);

		assertThat(orden.get(2).getApellido()).isEqualTo("GOMEZ");
		assertThat(orden.get(2).getEstadoCuestionario()).isEqualTo(EstadoCuestionario.COMPLETO);
		assertThat(orden.get(2).getCumplePonderacionMinima()).isFalse();

		assertThat(orden.get(3).getApellido()).isEqualTo("RODRIGUEZ");
		assertThat(orden.get(3).getEstadoCuestionario()).isEqualTo(EstadoCuestionario.INCOMPLETO);
		assertThat(orden.get(3).getPuntajeObtenido()).isNull();
		assertThat(orden.get(3).getFechaFin()).isNull();
	}

	@Test
	void cu028_puestoSinEvaluacionesDevuelveListaVacia() {
		Puesto puesto = new Puesto(10, "P001", "ANALISTA", "Desc", "EMPRESA", true, List.of());
		when(puestoDAO.findById(10)).thenReturn(puesto);
		when(evaluacionDAO.listarPorPuesto(10)).thenReturn(List.of());

		assertThat(gestorReportes.listarEvaluaciones(10)).isEmpty();
		verifyNoInteractions(cuestionarioDAO);
	}

	@Test
	void cu028_soloIncluyeCuestionariosDeEvaluacionesSeleccionadasYAceptaElMinimoExacto() {
		Competencia competencia = new Competencia("LID", "LIDERAZGO", "Desc", true, List.of());
		Puesto puesto = new Puesto(10, "P001", "ANALISTA", "Desc", "EMPRESA", true, List.of());
		puesto.setCaracteristicas(List.of(new CaracteristicaPuesto(1, 7.0, puesto, competencia)));
		Candidato candidato = Candidato.builder()
			.tipoDocumento(TipoDocumento.DNI).nroDocumento("123").apellido("PEREZ").nombre("ANA").build();
		Cuestionario cuestionario = Cuestionario.builder()
			.idCuestionario(1).candidato(candidato).estado(EstadoCuestionario.COMPLETO).build();
		when(puestoDAO.findById(10)).thenReturn(puesto);
		when(cuestionarioDAO.listarPorPuestoYEvaluacion(10, 100)).thenReturn(List.of(cuestionario));
		when(cuestionarioDAO.listarPorPuestoYEvaluacion(10, 101)).thenReturn(List.of());
		when(resultadoCompetenciaDAO.listarPorCuestionarios(List.of(1))).thenReturn(List.of(
			ResultadoCompetencia.builder().cuestionario(cuestionario).competencia(competencia)
				.puntajeObtenido(7.0).build()
		));

		assertThat(gestorReportes.generarOrdenMerito(10, List.of(100, 101, 100)))
			.singleElement().satisfies(dto -> {
				assertThat(dto.getNroDocumento()).isEqualTo("123");
				assertThat(dto.getCumplePonderacionMinima()).isTrue();
				assertThat(dto.getPuntajeObtenido()).isEqualTo(7.0);
			});
	}

	@Test
	void metodosPendientes_arrojanUnsupportedOperationException() {
		assertThatThrownBy(() -> gestorReportes.generarReporteComparativo(1, 1))
			.isInstanceOf(UnsupportedOperationException.class);
		assertThatThrownBy(() -> gestorReportes.exportarResultadosCSV(1, "/tmp"))
			.isInstanceOf(UnsupportedOperationException.class);
	}
}
