package com.grupo30.capitalhumano.evaluaciones.service;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.grupo30.capitalhumano.evaluaciones.dao.IOpcionRespuestaDAO;
import com.grupo30.capitalhumano.evaluaciones.dto.ItemOpcionRespuestaDTO;
import com.grupo30.capitalhumano.evaluaciones.dto.OpcionRespuestaDTO;
import com.grupo30.capitalhumano.evaluaciones.entity.ItemOpcionRespuesta;
import com.grupo30.capitalhumano.evaluaciones.entity.OpcionRespuesta;

@Service
// @RequiredArgsConstructor (EN ESTE CASO SE HIZO EL CONSTRUCTOR MANUAL PARA PRACTICAR :))
public class GestorOpcionesRespuesta{
    
    private final IOpcionRespuestaDAO repositorio;

    public GestorOpcionesRespuesta(IOpcionRespuestaDAO repositorio) {
        this.repositorio = repositorio;
    }
 
	public List<OpcionRespuestaDTO> buscarOpciones(String nombre){
        List<OpcionRespuestaDTO> lista_salida= new ArrayList<>();

        List<OpcionRespuesta> lista_opciones = repositorio.buscarOpciones(nombre);
        
        for (OpcionRespuesta op : lista_opciones) {   
            
            List<ItemOpcionRespuestaDTO> itemsDTO = new ArrayList<>();

            for (ItemOpcionRespuesta item : op.getItems()) {
                    itemsDTO.add(new ItemOpcionRespuestaDTO(
                        item.getOrden(),
                        item.getDescripcion()
                    ));
            }
            
            lista_salida.add(new OpcionRespuestaDTO(
                op.getId(), 
                op.getNombre(), 
                op.getDescripcion(), 
                op.getActivo(), 
                itemsDTO
            ));
        }
        return lista_salida;
    }

}
