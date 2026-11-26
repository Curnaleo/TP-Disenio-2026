package com.grupo30.capitalhumano.evaluaciones.repository;

import com.grupo30.capitalhumano.evaluaciones.dao.IOpcionRespuestaDAO;
import com.grupo30.capitalhumano.evaluaciones.entity.OpcionRespuesta;

import jakarta.persistence.EntityManager;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
@RequiredArgsConstructor
public class OpcionRespuestaRepository implements IOpcionRespuestaDAO {

	    private final EntityManager entityManager;

	        @Override
		    public List<OpcionRespuesta> buscarOpciones(String nombre) {
			            throw new UnsupportedOperationException(
						                    "Busqueda de opciones pendiente");
				        }

		    @Override
		        public Optional<OpcionRespuesta> findById(Integer id) {
				        throw new UnsupportedOperationException(
							                "Busqueda por id pendiente");
					    }

		        @Override
			    public void guardar(OpcionRespuesta opcion) {
				            throw new UnsupportedOperationException(
							                    "Alta de opcion pendiente");
					        }

			    @Override
			        public void actualizar(OpcionRespuesta opcion) {
					        throw new UnsupportedOperationException(
								                "Modificacion de opcion pendiente");
						    }

			        @Override
				    public void eliminarLogico(Integer id) {
					            throw new UnsupportedOperationException(
								                    "Baja logica pendiente");
						        }

				    @Override
				        public boolean validarEnUso(Integer id) {
						        throw new UnsupportedOperationException(
									                "Validacion de uso pendiente");
							    }
}
