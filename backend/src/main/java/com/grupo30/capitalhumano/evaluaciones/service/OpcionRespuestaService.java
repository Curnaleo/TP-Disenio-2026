package com.capitalhumano.evaluaciones.service;

import java.util.List;
import com.capitalhumano.evaluaciones.dto.OpcionRespuestaDTO;

public interface OpcionRespuestaService {

	OpcionRespuestaDTO guardar(OpcionRespuestaDTO dto);

	OpcionRespuestaDTO actualizar(Long id, OpcionRespuestaDTO dto);

	OpcionRespuestaDTO findById(Long id);

	List<OpcionRespuestaDTO> buscarOpciones(String nombre);

	void eliminar(Long id);

	boolean validarEnUso(Long id);

}
