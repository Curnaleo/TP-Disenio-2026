package com.grupo30.capitalhumano.evaluaciones.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.grupo30.capitalhumano.evaluaciones.entity.Competencia;
import com.grupo30.capitalhumano.evaluaciones.entity.Factor;

@SpringBootTest
@Transactional
class FactorRepositoryTest {

	@Autowired
	private FactorRepository factorRepository;

	@Autowired
	private CompetenciaRepository competenciaRepository;

	@Test
	void debeGuardarYRecuperarFactor() {

		Competencia competencia = new Competencia(
			"COMP_F",
			"COMP",
			"DESC",
			true,
			new ArrayList<>()
		);

		competenciaRepository.guardar(competencia);

		Factor factor = new Factor();

		factor.setCodigo("FACTOR_TEST");
		factor.setNombre("FACTOR TEST");
		factor.setDescripcion("DESC");
		factor.setNroOrden(1);
		factor.setActivo(true);
		factor.setCompetencia(competencia);
		factorRepository.guardar(factor);

		var recuperado =
			factorRepository.findByCodigo(
				"FACTOR_TEST"
			);

		assertTrue(recuperado.isPresent());
	}

	@Test
	void debeBuscarFactores() {

		var factores =
			factorRepository.buscar(
				"COMP",
				"FACTOR"
			);

		assertNotNull(factores);
	}
}
