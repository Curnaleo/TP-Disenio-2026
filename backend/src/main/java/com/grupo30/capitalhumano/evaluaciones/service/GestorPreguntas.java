package com.grupo30.capitalhumano.evaluaciones.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.grupo30.capitalhumano.evaluaciones.dao.IFactorDAO;
import com.grupo30.capitalhumano.evaluaciones.dao.IOpcionRespuestaDAO;
import com.grupo30.capitalhumano.evaluaciones.dao.IPreguntaDAO;
import com.grupo30.capitalhumano.evaluaciones.dto.OpcionPreguntaDTO;
import com.grupo30.capitalhumano.evaluaciones.dto.PreguntaDTO;
import com.grupo30.capitalhumano.evaluaciones.entity.Factor;
import com.grupo30.capitalhumano.evaluaciones.entity.ItemOpcionRespuesta;
import com.grupo30.capitalhumano.evaluaciones.entity.OpcionPregunta;
import com.grupo30.capitalhumano.evaluaciones.entity.OpcionRespuesta;
import com.grupo30.capitalhumano.evaluaciones.entity.Pregunta;
import com.grupo30.capitalhumano.evaluaciones.exception.PreguntaDuplicadaException;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service 
@RequiredArgsConstructor
public class GestorPreguntas {
    private final IPreguntaDAO repositorio;
    private final IFactorDAO factorDAO;
    private final IOpcionRespuestaDAO opcionRespuestaDAO;
    
    // public List<PreguntaDTO> buscarPreguntas(String competencia, String factor, String nombre){

    //     return null;
    // } 
    
    @Transactional // Si falla el guardado de una ponderación, se revierte también el alta de la pregunta.
    public void altaPregunta(PreguntaDTO preguntaDTO) throws PreguntaDuplicadaException{
        // La escala de respuesta (OpcionRespuesta) y sus ítems ya existen.

        // findById(idOpcionRespuesta): Recupera la escala elegida para comprobar que siga activa 
        // y que el formulario haya enviado un peso para cada ítem.
        OpcionRespuesta opcionRespuesta = opcionRespuestaDAO.findById(preguntaDTO.getIdOpcionRespuesta())
            .orElseThrow(() -> new IllegalArgumentException(
                "No existe la opción de respuesta con ID " + preguntaDTO.getIdOpcionRespuesta()
        ));

        // validar los demás datos recibidos:
        if (! opcionRespuesta.getActivo()) {
            throw new IllegalArgumentException("La escala seleccionada está inactiva");
        }

        List<Pregunta> coincidencias = repositorio.buscarPreguntas(
            preguntaDTO.getNombreCompetencia(),
            preguntaDTO.getNombreFactor(),
            preguntaDTO.getNombre()
        );

        for (Pregunta existente : coincidencias) {
            if (existente.getNombre().equalsIgnoreCase(preguntaDTO.getNombre())
                    && existente.getFactor().getNombre().equalsIgnoreCase(preguntaDTO.getNombreFactor())
                    && existente.getFactor().getCompetencia().getNombre()
                        .equalsIgnoreCase(preguntaDTO.getNombreCompetencia())) {
                throw new PreguntaDuplicadaException();
            }
        }

        // Crea pregunta y la vincula a la escala
        
        Factor factor= factorDAO.findById(preguntaDTO.getIdFactor())
            .orElseThrow(() -> new IllegalArgumentException(
                "No existe el factor con ID " + preguntaDTO.getIdFactor()
        ));

        Pregunta nuevaPregunta= new Pregunta(
            null, // Segun el chat se lo asigna la BDD 
            preguntaDTO.getNombre(), 
            preguntaDTO.getTextoPregunta(),
            preguntaDTO.getDescripcion(), 
            preguntaDTO.getActivo(),
            factor,
            opcionRespuesta, // escala completa (ej. Frecuencia contiene ItemsOpcionRespuesta)
            null // Ponderación relacionada con los ItemsOpcionRespuesta
        );
        
        // Crea OpciónPregunta con el item existente y su peso
        List<OpcionPreguntaDTO> opcionesRecibidas = preguntaDTO.getOpcionesPregunta();
        List<ItemOpcionRespuesta> itemsEscala = opcionRespuesta.getItems();

        if (opcionesRecibidas == null || opcionesRecibidas.size() != itemsEscala.size()) {
            throw new IllegalArgumentException("Debe haber una ponderación por cada ítem de la escala");
        }

        Set<Integer> ordenesRecibidos = new HashSet<>();
        List<OpcionPregunta> listaOpcionPregunta = new ArrayList<>();

        for (OpcionPreguntaDTO opDTO : opcionesRecibidas) {
            // VERIFICACIONES FULL PALA:
            if (opDTO == null || opDTO.getOrden() == null || opDTO.getPonderacion() == null) {
                throw new IllegalArgumentException("Cada opción debe tener un orden y una ponderación");
            }

            Integer orden = opDTO.getOrden();
            if (!ordenesRecibidos.add(orden)) {
                throw new IllegalArgumentException("El orden " + orden + " está repetido");
            }

            ItemOpcionRespuesta itemReferido = itemsEscala.stream()
                .filter(item -> orden.equals(item.getOrden()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                    "El ítem con orden " + orden + " no pertenece a la escala seleccionada"
                ));

            listaOpcionPregunta.add(new OpcionPregunta(
                null,
                opDTO.getPonderacion(),
                nuevaPregunta,
                itemReferido
            ));
        }

        // Agrega opción pregunta a la pregunta
        nuevaPregunta.setOpcionesPregunta(listaOpcionPregunta);

        // Guarda la pregunta
        repositorio.guardar(nuevaPregunta);
    }
    
    // public void modificarPregunta(PreguntaDTO preguntaDTO)
    
    // public void bajaPregunta(Integer id)
    
    // public PreguntaDTO generarConIA(CompetenciaDTO compDTO, FactorDTO factDTO) 

}
