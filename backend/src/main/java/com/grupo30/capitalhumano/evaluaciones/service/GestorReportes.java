package com.grupo30.capitalhumano.evaluaciones.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.grupo30.capitalhumano.evaluaciones.dao.ICuestionarioDAO;
import com.grupo30.capitalhumano.evaluaciones.dao.IEvaluacionDAO;
import com.grupo30.capitalhumano.evaluaciones.dao.IPuestoDAO;
import com.grupo30.capitalhumano.evaluaciones.dao.IResultadoCompetenciaDAO;
import com.grupo30.capitalhumano.evaluaciones.dto.CandidatoOrdenMeritoDTO;
import com.grupo30.capitalhumano.evaluaciones.dto.EvaluacionDTO;
import com.grupo30.capitalhumano.evaluaciones.dto.ResultadoComparativoDTO;
import com.grupo30.capitalhumano.evaluaciones.entity.Candidato;
import com.grupo30.capitalhumano.evaluaciones.entity.CaracteristicaPuesto;
import com.grupo30.capitalhumano.evaluaciones.entity.Cuestionario;
import com.grupo30.capitalhumano.evaluaciones.entity.EstadoCuestionario;
import com.grupo30.capitalhumano.evaluaciones.entity.Evaluacion;
import com.grupo30.capitalhumano.evaluaciones.entity.Puesto;
import com.grupo30.capitalhumano.evaluaciones.entity.ResultadoCompetencia;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GestorReportes {

	private final ICuestionarioDAO cuestionarioDAO;
	private final IPuestoDAO puestoDAO;
	private final IEvaluacionDAO evaluacionDAO;
	private final IResultadoCompetenciaDAO resultadoCompetenciaDAO;

	@Transactional(readOnly = true)
	public List<EvaluacionDTO> listarEvaluaciones(int idPuesto) { 
		Puesto puesto = obtenerPuestoActivo(idPuesto);
		List<Evaluacion> evaluaciones = evaluacionDAO.listarPorPuesto(puesto.getPuestoId());

		return evaluaciones.stream()
			.map(ev -> {
				int cantidadCandidatos = cuestionarioDAO
					.listarPorPuestoYEvaluacion(puesto.getPuestoId(), ev.getId())
					.size();

				return EvaluacionDTO.builder()
					.idEvaluacion(ev.getId())
					.nombreEvaluacion(ev.getNombreEvaluacion())
					.nombrePuesto(puesto.getNombre())
					.fechaCreacion(ev.getFechaCreacion())
					.cantidadCandidatos(cantidadCandidatos)
					.build();
			})
			.toList();
	}

	// evalúa ponderaciones mínimas por competencia,
	//  ordena aprobados COMPLETO por puntaje descendente y agrupa el resto por EstadoCuestionario
	@Transactional(readOnly = true)
	public List<CandidatoOrdenMeritoDTO> generarOrdenMerito(int idPuesto, List<Integer> idEvaluaciones) {
		Puesto puesto = obtenerPuestoActivo(idPuesto);
		List<CaracteristicaPuesto> caracteristicas = puesto.getCaracteristicas();
		List<Cuestionario> cuestionarios = obtenerCuestionarios(puesto.getPuestoId(), idEvaluaciones);

		List<Integer> idsCompletos = cuestionarios.stream()
			.filter(c -> c.getEstado() == EstadoCuestionario.COMPLETO)
			.map(Cuestionario::getIdCuestionario)
			.toList();

		Map<Integer, Map<String, Double>> puntajesPorCuestionario = resultadoCompetenciaDAO
			.listarPorCuestionarios(idsCompletos)
			.stream()
			.collect(Collectors.groupingBy(
				rc -> rc.getCuestionario().getIdCuestionario(),
				Collectors.toMap(
					rc -> rc.getCompetencia().getCodigo(),
					ResultadoCompetencia::getPuntajeObtenido,
					(a, b) -> b
				)
			));

		List<CandidatoOrdenMeritoDTO> resultado = cuestionarios.stream()
			.map(c -> mapearCandidatoOrdenMerito(c, caracteristicas, puntajesPorCuestionario))
			.sorted(ordenarResultado())
			.toList();

		return resultado;
	}

	public List<ResultadoComparativoDTO> generarReporteComparativo(int idCandidato, int idPuesto) {
		throw new UnsupportedOperationException("Método pendiente de implementación.");
	}

	public void exportarResultadosCSV(int idPuesto, String carpeta) {
		throw new UnsupportedOperationException("Método pendiente de implementación.");
	}

	private Puesto obtenerPuestoActivo(int idPuesto) {
		Puesto puesto = puestoDAO.findById(idPuesto);
		if (puesto == null) {
			throw new IllegalArgumentException("No existe un puesto activo con ID " + idPuesto);
		}
		return puesto;
	}

	private List<Cuestionario> obtenerCuestionarios(int idPuesto, List<Integer> idEvaluaciones) {
		if (idEvaluaciones == null || idEvaluaciones.isEmpty()) {
			return cuestionarioDAO.listarPorPuestoYEvaluacion(idPuesto, null);
		}

		Map<Integer, Cuestionario> porId = new LinkedHashMap<>();
		for (Integer idEval : idEvaluaciones.stream().filter(Objects::nonNull).distinct().toList()) {
			for (Cuestionario c : cuestionarioDAO.listarPorPuestoYEvaluacion(idPuesto, idEval)) {
				porId.putIfAbsent(c.getIdCuestionario(), c);
			}
		}
		return new ArrayList<>(porId.values());
	}

	private CandidatoOrdenMeritoDTO mapearCandidatoOrdenMerito(
			Cuestionario cuestionario,
			List<CaracteristicaPuesto> caracteristicas,
			Map<Integer, Map<String, Double>> puntajesPorCuestionario) {

		Candidato candidato = cuestionario.getCandidato();
		boolean esCompleto = cuestionario.getEstado() == EstadoCuestionario.COMPLETO;

		Double puntajeObtenido = null;
		boolean cumpleMinimas = false;

		if (esCompleto) {
			Map<String, Double> puntajesCompetencia = puntajesPorCuestionario.getOrDefault(
				cuestionario.getIdCuestionario(),
				Map.of()
			);

			cumpleMinimas = cumplePonderacionesMinimas(caracteristicas, puntajesCompetencia);
			puntajeObtenido = calcularPuntajeTotal(caracteristicas, puntajesCompetencia);
		}

		return CandidatoOrdenMeritoDTO.builder()
			.tipoDocumento(candidato.getTipoDocumento())
			.nroDocumento(candidato.getNroDocumento())
			.apellido(candidato.getApellido())
			.nombre(candidato.getNombre())
			.puntajeObtenido(puntajeObtenido)
			.estadoCuestionario(cuestionario.getEstado())
			.cumplePonderacionMinima(cumpleMinimas)
			.fechaInicio(cuestionario.getFechaHoraInicio())
			.fechaFin(esCompleto ? cuestionario.getFechaHoraFin() : null)
			.fechaHoraUltimoIngreso(cuestionario.getFechaHoraUltimoIngreso())
			.cantidadAccesos(cuestionario.getCantidadAccesos())
			.build();
	}

	private boolean cumplePonderacionesMinimas(
			List<CaracteristicaPuesto> caracteristicas,
			Map<String, Double> puntajesCompetencia) {

		if (caracteristicas.isEmpty()) {
			return !puntajesCompetencia.isEmpty();
		}

		for (CaracteristicaPuesto cp : caracteristicas) {
			Double obtenido = puntajesCompetencia.get(cp.getCompetencia().getCodigo());
			if (obtenido == null || obtenido < cp.getPonderacionMinima()) {
				return false;
			}
		}
		return true;
	}

	private Double calcularPuntajeTotal(
			List<CaracteristicaPuesto> caracteristicas,
			Map<String, Double> puntajesCompetencia) {

		if (caracteristicas.isEmpty()) {
			if (puntajesCompetencia.isEmpty()) {
				return 0.0;
			}
			return puntajesCompetencia.values().stream()
				.mapToDouble(Double::doubleValue)
				.average()
				.orElse(0.0);
		}

		double suma = 0.0;
		for (CaracteristicaPuesto cp : caracteristicas) {
			suma += puntajesCompetencia.getOrDefault(cp.getCompetencia().getCodigo(), 0.0);
		}
		return suma / caracteristicas.size();
	}

	private Comparator<CandidatoOrdenMeritoDTO> ordenarResultado() {
		return Comparator
			.comparing((CandidatoOrdenMeritoDTO dto) -> !Boolean.TRUE.equals(dto.getCumplePonderacionMinima()))
			.thenComparing(dto -> prioridadEstado(dto.getEstadoCuestionario()))
			.thenComparing(
				dto -> Boolean.TRUE.equals(dto.getCumplePonderacionMinima()) ? dto.getPuntajeObtenido() : null,
				Comparator.nullsLast(Comparator.reverseOrder())
			)
			.thenComparing(CandidatoOrdenMeritoDTO::getApellido, String.CASE_INSENSITIVE_ORDER)
			.thenComparing(CandidatoOrdenMeritoDTO::getNombre, String.CASE_INSENSITIVE_ORDER);
	}

	private int prioridadEstado(EstadoCuestionario estado) {
		return switch (estado) {
			case COMPLETO -> 0;
			case INCOMPLETO -> 1;
			case EN_PROCESO -> 2;
			case ACTIVO -> 3;
			case SIN_CONTESTAR -> 4;
		};
	}
}
