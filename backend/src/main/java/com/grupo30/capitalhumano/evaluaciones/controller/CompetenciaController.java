package com.grupo30.capitalhumano.evaluaciones.controller;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.grupo30.capitalhumano.evaluaciones.dao.ICompetenciaDAO;
import com.grupo30.capitalhumano.evaluaciones.dao.IFactorDAO;
import com.grupo30.capitalhumano.evaluaciones.dto.CompetenciaDTO;
import com.grupo30.capitalhumano.evaluaciones.dto.FactorDTO;
import com.grupo30.capitalhumano.evaluaciones.entity.Competencia;
import com.grupo30.capitalhumano.evaluaciones.entity.Factor;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CompetenciaController {

	private final ICompetenciaDAO competenciaDAO;
	private final IFactorDAO factorDAO;

	@GetMapping("/competencias")
	@Transactional(readOnly = true)
	public List<CompetenciaDTO> listarCompetencias(
		@RequestParam(required = false) String nombre) {

		return competenciaDAO.buscar(nombre).stream()
			.map(this::mapCompetencia)
			.toList();
	}

	@GetMapping("/competencias/{codigo}/factores")
	@Transactional(readOnly = true)
	public List<FactorDTO> listarFactoresPorCompetencia(
		@PathVariable String codigo) {

		return factorDAO.buscarPorCompetencia(codigo).stream()
			.map(this::mapFactor)
			.toList();
	}

	@GetMapping("/factores")
	@Transactional(readOnly = true)
	public List<FactorDTO> listarFactores(
		@RequestParam(required = false) String competencia,
		@RequestParam(required = false) String nombre) {

		return factorDAO.buscar(competencia, nombre).stream()
			.map(this::mapFactor)
			.toList();
	}

	private CompetenciaDTO mapCompetencia(Competencia c) {
		return CompetenciaDTO.builder()
			.codigo(c.getCodigo())
			.nombre(c.getNombre())
			.descripcion(c.getDescripcion())
			.activo(c.getActivo())
			.build();
	}

	private FactorDTO mapFactor(Factor f) {
		return FactorDTO.builder()
			.codigo(f.getCodigo())
			.nombre(f.getNombre())
			.descripcion(f.getDescripcion())
			.nroOrden(f.getNroOrden())
			.activo(f.getActivo())
			.nombreCompetencia(f.getCompetencia().getNombre())
			.build();
	}
}
