package com.grupo30.capitalhumano.evaluaciones.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.grupo30.capitalhumano.evaluaciones.entity.Competencia;

@SpringBootTest
@Transactional
class CompetenciaRepositoryTest {

	@Autowired
	private CompetenciaRepository repository;

	@Test
	void debeGuardarYRecuperarCompetencia() {

		Competencia competencia =
			new Competencia();

		competencia.setCodigo("COMP_TEST");

		competencia.setNombre("COMPETENCIA TEST");

		competencia.setDescripcion("DESCRIPCION TEST");

		competencia.setActivo(true);

		repository.guardar(competencia);

		Optional<Competencia> recuperada =
			repository.findByCodigo("COMP_TEST");

		assertTrue(recuperada.isPresent());

		assertEquals(
			"COMPETENCIA TEST",
			recuperada.get().getNombre()
		);
	}

	@Test
	void debeBuscarPorNombre() {

		Competencia competencia = new Competencia();

		competencia.setCodigo("COMP_BUSQ");
		competencia.setNombre("JAVA AVANZADO");
		competencia.setDescripcion("TEST");
		competencia.setActivo(true);
	        
		repository.guardar(competencia);

		var resultados =
			repository.buscar("JAVA");

		assertFalse(resultados.isEmpty());
	}

	@Test
	void debeEliminarLogicamenteCompetencia() {

		Competencia competencia =
			new Competencia();

		competencia.setCodigo("COMP_DEL");

		competencia.setNombre("COMPETENCIA ELIMINAR");

		competencia.setDescripcion("TEST");

		competencia.setActivo(true);

		repository.guardar(competencia);

		repository.eliminarLogico("COMP_DEL");

		Optional<Competencia> recuperada =
			repository.findByCodigo("COMP_DEL");

		assertTrue(
			recuperada.isEmpty()
		);
	}
}
