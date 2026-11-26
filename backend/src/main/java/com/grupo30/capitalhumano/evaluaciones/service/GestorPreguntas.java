package com.grupo30.capitalhumano.evaluaciones.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.grupo30.capitalhumano.evaluaciones.dao.ICompetenciaDAO;
import com.grupo30.capitalhumano.evaluaciones.dao.IFactorDAO;
import com.grupo30.capitalhumano.evaluaciones.dao.IOpcionRespuestaDAO;
import com.grupo30.capitalhumano.evaluaciones.dao.IPreguntaDAO;
import com.grupo30.capitalhumano.evaluaciones.dto.OpcionPreguntaDTO;
import com.grupo30.capitalhumano.evaluaciones.dto.PreguntaDTO;
import com.grupo30.capitalhumano.evaluaciones.entity.Competencia;
import com.grupo30.capitalhumano.evaluaciones.entity.Factor;
import com.grupo30.capitalhumano.evaluaciones.entity.ItemOpcionRespuesta;
import com.grupo30.capitalhumano.evaluaciones.entity.OpcionPregunta;
import com.grupo30.capitalhumano.evaluaciones.entity.OpcionRespuesta;
import com.grupo30.capitalhumano.evaluaciones.entity.Pregunta;
import com.grupo30.capitalhumano.evaluaciones.exception.PreguntaDuplicadaException;

import lombok.RequiredArgsConstructor;

@Service 
@RequiredArgsConstructor
public class GestorPreguntas {
    private final IPreguntaDAO repositorio;
    private final IFactorDAO factorDAO;
    private final ICompetenciaDAO competenciaDAO;
    private final IOpcionRespuestaDAO opcionRespuestaDAO;
    private final AuditoriaBajas auditoria;
    
    @Transactional
    public List<PreguntaDTO> buscarPreguntas(String competencia, String factor, String nombre) {
        // El CU020 requiere al menos un criterio para buscar.
        if (!StringUtils.hasText(competencia) && !StringUtils.hasText(factor) && !StringUtils.hasText(nombre)) {
            throw new IllegalArgumentException("Ingresá al menos un criterio de búsqueda");
        }

        List<Pregunta> preguntas = repositorio.buscarPreguntas(competencia, factor, nombre);

        List<PreguntaDTO> preguntasDTO = new ArrayList<>();
        for (Pregunta p : preguntas) {
            preguntasDTO.add(new PreguntaDTO(
                p.getId(),
                p.getNombre(),
                p.getTextoPregunta(),
                p.getDescripcion(),
                p.getActivo(),
                p.getFactor().getCompetencia().getNombre(),
                p.getFactor().getCompetencia().getCodigo(),
                p.getFactor().getNombre(),
                p.getFactor().getCodigo(),
                p.getOpcionRespuesta().getId(),
                p.getOpcionRespuesta().getNombre(),
                p.getOpcionesPregunta().stream()
                    .map(op -> new OpcionPreguntaDTO(
                        op.getId(),
                        op.getItemOpcionRespuesta().getOrden(),
                        op.getItemOpcionRespuesta().getDescripcion(),
                        op.getPonderacion()
                    ))
                    .sorted(Comparator.comparing(OpcionPreguntaDTO::getOrden))
                    .toList()
            ));
        }

        return preguntasDTO;
    }
    
    @Transactional
    public void altaPregunta(PreguntaDTO preguntaDTO) throws PreguntaDuplicadaException {
        validarDatosPregunta(preguntaDTO, true);

        // La escala seleccionada debe existir y estar activa.
        OpcionRespuesta opcionRespuesta = opcionRespuestaDAO.findById(preguntaDTO.getIdOpcionRespuesta())
            .orElseThrow(() -> new IllegalArgumentException(
                "No existe la opción de respuesta con ID " + preguntaDTO.getIdOpcionRespuesta()
            ));

        if (!opcionRespuesta.getActivo()) {
            throw new IllegalArgumentException("La escala seleccionada está inactiva");
        }

        // El factor debe pertenecer a la competencia seleccionada.
        String codigoFactor = preguntaDTO.getCodigoFactor();
        Factor factor = factorDAO.findByCodigo(codigoFactor)
            .orElseThrow(() -> new IllegalArgumentException(
                "No existe el factor con código " + codigoFactor
            ));

        String codigoCompetencia = preguntaDTO.getCodigoCompetencia();
        Competencia competencia = competenciaDAO.findByCodigo(codigoCompetencia)
            .orElseThrow(() -> new IllegalArgumentException(
                "No existe la competencia con código " + preguntaDTO.getCodigoCompetencia()
            ));

        if (!(competencia.getCodigo().equals(factor.getCompetencia().getCodigo()))) {
            throw new IllegalArgumentException(
                "El factor con código " + codigoFactor
                    + " no pertenece a la competencia con código " + preguntaDTO.getCodigoCompetencia()
            );
        }

        validarDuplicado(preguntaDTO, factor, null);

        // La base de datos genera el ID de la pregunta.
        Pregunta nuevaPregunta = new Pregunta(
            null,
            preguntaDTO.getNombre(),
            preguntaDTO.getTextoPregunta(),
            preguntaDTO.getDescripcion(),
            preguntaDTO.getActivo() == null ? true : preguntaDTO.getActivo(),
            factor,
            opcionRespuesta,
            null
        );

        nuevaPregunta.setOpcionesPregunta(
            armarOpciones(nuevaPregunta, opcionRespuesta, preguntaDTO.getOpcionesPregunta())
        );
        repositorio.guardar(nuevaPregunta);
    }

    @Transactional
    public void modificarPregunta(PreguntaDTO preguntaDTO) {
        validarDatosPregunta(preguntaDTO, false);

        // La pregunta a modificar debe existir.
        Pregunta pregunta = repositorio.findById(preguntaDTO.getIdPregunta())
            .orElseThrow(() -> new IllegalArgumentException(
                "No existe la pregunta con ID " + preguntaDTO.getIdPregunta()
            ));

        // El CU22 deja el factor y la competencia en solo lectura.
        Factor factor = pregunta.getFactor();
        if ((preguntaDTO.getCodigoFactor() != null && !factor.getCodigo().equals(preguntaDTO.getCodigoFactor()))
                || (preguntaDTO.getCodigoCompetencia() != null
                    && !factor.getCompetencia().getCodigo().equals(preguntaDTO.getCodigoCompetencia()))) {
            throw new IllegalArgumentException("No se puede cambiar el factor ni la competencia de la pregunta");
        }

        // Verifica que la escala seleccionada exista y esté activa.
        OpcionRespuesta opcionRespuesta = opcionRespuestaDAO.findById(preguntaDTO.getIdOpcionRespuesta())
            .orElseThrow(() -> new IllegalArgumentException(
                "No existe la opción de respuesta con ID " + preguntaDTO.getIdOpcionRespuesta()
            ));
        
        if (!Boolean.TRUE.equals(opcionRespuesta.getActivo())) {
            throw new IllegalArgumentException("La escala seleccionada está inactiva");
        }

        // Valida duplicados y ponderaciones antes de modificar la pregunta.
        validarDuplicado(preguntaDTO, factor, pregunta.getId());

        List<OpcionPregunta> opcionesValidadas = armarOpciones(
            pregunta, opcionRespuesta, preguntaDTO.getOpcionesPregunta()
        );

        pregunta.setNombre(preguntaDTO.getNombre());
        pregunta.setTextoPregunta(preguntaDTO.getTextoPregunta());
        pregunta.setDescripcion(preguntaDTO.getDescripcion());
        pregunta.setOpcionRespuesta(opcionRespuesta);

        actualizarOpciones(pregunta.getOpcionesPregunta(), opcionesValidadas);

        repositorio.actualizar(pregunta);
    }

    @Transactional
    public void bajaPregunta(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("Falta el ID de la pregunta");
        }

        // La pregunta debe existir y seguir activa cuando se realiza la baja.
        repositorio.findById(id)
            .orElseThrow(() -> new IllegalArgumentException(
                "No existe la pregunta con ID " + id
        ));

        repositorio.eliminarLogico(id);
        auditoria.registrar("Pregunta", id);
    }

    // public PreguntaDTO generarConIA(CompetenciaDTO compDTO, FactorDTO factDTO)

    // Funciones auxiliares
    private void validarDatosPregunta(PreguntaDTO pregunta, boolean alta) {
        if (pregunta == null) {
            throw new IllegalArgumentException("Faltan los datos de la pregunta");
        }

        // Reúne los errores de los campos antes de consultar el repositorio.
        List<String> errores = new ArrayList<>();
        validarTexto(pregunta.getNombre(), "Nombre de la pregunta", 150, true, errores);
        validarTexto(pregunta.getTextoPregunta(), "Texto de la pregunta", 500, true, errores);
        validarTexto(pregunta.getDescripcion(), "Descripción", 255, false, errores);

        // La competencia y el factor se eligen en el alta. En la edición son de solo lectura.
        validarTexto(pregunta.getCodigoCompetencia(), "Código de competencia", 20, alta, errores);
        validarTexto(pregunta.getCodigoFactor(), "Código de factor", 20, alta, errores);
        if (!alta && pregunta.getIdPregunta() == null) {
            errores.add("Falta el ID de la pregunta");
        }

        // Las dos operaciones requieren una escala y sus ponderaciones.
        if (pregunta.getIdOpcionRespuesta() == null) {
            errores.add("Falta seleccionar una escala de respuesta");
        }
        if (pregunta.getOpcionesPregunta() == null || pregunta.getOpcionesPregunta().isEmpty()) {
            errores.add("Faltan las ponderaciones");
        }

        if (!errores.isEmpty()) {
            throw new IllegalArgumentException(String.join(". ", errores));
        }
    }

    private void validarTexto(String valor, String campo, int maximo, boolean obligatorio, List<String> errores) {
        if (obligatorio && (valor == null || valor.isBlank())) {
            errores.add("Falta completar " + campo);
        } else if (valor != null && valor.length() > maximo) {
            errores.add(campo + " admite hasta " + maximo + " caracteres");
        }
    }

    private void validarDuplicado(PreguntaDTO preguntaDTO, Factor factor, Integer idExcluido) {
        // Sin filtro de nombre para detectar también textos repetidos.
        List<Pregunta> coincidencias = repositorio.buscarPreguntas(
            factor.getCompetencia().getNombre(),
            factor.getNombre(),
            ""
        );

        for (Pregunta existente : coincidencias) {
            // En la modificación, la pregunta actual no cuenta como duplicada.
            if (idExcluido != null && idExcluido.equals(existente.getId())) {
                continue;
            }

            if (!factor.getCodigo().equals(existente.getFactor().getCodigo())) {
                continue;
            }
            if (existente.getNombre().equalsIgnoreCase(preguntaDTO.getNombre())) {
                throw new PreguntaDuplicadaException("nombre");
            }
            if (existente.getTextoPregunta().equalsIgnoreCase(preguntaDTO.getTextoPregunta())) {
                throw new PreguntaDuplicadaException("texto");
            }
        }
    }

    private List<OpcionPregunta> armarOpciones(
            Pregunta pregunta, OpcionRespuesta opcionRespuesta, List<OpcionPreguntaDTO> opcionesRecibidas) {
        
        List<ItemOpcionRespuesta> itemsEscala = opcionRespuesta.getItems();

        // La cantidad de pesos debe coincidir con la cantidad de ítems.
        if (opcionesRecibidas == null || opcionesRecibidas.size() != itemsEscala.size()) {
            throw new IllegalArgumentException("Debe haber una ponderación por cada ítem de la escala");
        }

        List<Integer> ordenesRecibidos = new ArrayList<>();
        List<OpcionPregunta> listaOpcionPregunta = new ArrayList<>();
        double totalPonderaciones = 0;

        for (OpcionPreguntaDTO opDTO : opcionesRecibidas) {
            // Cada opción debe indicar su orden y un peso válido.
            if (opDTO == null || opDTO.getOrden() == null || opDTO.getPonderacion() == null) {
                throw new IllegalArgumentException("Cada opción debe tener un orden y una ponderación");
            }

            double ponderacion = opDTO.getPonderacion();
            if (!Double.isFinite(ponderacion) || ponderacion < 0) {
                throw new IllegalArgumentException("Las ponderaciones deben ser números no negativos");
            }
            totalPonderaciones += ponderacion;

            // Cada orden debe aparecer una sola vez.
            Integer orden = opDTO.getOrden();
            if (ordenesRecibidos.contains(orden)) {
                throw new IllegalArgumentException("El orden " + orden + " está repetido");
            }
            ordenesRecibidos.add(orden);

            // Busca el ítem por su orden dentro de la escala elegida.
            ItemOpcionRespuesta itemReferido = null;
            for (ItemOpcionRespuesta item : itemsEscala) {
                if (orden.equals(item.getOrden())) {
                    itemReferido = item;
                    break;
                }
            }
            if (itemReferido == null) {
                throw new IllegalArgumentException(
                    "El ítem con orden " + orden + " no pertenece a la escala seleccionada"
                );
            }

            listaOpcionPregunta.add(new OpcionPregunta(
                null,
                ponderacion,
                pregunta,
                itemReferido
            ));
        }

        // Compara con 10 y tolera únicamente el redondeo de los decimales.
        if (Math.abs(totalPonderaciones - 10) > 0.000000001) {
            throw new IllegalArgumentException("Las ponderaciones deben sumar 10 puntos");
        }

        return listaOpcionPregunta;
    }

    private void actualizarOpciones(List<OpcionPregunta> actuales, List<OpcionPregunta> validadas) {
        List<OpcionPregunta> opcionesAEliminar = new ArrayList<>(actuales);

        for (OpcionPregunta validada : validadas) {
            OpcionPregunta opcionExistente = null;
            for (OpcionPregunta actual : opcionesAEliminar) {
                if (actual.getItemOpcionRespuesta().getId().equals(validada.getItemOpcionRespuesta().getId())) {
                    opcionExistente = actual;
                    break;
                }
            }

            // Si el ítem sigue en la escala, solo cambia su peso y conserva su ID.
            if (opcionExistente != null) {
                opcionExistente.setPonderacion(validada.getPonderacion());
                opcionesAEliminar.remove(opcionExistente);
            } else {
                actuales.add(validada);
            }
        }

        // Quita las opciones de los ítems que ya no están en la escala.
        actuales.removeAll(opcionesAEliminar);
    }

}
