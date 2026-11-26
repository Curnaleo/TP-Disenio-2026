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
        validarTexto(nombre, "El nombre de la escala", 100);
        validarTexto(opcionDTO.getDescripcion(), "La descripción de la escala", 255);
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
        validarTexto(opcionDTO.getDescripcion(), "La descripción de la escala", 255);
        List<ItemOpcionRespuestaDTO> itemsDTO = opcionDTO.getItems();
        if (itemsDTO == null) {
            validarItems(convertirItems(opcion.getItems()));
        } else {
            validarItems(itemsDTO);
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
            itemsDTO.add(new ItemOpcionRespuestaDTO(item.getOrden(), item.getDescripcion()));
        }
        itemsDTO.sort(Comparator.comparing(ItemOpcionRespuestaDTO::getOrden));
        return itemsDTO;
    }

    private void validarTexto(String valor, String campo, int maximo) {
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException("Campo obligatorio: " + campo);
        }
        if (valor.length() > maximo) {
            throw new IllegalArgumentException(campo + " admite hasta " + maximo + " caracteres");
        }
    }

    private void validarItems(List<ItemOpcionRespuestaDTO> items) {
        // Una escala debe tener al menos dos ítems.
        if (items == null || items.size() < 2) {
            throw new IllegalArgumentException("La escala debe tener al menos dos ítems");
        }

        Set<Integer> ordenes = new HashSet<>();
        Set<String> descripciones = new HashSet<>();
        for (ItemOpcionRespuestaDTO item : items) {
            // Cada ítem debe tener un orden y una descripción.
            if (item == null || item.getOrden() == null) {
                throw new IllegalArgumentException("Cada ítem de la escala debe tener un orden");
            }
            validarTexto(item.getDescripcion(), "La descripción del ítem " + item.getOrden(), 100);

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

        Map<Integer, String> descripcionPorOrden = new HashMap<>();
        for (ItemOpcionRespuestaDTO itemDTO : recibidos) {
            descripcionPorOrden.put(itemDTO.getOrden(), itemDTO.getDescripcion());
        }

        // El orden identifica el ítem dentro de la escala y se conserva durante la edición.
        for (ItemOpcionRespuesta item : existentes) {
            if (!descripcionPorOrden.containsKey(item.getOrden())) {
                throw new IllegalArgumentException("Los órdenes enviados deben coincidir con los de la escala");
            }
        }

        for (ItemOpcionRespuesta item : existentes) {
            item.setDescripcion(descripcionPorOrden.get(item.getOrden()));
        }
    }
}
