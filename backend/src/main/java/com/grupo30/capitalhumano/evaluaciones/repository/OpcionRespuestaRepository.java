package com.grupo30.capitalhumano.evaluaciones.repository;

import com.grupo30.capitalhumano.evaluaciones.entity.OpcionRespuesta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OpcionRespuestaRepository extends JpaRepository<OpcionRespuesta, Long> {

	boolean existsByNombreIgnoreCase(String nombre);

	Optional<OpcionRespuesta> findByNombreIgnoreCase(String nombre);

	List<OpcionRespuesta> findByNombreContainingIgnoreCase(String nombre);

	List<OpcionRespuesta> findByActivoTrue();

}
