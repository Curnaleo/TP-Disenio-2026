package com.grupo30.capitalhumano.evaluaciones.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import com.grupo30.capitalhumano.evaluaciones.controller.EvaluacionController;
import com.grupo30.capitalhumano.evaluaciones.dao.IBloquePreguntasDAO;
import com.grupo30.capitalhumano.evaluaciones.dao.ICandidatoDAO;
import com.grupo30.capitalhumano.evaluaciones.dao.IConfiguracionSistemaDAO;
import com.grupo30.capitalhumano.evaluaciones.dao.ICuestionarioDAO;
import com.grupo30.capitalhumano.evaluaciones.dao.IEvaluacionDAO;
import com.grupo30.capitalhumano.evaluaciones.dao.IFactorDAO;
import com.grupo30.capitalhumano.evaluaciones.dao.IPreguntaCuestionarioDAO;
import com.grupo30.capitalhumano.evaluaciones.dao.IPreguntaDAO;
import com.grupo30.capitalhumano.evaluaciones.dao.IPuestoDAO;
import com.grupo30.capitalhumano.evaluaciones.dao.IUsuarioDAO;
import com.grupo30.capitalhumano.evaluaciones.dto.CandidatoGestionDTO;
import com.grupo30.capitalhumano.evaluaciones.entity.Candidato;
import com.grupo30.capitalhumano.evaluaciones.entity.CaracteristicaPuesto;
import com.grupo30.capitalhumano.evaluaciones.entity.Competencia;
import com.grupo30.capitalhumano.evaluaciones.entity.ConfiguracionSistema;
import com.grupo30.capitalhumano.evaluaciones.entity.Cuestionario;
import com.grupo30.capitalhumano.evaluaciones.entity.EstadoCuestionario;
import com.grupo30.capitalhumano.evaluaciones.entity.Evaluacion;
import com.grupo30.capitalhumano.evaluaciones.entity.Factor;
import com.grupo30.capitalhumano.evaluaciones.entity.Pregunta;
import com.grupo30.capitalhumano.evaluaciones.entity.PreguntaCuestionario;
import com.grupo30.capitalhumano.evaluaciones.entity.Puesto;
import com.grupo30.capitalhumano.evaluaciones.entity.TipoDocumento;
import com.grupo30.capitalhumano.evaluaciones.entity.Usuario;
import com.grupo30.capitalhumano.evaluaciones.service.GestorEvaluaciones.CandidatoConClave;

@ExtendWith(MockitoExtension.class)
class GestorEvaluacionesTest {

    @Mock private ICandidatoDAO candidatos;
    @Mock private IPuestoDAO puestos;
    @Mock private IEvaluacionDAO evaluaciones;
    @Mock private ICuestionarioDAO cuestionarios;
    @Mock private IFactorDAO factores;
    @Mock private IPreguntaDAO preguntas;
    @Mock private IUsuarioDAO usuarios;
    @Mock private IBloquePreguntasDAO bloques;
    @Mock private IPreguntaCuestionarioDAO preguntasCuestionario;
    @Mock private IConfiguracionSistemaDAO configuracion;
    private GestorEvaluaciones gestor;
    private GestorCuestionario gestorCuestionario;

    @BeforeEach
    void prepararGestores() {
        gestorCuestionario = new GestorCuestionario(cuestionarios, bloques, preguntasCuestionario,
            configuracion, null, null, null);
        gestor = new GestorEvaluaciones(candidatos, puestos, evaluaciones, cuestionarios, usuarios,
            factores, preguntas, gestorCuestionario);
    }

    @Test
    void cu025_busquedaDevuelveDatosIdentificatoriosDelCandidato() {
        Candidato candidato = new Candidato();
        candidato.setNroCandidato(42);
        candidato.setApellido("PEREZ");
        candidato.setNombre("ANA");
        candidato.setTipoDocumento(TipoDocumento.DNI);
        candidato.setNroDocumento("30123456");
        candidato.setEmail("ana@example.test");
        when(candidatos.buscar("PER", "ANA", 42)).thenReturn(List.of(candidato));

        assertThat(gestor.buscarCandidatos("PER", "ANA", 42)).singleElement().satisfies(dto -> {
            assertThat(dto.getNroCandidato()).isEqualTo(42);
            assertThat(dto.getApellido()).isEqualTo("PEREZ");
            assertThat(dto.getNombre()).isEqualTo("ANA");
            assertThat(dto.getTipoDocumento()).isEqualTo(TipoDocumento.DNI);
            assertThat(dto.getNroDocumento()).isEqualTo("30123456");
            assertThat(dto.getEmail()).isEqualTo("ana@example.test");
        });
    }

    @Test
    void cu025_busquedaSinCoincidenciasDevuelveListaVacia() {
        when(candidatos.buscar("INEXISTENTE", null, null)).thenReturn(List.of());

        assertThat(gestor.buscarCandidatos("INEXISTENTE", null, null)).isEmpty();
    }

    @Test
    void cu025_informaSoloLosSeleccionadosQueYaTienenCuestionarioActivo() {
        CandidatoGestionDTO sinActivo = CandidatoGestionDTO.builder().nroCandidato(41).build();
        CandidatoGestionDTO conActivo = CandidatoGestionDTO.builder().nroCandidato(42).build();
        when(cuestionarios.buscarActivoPorCandidato(41)).thenReturn(Optional.empty());
        when(cuestionarios.buscarActivoPorCandidato(42)).thenReturn(Optional.of(new Cuestionario()));

        assertThat(gestor.validarCandidatosActivos(List.of(sinActivo, conActivo))).containsExactly(conActivo);
        verifyNoInteractions(puestos, evaluaciones);
    }

    @Test
    void cu025_confirmaConLasClavesMostradasYDosPreguntasDelFactorPorCandidato() {
        Candidato candidato = new Candidato();
        candidato.setNroCandidato(42);
        CandidatoGestionDTO seleccion = CandidatoGestionDTO.builder().nroCandidato(42).build();
        Candidato segundoCandidato = new Candidato();
        segundoCandidato.setNroCandidato(43);
        CandidatoGestionDTO segundaSeleccion = CandidatoGestionDTO.builder().nroCandidato(43).build();
        Usuario consultor = new Usuario();
        consultor.setIdUsuario(7);
        Competencia competencia = new Competencia();
        competencia.setCodigo("C01");
        CaracteristicaPuesto caracteristica = new CaracteristicaPuesto();
        caracteristica.setCompetencia(competencia);
        Puesto puesto = new Puesto();
        puesto.getCaracteristicas().add(caracteristica);
        Factor sinPreguntas = new Factor();
        sinPreguntas.setCodigo("F01");
        Factor evaluable = new Factor();
        evaluable.setCodigo("F02");
        List<Pregunta> disponibles = List.of(new Pregunta(), new Pregunta(), new Pregunta());
        evaluable.getPreguntas().addAll(disponibles);
        competencia.getFactores().addAll(List.of(sinPreguntas, evaluable));
        ConfiguracionSistema cantidadPorBloque = new ConfiguracionSistema();
        cantidadPorBloque.setValor("1");

        when(usuarios.buscarPorUser("consultor")).thenReturn(Optional.of(consultor));
        when(puestos.findById(1)).thenReturn(puesto);
        when(candidatos.findById(42)).thenReturn(Optional.of(candidato));
        when(candidatos.findById(43)).thenReturn(Optional.of(segundoCandidato));
        when(factores.buscarPorCompetencia("C01")).thenReturn(List.of(sinPreguntas, evaluable));
        when(preguntas.buscarActivasPorFactor("F01")).thenReturn(List.of());
        when(preguntas.buscarActivasPorFactor("F02")).thenReturn(disponibles);
        when(configuracion.obtener("CANTIDAD_PREGUNTAS_BLOQUE")).thenReturn(Optional.of(cantidadPorBloque));
        doAnswer(invocacion -> {
            Evaluacion evaluacion = invocacion.getArgument(0);
            evaluacion.setId(17);
            return null;
        }).when(evaluaciones).guardar(any());

        SecurityContextHolder.getContext().setAuthentication(
            new UsernamePasswordAuthenticationToken("consultor", null));
        try {
            var vista = gestor.prepararEvaluacion(1, List.of(seleccion, segundaSeleccion));
            String clave = vista.candidatos().getFirst().clave();
            assertThat(clave).matches("(?=.*[A-Z])(?=.*[0-9])[A-Z0-9]{8}");
            verifyNoInteractions(evaluaciones, bloques, preguntasCuestionario);

            assertThat(gestor.confirmarEvaluacion(1, vista.id())).isEqualTo(17);

            var guardado = ArgumentCaptor.forClass(Cuestionario.class);
            verify(cuestionarios, times(2)).guardar(guardado.capture());
            List<Cuestionario> guardados = guardado.getAllValues();
            assertThat(guardados).extracting(Cuestionario::getClave)
                .containsExactlyElementsOf(vista.candidatos().stream().map(CandidatoConClave::clave).toList());
            assertThat(guardados).allSatisfy(cuestionario -> {
                assertThat(cuestionario.getEstado()).isEqualTo(EstadoCuestionario.ACTIVO);
                assertThat(cuestionario.getFechaHoraInicio()).isNull();
            });

            var asignada = ArgumentCaptor.forClass(PreguntaCuestionario.class);
            verify(preguntasCuestionario, times(4)).guardar(asignada.capture());
            List<PreguntaCuestionario> asignaciones = asignada.getAllValues();
            List<PreguntaCuestionario> primerModelo = asignaciones.subList(0, 2);
            List<PreguntaCuestionario> segundoModelo = asignaciones.subList(2, 4);
            for (var modelo : List.of(primerModelo, segundoModelo)) {
                assertThat(modelo).extracting(PreguntaCuestionario::getPregunta)
                    .hasSize(2).doesNotHaveDuplicates().isSubsetOf(disponibles);
            }
            for (int i = 0; i < primerModelo.size(); i++) {
                var primera = primerModelo.get(i);
                var segunda = segundoModelo.get(i);
                assertThat(primera.getBloque().getNroBloque()).isEqualTo(i + 1);
                assertThat(segunda.getBloque().getNroBloque()).isEqualTo(i + 1);
                assertThat(primera.getBloque().getCuestionario()).isSameAs(guardados.get(0));
                assertThat(segunda.getBloque().getCuestionario()).isSameAs(guardados.get(1));
            }
            assertThatThrownBy(() -> gestor.confirmarEvaluacion(1, vista.id()))
                .isInstanceOf(IllegalArgumentException.class);

            Cuestionario cuestionario = guardados.getFirst();
            cuestionario.setIdCuestionario(1);
            ConfiguracionSistema plazo = new ConfiguracionSistema();
            plazo.setValor("7");
            ConfiguracionSistema limite = new ConfiguracionSistema();
            limite.setValor("5");
            when(configuracion.obtener("DIAS_MAXIMO_CUESTIONARIO_ACTIVO")).thenReturn(Optional.of(plazo));
            when(configuracion.obtener("MINUTOS_MAXIMO_CUESTIONARIO")).thenReturn(Optional.of(limite));
            when(configuracion.obtener("MAXIMO_ACCESOS_CUESTIONARIO")).thenReturn(Optional.of(limite));
            when(cuestionarios.findById(1)).thenReturn(Optional.of(cuestionario));
            when(bloques.recuperarBloquesPorCuestionario(1))
                .thenReturn(primerModelo.stream().map(PreguntaCuestionario::getBloque).toList());

            assertThat(gestorCuestionario.iniciarCuestionario(1)).isEqualTo(1);

            assertThat(cuestionario.getEstado()).isEqualTo(EstadoCuestionario.EN_PROCESO);
            assertThat(cuestionario.getFechaHoraInicio()).isNotNull();
            assertThat(cuestionario.getCantidadAccesos()).isEqualTo(1);
            primerModelo.getFirst().getBloque().setCompletado(true);
            assertThat(gestorCuestionario.iniciarCuestionario(1)).isEqualTo(2);
            verify(bloques, times(4)).guardar(any());
            verify(configuracion).obtener("CANTIDAD_PREGUNTAS_BLOQUE");
        } finally {
            SecurityContextHolder.clearContext();
        }
    }

    @Test
    void cu025_exportaClavesGuardadasSoloAlConsultorDeLaEvaluacion() throws IOException {
        Usuario titular = new Usuario();
        titular.setIdUsuario(7);
        Usuario otroConsultor = new Usuario();
        otroConsultor.setIdUsuario(8);
        Puesto puesto = new Puesto();
        puesto.setNombre("Analista de Gestión");
        Candidato candidato = new Candidato();
        candidato.setApellido("PÉREZ");
        candidato.setNombre("ANA");
        candidato.setTipoDocumento(TipoDocumento.DNI);
        candidato.setNroDocumento("00123456");
        Evaluacion evaluacion = new Evaluacion();
        evaluacion.setUsuario(titular);
        evaluacion.setPuesto(puesto);
        evaluacion.getCuestionarios().add(Cuestionario.builder()
            .candidato(candidato).clave("ABCD1234").build());
        when(evaluaciones.findById(17)).thenReturn(Optional.of(evaluacion));
        when(usuarios.buscarPorUser("titular")).thenReturn(Optional.of(titular));
        when(usuarios.buscarPorUser("otro")).thenReturn(Optional.of(otroConsultor));
        var controller = new EvaluacionController(gestor);

        try {
            SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken("titular", null));
            var respuesta = controller.exportarClavesExcel(17);
            assertThat(respuesta.getStatusCode()).isEqualTo(HttpStatus.OK);
            String disposicion = respuesta.getHeaders().getFirst(HttpHeaders.CONTENT_DISPOSITION);
            assertThat(disposicion).contains("filename*=UTF-8''");
            assertThat(ContentDisposition.parse(disposicion).getFilename())
                .matches("\\d{4}(?:-\\d{2}){4}-Analista de Gestión\\.xls");
            assertThat(respuesta.getHeaders().getCacheControl()).isEqualTo("no-store");
            try (var libro = new HSSFWorkbook(new ByteArrayInputStream(respuesta.getBody()))) {
                var hoja = libro.getSheetAt(0);
                assertThat(hoja.getPhysicalNumberOfRows()).isEqualTo(2);
                var fila = hoja.getRow(1);
                assertThat(fila.getCell(0).getStringCellValue()).isEqualTo("PÉREZ");
                assertThat(fila.getCell(1).getStringCellValue()).isEqualTo("ANA");
                assertThat(fila.getCell(2).getStringCellValue()).isEqualTo("DNI");
                assertThat(fila.getCell(3).getStringCellValue()).isEqualTo("00123456");
                assertThat(fila.getCell(4).getStringCellValue()).isEqualTo("ABCD1234");
            }
            assertThat(controller.exportarClavesExcel(17).getStatusCode()).isEqualTo(HttpStatus.OK);

            SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken("otro", null));
            assertThat(controller.exportarClavesExcel(17).getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
            verify(evaluaciones, never()).guardar(any());
        } finally {
            SecurityContextHolder.clearContext();
        }
    }
}
