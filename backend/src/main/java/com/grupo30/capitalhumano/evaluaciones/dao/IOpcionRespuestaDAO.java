package com.grupo30.capitalhumano.evaluaciones.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.grupo30.capitalhumano.evaluaciones.dto.OpcionRespuestaDTO;
import com.grupo30.capitalhumano.evaluaciones.entity.OpcionRespuesta;

public interface IOpcionRespuestaDAO extends JpaRepository<OpcionRespuesta, Long> {
	List<OpcionRespuesta> buscarOpciones(String nombre);
	OpcionRespuesta findById(Integer id);
	void guardar(OpcionRespuestaDTO dto);
	void actualizar(Integer id, OpcionRespuestaDTO dto);
	void eliminarLogico(Integer id);
	boolean validarEnUso(Integer id);

	// boolean existsByNombreIgnoreCase(String nombre);
	// Optional<OpcionRespuesta> findByNombreIgnoreCase(String nombre);
	// List<OpcionRespuesta> findByNombreContainingIgnoreCase(String nombre);
	// List<OpcionRespuesta> findByActivoTrue();
}
