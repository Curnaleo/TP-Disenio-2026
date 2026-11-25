package com.grupo30.capitalhumano.evaluaciones.service;

import org.springframework.stereotype.Service;

import com.grupo30.capitalhumano.evaluaciones.dao.IOpcionRespuestaDAO;
import com.grupo30.capitalhumano.evaluaciones.dao.IPreguntaDAO;
import com.grupo30.capitalhumano.evaluaciones.dto.PreguntaDTO;
import com.grupo30.capitalhumano.evaluaciones.entity.OpcionRespuesta;

import lombok.RequiredArgsConstructor;

@Service 
@RequiredArgsConstructor
public class GestorPreguntas {
    private final IPreguntaDAO repositorio;
    private final IOpcionRespuestaDAO opcionRespuestaDAO;
    
    // public List<PreguntaDTO> buscarPreguntas(String competencia, String factor, String nombre){

    //     return null;
    // } 
    
    public void altaPregunta(PreguntaDTO preguntaDTO){
        // La escala de respuesta (OpcionPregunta) y sus ítems (ItemOpcionRespuesta) ya existen..

        // findById(idOpcionRespuesta): Recupera la escala elegida para comprobar que siga activa 
        // y que el formulario haya enviado un peso para cada ítem.
        OpcionRespuesta opcionRespuesta = opcionRespuestaDAO.findById(preguntaDTO.getIdOpcionRespuesta());
        // VALIDAR QUE EXISTE?

        

    // private String nombre;
    // private String textoPregunta;
    // private String descripcion;
    // private Boolean activo;
    // private String nombreCompetencia;
    // private Integer idCompetencia;
    // private String nombreFactor;
    // private Integer idFactor;
    // private Integer idOpcionRespuesta;
    // private String nombreOpcionRespuesta;
    // private List<OpcionPreguntaDTO> opcionesPregunta;
    }
    
    // public void modificarPregunta(PreguntaDTO preguntaDTO)
    
    // public void bajaPregunta(Integer id)
    
    // public PreguntaDTO generarConIA(CompetenciaDTO compDTO, FactorDTO factDTO) 

}
