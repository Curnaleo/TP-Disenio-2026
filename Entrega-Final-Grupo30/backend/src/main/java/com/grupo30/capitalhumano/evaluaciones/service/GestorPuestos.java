package com.grupo30.capitalhumano.evaluaciones.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.grupo30.capitalhumano.evaluaciones.dao.ICuestionarioDAO;
import com.grupo30.capitalhumano.evaluaciones.dao.IPuestoDAO;
import com.grupo30.capitalhumano.evaluaciones.dto.CaracteristicaPuestoDTO;
import com.grupo30.capitalhumano.evaluaciones.dto.PuestoDTO;
import com.grupo30.capitalhumano.evaluaciones.entity.Cuestionario;
import com.grupo30.capitalhumano.evaluaciones.entity.EstadoCuestionario;
import com.grupo30.capitalhumano.evaluaciones.entity.Puesto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GestorPuestos {

	private final IPuestoDAO puestoDAO;
	private final ICuestionarioDAO cuestionarioDAO;

	@Transactional(readOnly = true)
	public List<PuestoDTO> buscarPuestos(String codigo, String nombre, String empresa) {
		boolean sinCodigo = codigo == null || codigo.isBlank();
		boolean sinNombre = nombre == null || nombre.isBlank();
		boolean sinEmpresa = empresa == null || empresa.isBlank();

		if (sinCodigo && sinNombre && sinEmpresa) {
			throw new IllegalArgumentException("Debe ingresar al menos un criterio de búsqueda.");
		}

		List<Puesto> puestos = puestoDAO.buscar(codigo, nombre, empresa);
		return puestos.stream()
			.map(this::mapearPuestoDTO)
			.toList();
	}

	public void altaPuesto(PuestoDTO puesto) {
		throw new UnsupportedOperationException("Método pendiente de implementación.");
	}

	public void modificarPuesto(PuestoDTO puesto) {
		throw new UnsupportedOperationException("Método pendiente de implementación.");
	}

	public void bajaPuesto(int id) {
		throw new UnsupportedOperationException("Método pendiente de implementación.");
	}

	public boolean validarPuestoEnUso(int id) {
		throw new UnsupportedOperationException("Método pendiente de implementación.");
	}

	private PuestoDTO mapearPuestoDTO(Puesto puesto) {
		List<Cuestionario> cuestionarios = cuestionarioDAO.listarPorPuestoYEvaluacion(
			puesto.getPuestoId(),
			null
		);

		int cantidadCandidatos = cuestionarios.size();
		int evaluacionesCompletas = (int) cuestionarios.stream()
			.filter(c -> c.getEstado() == EstadoCuestionario.COMPLETO)
			.count();

		List<CaracteristicaPuestoDTO> caracteristicas = puesto.getCaracteristicas().stream()
			.map(cp -> CaracteristicaPuestoDTO.builder()
				.codigoCompetencia(cp.getCompetencia().getCodigo())
				.nombreCompetencia(cp.getCompetencia().getNombre())
				.ponderacionMinima(cp.getPonderacionMinima())
				.build())
			.toList();

		return PuestoDTO.builder()
			.puestoId(puesto.getPuestoId())
			.codigo(puesto.getCodigo())
			.nombre(puesto.getNombre())
			.descripcion(puesto.getDescripcion())
			.empresa(puesto.getEmpresa())
			.activo(puesto.getActivo())
			.caracteristicas(caracteristicas)
			.cantidadCandidatos(cantidadCandidatos)
			.evaluacionesCompletas(evaluacionesCompletas)
			.build();
	}
}
