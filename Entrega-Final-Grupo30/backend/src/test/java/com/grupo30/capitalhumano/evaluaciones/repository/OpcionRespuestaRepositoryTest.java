package com.grupo30.capitalhumano.evaluaciones.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.grupo30.capitalhumano.evaluaciones.entity.OpcionRespuesta;

@SpringBootTest
@Transactional
class OpcionRespuestaRepositoryTest {

	@Autowired
	private OpcionRespuestaRepository repository;

	@Test
	void debeGuardarYRecuperarOpcion() {

		OpcionRespuesta opcion =
			new OpcionRespuesta();

		opcion.setNombre("ESCALA TEST");
		opcion.setDescripcion("DESC");
		opcion.setActivo(true);

		repository.guardar(opcion);

		assertNotNull(opcion.getId());
	}

	@Test
	void debeBuscarOpciones() {

		var opciones =
			repository.buscarOpciones("");

		assertNotNull(opciones);
	}

	@Test
	void debeEliminarLogicamenteOpcion() {

		OpcionRespuesta opcion =
			new OpcionRespuesta();

		opcion.setNombre("BORRAR");
		opcion.setDescripcion("DESC");
		opcion.setActivo(true);

		repository.guardar(opcion);

		Integer id = opcion.getId();

		repository.eliminarLogico(id);

		var recuperada =
			repository.findById(id);

		assertTrue(recuperada.isEmpty());
	}
}
