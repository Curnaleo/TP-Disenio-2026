package com.grupo30.capitalhumano.evaluaciones.service;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.grupo30.capitalhumano.evaluaciones.dao.IOpcionRespuestaDAO;
import com.grupo30.capitalhumano.evaluaciones.dto.ItemOpcionRespuestaDTO;
import com.grupo30.capitalhumano.evaluaciones.dto.OpcionRespuestaDTO;
import com.grupo30.capitalhumano.evaluaciones.entity.ItemOpcionRespuesta;
import com.grupo30.capitalhumano.evaluaciones.entity.OpcionRespuesta;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GestorOpcionesRespuesta{
    
    private final IOpcionRespuestaDAO repositorio; // No hace falta @Autowired. Lombok genera un constructor que recibe IOpcionRespuestaDAO porque el campo es final
 
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
        if (opcionDTO.getItems() == null) {
            throw new IllegalArgumentException("La escala debe tener al menos dos ítems");
        }
        
        OpcionRespuesta opcion = new OpcionRespuesta(
            opcionDTO.getId(), 
            opcionDTO.getNombre(), 
            opcionDTO.getDescripcion(), 
            opcionDTO.getActivo(), 
            null
        );
        
        List<ItemOpcionRespuesta> listaI = new ArrayList<>();
        for (ItemOpcionRespuestaDTO itemDTO : opcionDTO.getItems()) {
            if (itemDTO == null) {
                throw new IllegalArgumentException("Cada ítem de la escala debe tener un orden");
            }
            listaI.add(new ItemOpcionRespuesta(
                null, 
                itemDTO.getOrden(), 
                itemDTO.getDescripcion(), 
                opcion
            ));
        }
        
        validarItems(listaI);
        opcion.setItems(listaI);

        repositorio.guardar(opcion);
    }
    
    @Transactional
    public void modificarOpcion(OpcionRespuestaDTO opcionDTO){
        OpcionRespuesta opcion = repositorio.findById(opcionDTO.getId())
        .orElseThrow(() -> new IllegalArgumentException(
            "No existe la opción con ID " + opcionDTO.getId()
        ));

        // La modificación conserva los ítems existentes de la escala.
        validarItems(opcion.getItems());

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

    // Funciones auxiliares
    private void validarItems(List<ItemOpcionRespuesta> items) {
        // Una escala debe tener al menos dos ítems.
        if (items == null || items.size() < 2) {
            throw new IllegalArgumentException("La escala debe tener al menos dos ítems");
        }

        // Cada ítem debe tener un orden distinto dentro de la escala.
        List<Integer> ordenes = new ArrayList<>();
        for (ItemOpcionRespuesta item : items) {
            if (item == null || item.getOrden() == null) {
                throw new IllegalArgumentException("Cada ítem de la escala debe tener un orden");
            }
            if (ordenes.contains(item.getOrden())) {
                throw new IllegalArgumentException("El orden " + item.getOrden() + " está repetido en la escala");
            }
            ordenes.add(item.getOrden());
        }
    }
}
