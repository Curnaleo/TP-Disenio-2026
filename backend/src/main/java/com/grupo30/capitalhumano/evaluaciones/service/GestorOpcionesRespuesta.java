package com.grupo30.capitalhumano.evaluaciones.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.grupo30.capitalhumano.evaluaciones.dao.IOpcionRespuestaDAO;
import com.grupo30.capitalhumano.evaluaciones.dto.ItemOpcionRespuestaDTO;
import com.grupo30.capitalhumano.evaluaciones.dto.OpcionRespuestaDTO;
import com.grupo30.capitalhumano.evaluaciones.entity.ItemOpcionRespuesta;
import com.grupo30.capitalhumano.evaluaciones.entity.OpcionRespuesta;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GestorOpcionesRespuesta {

    private final IOpcionRespuestaDAO repositorio;
    private final AuditoriaBajas auditoria;

    @Transactional
    public List<OpcionRespuestaDTO> buscarOpciones(String nombre) {
        List<OpcionRespuestaDTO> opcionesDTO = new ArrayList<>();
        for (OpcionRespuesta opcion : repositorio.buscarOpciones(nombre)) {
            opcionesDTO.add(new OpcionRespuestaDTO(
                opcion.getId(),
                opcion.getNombre(),
                opcion.getDescripcion(),
                opcion.getActivo(),
                convertirItems(opcion.getItems())
            ));
        }
        return opcionesDTO;
    }

    @Transactional
    public void altaOpcion(OpcionRespuestaDTO opcionDTO) {
        // El nombre y la descripción son obligatorios y tienen un máximo de caracteres.
        if (opcionDTO == null) {
            throw new IllegalArgumentException("Faltan los datos de la escala");
        }
        String nombre = opcionDTO.getNombre();
        if (nombre != null) {
            nombre = nombre.strip().toUpperCase(Locale.ROOT);
        }
        validarCampos(nombre, opcionDTO.getDescripcion(), opcionDTO.getItems(), true);
        validarItems(opcionDTO.getItems());

        // La búsqueda del repositorio permite detectar nombres repetidos entre las escalas activas.
        for (OpcionRespuesta existente : repositorio.buscarOpciones(nombre)) {
            if (nombre.equalsIgnoreCase(existente.getNombre().strip())) {
                throw new IllegalArgumentException("Ya existe una escala con el nombre " + nombre);
            }
        }

        OpcionRespuesta opcion = new OpcionRespuesta(
            null,
            nombre,
            opcionDTO.getDescripcion(),
            opcionDTO.getActivo() == null ? true : opcionDTO.getActivo(),
            null
        );

        List<ItemOpcionRespuesta> items = new ArrayList<>();
        for (ItemOpcionRespuestaDTO itemDTO : opcionDTO.getItems()) {
            items.add(new ItemOpcionRespuesta(null, itemDTO.getOrden(), itemDTO.getDescripcion(), opcion));
        }
        opcion.setItems(items);
        repositorio.guardar(opcion);
    }

    @Transactional
    public void modificarOpcion(OpcionRespuestaDTO opcionDTO) {
        // La escala debe existir y su nombre es de solo lectura.
        if (opcionDTO == null || opcionDTO.getId() == null) {
            throw new IllegalArgumentException("Falta el ID de la escala");
        }
        OpcionRespuesta opcion = repositorio.findById(opcionDTO.getId())
            .orElseThrow(() -> new IllegalArgumentException("No existe la opción con ID " + opcionDTO.getId()));
        if (opcionDTO.getNombre() != null && !opcion.getNombre().equals(opcionDTO.getNombre())) {
            throw new IllegalArgumentException("No se puede cambiar el nombre de la escala");
        }

        // La descripción de la escala es obligatoria, incluso cuando solo se edita ese campo.
        List<ItemOpcionRespuestaDTO> itemsDTO = opcionDTO.getItems();
        if (itemsDTO == null) {
            itemsDTO = convertirItems(opcion.getItems());
        }
        validarCampos(null, opcionDTO.getDescripcion(), itemsDTO, false);
        validarItems(itemsDTO);
        if (opcionDTO.getItems() != null) {
            actualizarItems(opcion.getItems(), itemsDTO);
        }

        opcion.setDescripcion(opcionDTO.getDescripcion());
        repositorio.actualizar(opcion);
    }

    @Transactional
    public void bajaOpcion(Integer id) {
        // Se vuelve a comprobar el uso en la misma transacción que desactiva la escala.
        OpcionRespuesta opcion = repositorio.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("No existe la opción con ID " + id));
        if (repositorio.validarEnUso(id)) {
            throw new IllegalArgumentException("La escala " + opcion.getNombre() + " está en uso y no puede eliminarse");
        }

        repositorio.eliminarLogico(id);
        auditoria.registrar("OpcionRespuesta", id);
    }

    public boolean validarOpcionEnUso(Integer id) {
        repositorio.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("No existe la opción con ID " + id));
        return repositorio.validarEnUso(id);
    }

    // Funciones auxiliares
    private List<ItemOpcionRespuestaDTO> convertirItems(List<ItemOpcionRespuesta> items) {
        List<ItemOpcionRespuestaDTO> itemsDTO = new ArrayList<>();
        for (ItemOpcionRespuesta item : items) {
            itemsDTO.add(new ItemOpcionRespuestaDTO(item.getOrden(), item.getDescripcion(), null));
        }
        itemsDTO.sort(Comparator.comparing(ItemOpcionRespuestaDTO::getOrden));
        return itemsDTO;
    }

    private void validarCampos(String nombre, String descripcion, List<ItemOpcionRespuestaDTO> items, boolean alta) {
        // Se reúnen los faltantes y los textos demasiado largos en un solo mensaje.
        List<String> errores = new ArrayList<>();
        if (alta) {
            validarTexto(nombre, "Nombre de la escala", 100, errores);
        }
        validarTexto(descripcion, "Descripción de la escala", 255, errores);

        // Se indica la posición de cada ítem para identificar sus campos faltantes.
        if (items == null || items.size() < 2) {
            errores.add("La escala debe tener al menos dos ítems");
        }
        if (items != null) {
            for (int i = 0; i < items.size(); i++) {
                ItemOpcionRespuestaDTO item = items.get(i);
                String campo = " del ítem " + (i + 1);
                if (item == null || item.getOrden() == null) {
                    errores.add("Falta completar el orden" + campo);
                }
                validarTexto(item == null ? null : item.getDescripcion(), "Descripción" + campo, 100, errores);
            }
        }
        if (!errores.isEmpty()) {
            throw new IllegalArgumentException(String.join(". ", errores));
        }
    }

    private void validarTexto(String valor, String campo, int maximo, List<String> errores) {
        if (valor == null || valor.isBlank()) {
            errores.add("Falta completar " + campo);
        } else if (valor.length() > maximo) {
            errores.add(campo + " admite hasta " + maximo + " caracteres");
        }
    }

    private void validarItems(List<ItemOpcionRespuestaDTO> items) {
        Set<Integer> ordenes = new HashSet<>();
        Set<String> descripciones = new HashSet<>();
        for (ItemOpcionRespuestaDTO item : items) {
            // El orden y la descripción no se pueden repetir dentro de la escala.
            if (!ordenes.add(item.getOrden())) {
                throw new IllegalArgumentException("El orden " + item.getOrden() + " está repetido en la escala");
            }
            if (!descripciones.add(item.getDescripcion().strip().toUpperCase(Locale.ROOT))) {
                throw new IllegalArgumentException("La descripción está repetida en la escala: " + item.getDescripcion());
            }
        }

        // La cantidad de órdenes debe coincidir con el intervalo para que no haya saltos.
        if ((long) Collections.max(ordenes) - Collections.min(ordenes) + 1 != ordenes.size()) {
            throw new IllegalArgumentException("Los órdenes de los ítems deben ser consecutivos");
        }
    }

    private void actualizarItems(List<ItemOpcionRespuesta> existentes, List<ItemOpcionRespuestaDTO> recibidos) {
        // Se pueden editar los ítems existentes, sin agregar ni quitar ninguno.
        if (existentes.size() != recibidos.size()) {
            throw new IllegalArgumentException("No se pueden agregar ni quitar ítems de la escala");
        }

        // El orden original identifica el ítem cuando se cambia su posición.
        Map<Integer, ItemOpcionRespuestaDTO> porOrdenOriginal = new HashMap<>();
        for (ItemOpcionRespuestaDTO itemDTO : recibidos) {
            Integer ordenOriginal = itemDTO.getOrdenOriginal() != null
                ? itemDTO.getOrdenOriginal()
                : itemDTO.getOrden();
            if (porOrdenOriginal.put(ordenOriginal, itemDTO) != null) {
                throw new IllegalArgumentException("El orden original " + ordenOriginal + " está repetido");
            }
        }

        // Se comprueba que todos los ítems correspondan a la escala antes de modificarlos.
        for (ItemOpcionRespuesta item : existentes) {
            if (!porOrdenOriginal.containsKey(item.getOrden())) {
                throw new IllegalArgumentException("Los órdenes originales deben coincidir con los de la escala");
            }
        }

        for (ItemOpcionRespuesta item : existentes) {
            ItemOpcionRespuestaDTO itemDTO = porOrdenOriginal.get(item.getOrden());
            item.setOrden(itemDTO.getOrden());
            item.setDescripcion(itemDTO.getDescripcion());
        }
    }
}
