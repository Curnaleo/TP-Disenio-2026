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

    public void altaOpcion(OpcionRespuestaDTO opcionDTO) {
        
        OpcionRespuesta opcion = new OpcionRespuesta(
            opcionDTO.getId(), 
            opcionDTO.getNombre(), 
            opcionDTO.getDescripcion(), 
            opcionDTO.getActivo(), 
            null
        );
        
        List<ItemOpcionRespuesta> listaI = new ArrayList<>();
        for (ItemOpcionRespuestaDTO itemDTO : opcionDTO.getItems()) {
            listaI.add(new ItemOpcionRespuesta(
                null, 
                itemDTO.getOrden(), 
                itemDTO.getDescripcion(), 
                opcion
            ));
        }
        
        opcion.setItems(listaI);

        repositorio.guardar(opcion);
    }
    
    public void modificarOpcion(OpcionRespuestaDTO opcionDTO){
        OpcionRespuesta opcion = repositorio.findById(opcionDTO.getId())
        .orElseThrow(() -> new IllegalArgumentException(
            "No existe la opción con ID " + opcionDTO.getId()
        ));

        opcion.setNombre(opcionDTO.getNombre());
        opcion.setDescripcion(opcionDTO.getDescripcion());
        opcion.setActivo(opcionDTO.getActivo());

        repositorio.actualizar(opcion);
    }

    public void bajaOpcion(Integer id){
        OpcionRespuesta opcion = repositorio.findById(id)
        .orElseThrow(() -> new IllegalArgumentException(
            "No existe la opción con ID " + id
        ));

        repositorio.eliminarLogico(id); // Ya se checkeo que exista
    }

    public boolean validarOpcionEnUso(Integer id){
        OpcionRespuesta opcion = repositorio.findById(id)
        .orElseThrow(() -> new IllegalArgumentException(
            "No existe la opción con ID " + id
        ));

        return opcion.getActivo();
    }
}