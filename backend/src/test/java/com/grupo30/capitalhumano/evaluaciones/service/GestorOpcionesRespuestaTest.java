package com.grupo30.capitalhumano.evaluaciones.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.grupo30.capitalhumano.evaluaciones.dao.IOpcionRespuestaDAO;
import com.grupo30.capitalhumano.evaluaciones.dto.ItemOpcionRespuestaDTO;
import com.grupo30.capitalhumano.evaluaciones.dto.OpcionRespuestaDTO;
import com.grupo30.capitalhumano.evaluaciones.entity.ItemOpcionRespuesta;
import com.grupo30.capitalhumano.evaluaciones.entity.OpcionRespuesta;

@ExtendWith(MockitoExtension.class)
class GestorOpcionesRespuestaTest {

    @Mock private IOpcionRespuestaDAO repositorio;
    @Mock private AuditoriaBajas auditoria;
    @InjectMocks private GestorOpcionesRespuesta gestor;

    @Test
    void cu024_busquedaSinNombreDevuelveEscalasConItemsOrdenados() {
        OpcionRespuesta escala = escalaExistente();
        when(repositorio.buscarOpciones("")).thenReturn(List.of(escala));

        assertThat(gestor.buscarOpciones("")).singleElement().satisfies(dto -> {
            assertThat(dto.getId()).isEqualTo(7);
            assertThat(dto.getNombre()).isEqualTo("FRECUENCIA");
            assertThat(dto.getItems()).extracting(ItemOpcionRespuestaDTO::getOrden).containsExactly(1, 2);
        });
    }

    @Test
    void cu029_altaConDosItemsGuardaEscalaActivaYAsociaLosItems() {
        gestor.altaOpcion(solicitud(" frecuencia ", List.of(item(1, "NUNCA"), item(2, "SIEMPRE"))));

        ArgumentCaptor<OpcionRespuesta> captor = ArgumentCaptor.forClass(OpcionRespuesta.class);
        verify(repositorio).guardar(captor.capture());
        OpcionRespuesta guardada = captor.getValue();
        assertThat(guardada.getNombre()).isEqualTo("FRECUENCIA");
        assertThat(guardada.getActivo()).isTrue();
        assertThat(guardada.getItems()).extracting(ItemOpcionRespuesta::getDescripcion)
                .containsExactly("NUNCA", "SIEMPRE");
        assertThat(guardada.getItems()).allSatisfy(item ->
                assertThat(item.getOpcionRespuesta()).isSameAs(guardada));
    }

    @Test
    void cu029_aceptaLasLongitudesMaximasDeLosTresCampos() {
        OpcionRespuestaDTO dto = solicitud("N".repeat(100), List.of(item(1, "I".repeat(100)), item(2, "SI")));
        dto.setDescripcion("D".repeat(255));

        gestor.altaOpcion(dto);

        ArgumentCaptor<OpcionRespuesta> guardada = ArgumentCaptor.forClass(OpcionRespuesta.class);
        verify(repositorio).guardar(guardada.capture());
        assertThat(guardada.getValue().getNombre()).hasSize(100);
        assertThat(guardada.getValue().getDescripcion()).hasSize(255);
        assertThat(guardada.getValue().getItems().getFirst().getDescripcion()).hasSize(100);
    }

    @Test
    void cu029_informaTodasLasOmisionesDelFormularioEnUnaRespuesta() {
        OpcionRespuestaDTO dto = new OpcionRespuestaDTO(null, " ", "", null,
                List.of(new ItemOpcionRespuestaDTO(null, "", null), item(2, "SI")));

        assertThatThrownBy(() -> gestor.altaOpcion(dto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContainingAll("Nombre de la escala", "Descripción de la escala",
                        "orden del ítem 1", "Descripción del ítem 1");
        verifyNoInteractions(repositorio);
    }

    @Test
    void cu030_rechazaCambiarElNombreDeSoloLectura() {
        OpcionRespuesta escala = escalaExistente();
        when(repositorio.findById(7)).thenReturn(Optional.of(escala));

        assertThatThrownBy(() -> gestor.modificarOpcion(
                new OpcionRespuestaDTO(7, "OTRA", "DESCRIPCION", true, null)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("No se puede cambiar el nombre");
        assertThat(escala.getNombre()).isEqualTo("FRECUENCIA");
        verify(repositorio, never()).actualizar(any());
    }

    @Test
    void cu029_rechazaUnSoloItem() {
        assertAltaRechazada(solicitud("FRECUENCIA", List.of(item(1, "NUNCA"))), "al menos dos ítems");
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 3})
    void cu029_rechazaOrdenRepetidoOConSaltos(int segundoOrden) {
        assertAltaRechazada(solicitud("FRECUENCIA", List.of(item(1, "NUNCA"), item(segundoOrden, "SIEMPRE"))),
                segundoOrden == 1 ? "está repetido" : "deben ser consecutivos");
    }

    @Test
    void cu029_rechazaDescripcionRepetida() {
        assertAltaRechazada(solicitud("FRECUENCIA", List.of(item(1, "NUNCA"), item(2, " nunca "))),
                "descripción está repetida");
    }

    @Test
    void cu029_rechazaNombreDuplicado() {
        when(repositorio.buscarOpciones("FRECUENCIA")).thenReturn(List.of(escalaExistente()));

        assertAltaRechazada(solicitud(" frecuencia ", List.of(item(1, "NUNCA"), item(2, "SIEMPRE"))),
                "Ya existe una escala");
    }

    @Test
    void cu030_permiteEditarYReordenarLosMismosItems() {
        OpcionRespuesta escala = escalaExistente();
        when(repositorio.findById(7)).thenReturn(Optional.of(escala));
        ItemOpcionRespuesta siempre = escala.getItems().getFirst();
        ItemOpcionRespuesta nunca = escala.getItems().getLast();

        gestor.modificarOpcion(new OpcionRespuestaDTO(7, "FRECUENCIA", "NUEVA DESCRIPCION", true,
                List.of(new ItemOpcionRespuestaDTO(2, "CASI NUNCA", 1),
                        new ItemOpcionRespuestaDTO(1, "CASI SIEMPRE", 2))));

        verify(repositorio).actualizar(escala);
        assertThat(escala.getDescripcion()).isEqualTo("NUEVA DESCRIPCION");
        assertThat(nunca.getOrden()).isEqualTo(2);
        assertThat(nunca.getDescripcion()).isEqualTo("CASI NUNCA");
        assertThat(siempre.getOrden()).isEqualTo(1);
        assertThat(siempre.getDescripcion()).isEqualTo("CASI SIEMPRE");
    }

    @Test
    void cu030_rechazaAgregarItemsSinCambiarLaEscala() {
        OpcionRespuesta escala = escalaExistente();
        when(repositorio.findById(7)).thenReturn(Optional.of(escala));

        assertThatThrownBy(() -> gestor.modificarOpcion(new OpcionRespuestaDTO(7, "FRECUENCIA", "NUEVA", true,
                List.of(item(1, "NUNCA"), item(2, "A VECES"), item(3, "SIEMPRE")))))
                .isInstanceOf(IllegalArgumentException.class).hasMessageContaining("agregar ni quitar");
        verify(repositorio, never()).actualizar(any());
        assertThat(escala.getDescripcion()).isEqualTo("ESCALA DE FRECUENCIA");
    }

    @Test
    void cu031_escalaEnUsoNoSeEliminaNiGeneraAuditoria() {
        when(repositorio.findById(7)).thenReturn(Optional.of(escalaExistente()));
        when(repositorio.validarEnUso(7)).thenReturn(true);

        assertThatThrownBy(() -> gestor.bajaOpcion(7))
                .isInstanceOf(IllegalArgumentException.class).hasMessageContaining("está en uso");
        verify(repositorio, never()).eliminarLogico(7);
        verifyNoInteractions(auditoria);
    }

    @Test
    void cu031_escalaLibreSeDaDeBajaLogicaYSeAudita() {
        when(repositorio.findById(7)).thenReturn(Optional.of(escalaExistente()));

        gestor.bajaOpcion(7);

        verify(repositorio).eliminarLogico(7);
        verify(auditoria).registrar("OpcionRespuesta", 7);
    }

    private void assertAltaRechazada(OpcionRespuestaDTO dto, String mensaje) {
        assertThatThrownBy(() -> gestor.altaOpcion(dto))
                .isInstanceOf(IllegalArgumentException.class).hasMessageContaining(mensaje);
        verify(repositorio, never()).guardar(any());
    }

    private static OpcionRespuestaDTO solicitud(String nombre, List<ItemOpcionRespuestaDTO> items) {
        return new OpcionRespuestaDTO(null, nombre, "ESCALA DE FRECUENCIA", null, items);
    }

    private static ItemOpcionRespuestaDTO item(int orden, String descripcion) {
        return new ItemOpcionRespuestaDTO(orden, descripcion, null);
    }

    private static OpcionRespuesta escalaExistente() {
        OpcionRespuesta escala = new OpcionRespuesta(7, "FRECUENCIA", "ESCALA DE FRECUENCIA", true,
                new ArrayList<>());
        escala.getItems().add(new ItemOpcionRespuesta(22, 2, "SIEMPRE", escala));
        escala.getItems().add(new ItemOpcionRespuesta(21, 1, "NUNCA", escala));
        return escala;
    }
}
