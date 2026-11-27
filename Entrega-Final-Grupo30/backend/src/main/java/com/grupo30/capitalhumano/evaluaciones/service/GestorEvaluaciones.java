package com.grupo30.capitalhumano.evaluaciones.service;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.SecureRandom;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.grupo30.capitalhumano.evaluaciones.dao.ICandidatoDAO;
import com.grupo30.capitalhumano.evaluaciones.dao.ICuestionarioDAO;
import com.grupo30.capitalhumano.evaluaciones.dao.IEvaluacionDAO;
import com.grupo30.capitalhumano.evaluaciones.dao.IFactorDAO;
import com.grupo30.capitalhumano.evaluaciones.dao.IPreguntaDAO;
import com.grupo30.capitalhumano.evaluaciones.dao.IPuestoDAO;
import com.grupo30.capitalhumano.evaluaciones.dao.IUsuarioDAO;
import com.grupo30.capitalhumano.evaluaciones.dto.CandidatoGestionDTO;
import com.grupo30.capitalhumano.evaluaciones.entity.Candidato;
import com.grupo30.capitalhumano.evaluaciones.entity.CaracteristicaPuesto;
import com.grupo30.capitalhumano.evaluaciones.entity.Competencia;
import com.grupo30.capitalhumano.evaluaciones.entity.Cuestionario;
import com.grupo30.capitalhumano.evaluaciones.entity.EstadoCuestionario;
import com.grupo30.capitalhumano.evaluaciones.entity.Evaluacion;
import com.grupo30.capitalhumano.evaluaciones.entity.Factor;
import com.grupo30.capitalhumano.evaluaciones.entity.Pregunta;
import com.grupo30.capitalhumano.evaluaciones.entity.Puesto;
import com.grupo30.capitalhumano.evaluaciones.entity.Usuario;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GestorEvaluaciones {

	private final ICandidatoDAO candidatoDAO;
	private final IPuestoDAO puestoDAO;
	private final IEvaluacionDAO evaluacionDAO;
	private final ICuestionarioDAO cuestionarioDAO;
	private final IUsuarioDAO usuarioDAO;
	private final IFactorDAO factorDAO;
	private final IPreguntaDAO preguntaDAO;
	private final GestorCuestionario gestorCuestionario;

	// Una sola vista previa por consultor. La evaluación se guarda al confirmar.
	private final Map<Integer, EvaluacionPendiente> vistasPrevias = new ConcurrentHashMap<>();

	public record CandidatoConClave(CandidatoGestionDTO candidato, String clave) {}

	public record VistaPrevia(UUID id, List<CandidatoConClave> candidatos) {}

	private record EvaluacionPendiente(Integer idPuesto, Instant vence, VistaPrevia vista) {}

	private Usuario obtenerUsuarioActual() {

		String username =
			SecurityContextHolder.getContext()
				.getAuthentication()
				.getName();

		return usuarioDAO.buscarPorUser(username)
			.orElseThrow(() ->
				new IllegalArgumentException(
				"No existe el usuario autenticado"
			));
	}

	private CandidatoGestionDTO mapToDTO(Candidato candidato) {

		return CandidatoGestionDTO.builder()
			.nroCandidato(candidato.getNroCandidato())
			.apellido(candidato.getApellido())
			.nombre(candidato.getNombre())
			.tipoDocumento(candidato.getTipoDocumento())
			.nroDocumento(candidato.getNroDocumento())
			.email(candidato.getEmail())
			.build();
	}

	@Transactional(readOnly = true)
	public List<CandidatoGestionDTO> buscarCandidatos(
		String apellido,
		String nombre,
		Integer nroCandidato) {

		List<CandidatoGestionDTO> resultado 
			= new ArrayList<>();

		for (Candidato candidato :
			candidatoDAO.buscar(
				apellido,
				nombre,
				nroCandidato)) {

			resultado.add(mapToDTO(candidato));
		}

		return resultado;
	}

	@Transactional(readOnly = true)
	public List<CandidatoGestionDTO> validarCandidatosActivos(
		List<CandidatoGestionDTO> candidatos) {

		List<CandidatoGestionDTO> 
			candidatosConCuestionario =
			new ArrayList<>();

		for (CandidatoGestionDTO candidato : candidatos) {

			if (candidato == null || candidato.getNroCandidato() == null) {
				throw new IllegalArgumentException("Cada candidato debe tener un número de candidato");
			}
			cuestionarioDAO.buscarActivoPorCandidato(candidato.getNroCandidato())
				.ifPresent(c -> candidatosConCuestionario.add(candidato));
		}

		return candidatosConCuestionario;
	}

	private Puesto recuperarPuesto(Integer idPuesto) {

		Puesto puesto = puestoDAO.findById(idPuesto);

		if (puesto == null) {
			throw new IllegalArgumentException(
			"No existe el puesto con ID " + idPuesto
			);
		}

		return puesto;
	}

	@Transactional(readOnly = true)
	public void validarPuestoEvaluable(Integer idPuesto) {

		Puesto puesto = recuperarPuesto(idPuesto);

		if (puesto.getCaracteristicas() == null || puesto.getCaracteristicas().isEmpty()) {
			throw new IllegalArgumentException("El puesto seleccionado no tiene competencias asociadas.");
		}

		List<String> competenciasNoEvaluables = 
			new ArrayList<>();

		for (CaracteristicaPuesto caracteristica :
			puesto.getCaracteristicas()) {

			Competencia competencia =
				caracteristica.getCompetencia();

			boolean competenciaEvaluable = false;

			List<Factor> factores =
				factorDAO.buscarPorCompetencia(
					competencia.getCodigo()
				);

			for (Factor factor : factores) {

				List<Pregunta> preguntas =
					preguntaDAO.buscarActivasPorFactor(
					factor.getCodigo()
				);

				if (preguntas.size() >= 2) {
					competenciaEvaluable = true;
					break;
				}
			}

			if (!competenciaEvaluable) {
				competenciasNoEvaluables.add(
					competencia.getNombre()
				);
			}
		}

		if (!competenciasNoEvaluables.isEmpty()) {
			throw new IllegalArgumentException(
				"Las siguientes competencias no pueden evaluarse: "
				+ String.join(
				", ",
				competenciasNoEvaluables
				)
			);
		}
	}

	private Evaluacion crearEvaluacion(
		Puesto puesto,
		Usuario usuario) {

		Evaluacion evaluacion =
			new Evaluacion();

		evaluacion.setNombreEvaluacion(
			puesto.getNombre()
		);

		evaluacion.setDescripcion(
			"EVALUACION GENERADA PARA "
			+ puesto.getNombre()
		);

		evaluacion.setFechaCreacion(
			LocalDate.now()
		);

		evaluacion.setActivo(true);

		evaluacion.setPuesto(puesto);

		evaluacion.setUsuario(usuario);

		evaluacionDAO.guardar(evaluacion);

		return evaluacion;
	}

	private static final String ALFABETO =
		        "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
	private static final SecureRandom RANDOM =
		        new SecureRandom();

	private String generarClave() {

		while (true) {
			StringBuilder clave = new StringBuilder();
			boolean tieneLetra = false;
			boolean tieneDigito = false;
			for (int i = 0; i < 8; i++) {
				char caracter = ALFABETO.charAt(RANDOM.nextInt(ALFABETO.length()));
				clave.append(caracter);
				tieneLetra |= Character.isLetter(caracter);
				tieneDigito |= Character.isDigit(caracter);
			}
			if (tieneLetra && tieneDigito) {
				return clave.toString();
			}
		}
	}

	private Cuestionario crearCuestionario(
		Candidato candidato,
		Evaluacion evaluacion,
		String clave) {

		Cuestionario cuestionario =
			Cuestionario.builder()
				.clave(clave)
				.estado(EstadoCuestionario.ACTIVO)
				.cantidadAccesos(0)
				.candidato(candidato)
				.evaluacion(evaluacion)
				.build();

		cuestionarioDAO.guardar(cuestionario);
		return cuestionario;
	}

	@Transactional
	public void generarEvaluacion(
		Integer idPuesto,
		List<CandidatoGestionDTO> candidatos) {

		validarSeleccion(idPuesto, candidatos);
		guardarEvaluacion(idPuesto, obtenerUsuarioActual(), prepararClaves(candidatos));
	}

	@Transactional(readOnly = true)
	public VistaPrevia prepararEvaluacion(Integer idPuesto, List<CandidatoGestionDTO> candidatos) {
		validarSeleccion(idPuesto, candidatos);
		List<CandidatoConClave> preparados = prepararClaves(candidatos);
		Usuario consultor = obtenerUsuarioActual();
		Instant ahora = Instant.now();
		vistasPrevias.values().removeIf(pendiente -> !pendiente.vence().isAfter(ahora));

		VistaPrevia vista = new VistaPrevia(UUID.randomUUID(), preparados);
		vistasPrevias.put(consultor.getIdUsuario(),
			new EvaluacionPendiente(idPuesto, ahora.plusSeconds(900), vista));
		return vista;
	}

	@Transactional
	public int confirmarEvaluacion(Integer idPuesto, UUID idVista) {
		Usuario consultor = obtenerUsuarioActual();
		Integer idConsultor = consultor.getIdUsuario();
		EvaluacionPendiente pendiente = vistasPrevias.get(idConsultor);
		if (pendiente == null || !pendiente.idPuesto().equals(idPuesto)
			|| !pendiente.vista().id().equals(idVista) || !pendiente.vence().isAfter(Instant.now())) {
			throw new IllegalArgumentException("La vista previa ya no está disponible. Volvé a revisar las claves.");
		}

		List<CandidatoConClave> preparados = pendiente.vista().candidatos();
		validarSeleccion(idPuesto, preparados.stream().map(CandidatoConClave::candidato).toList());
		if (!vistasPrevias.remove(idConsultor, pendiente)) {
			throw new IllegalArgumentException("La vista previa ya no está disponible. Volvé a revisar las claves.");
		}
		return guardarEvaluacion(idPuesto, consultor, preparados).getId();
	}

	@Transactional(readOnly = true)
	public void exportarClavesExcel(int idEvaluacion, String carpeta) {
		Usuario consultor = obtenerUsuarioActual();
		Evaluacion evaluacion = evaluacionDAO.findById(idEvaluacion)
			.filter(e -> consultor.getIdUsuario().equals(e.getUsuario().getIdUsuario()))
			.orElseThrow(() -> new AccessDeniedException("No tenés acceso a esta evaluación."));

		String nombrePuesto = evaluacion.getPuesto().getNombre()
			.replaceAll("[\\\\/:*?\"<>|\\p{Cntrl}]", "_").strip();
		String fecha = LocalDateTime.now().format(DateTimeFormatter.ofPattern("uuuu-MM-dd-HH-mm"));
		Path archivo = Path.of(carpeta).resolve(fecha + "-" + nombrePuesto + ".xls");

		try (var libro = new HSSFWorkbook()) {
			var hoja = libro.createSheet("Claves de ingreso");
			escribirFila(hoja.createRow(0), "Apellido", "Nombre", "Tipo de documento",
				"Número de documento", "Clave de ingreso");

			int fila = 1;
			for (Cuestionario cuestionario : evaluacion.getCuestionarios()) {
				Candidato candidato = cuestionario.getCandidato();
				escribirFila(hoja.createRow(fila++), candidato.getApellido(), candidato.getNombre(),
					candidato.getTipoDocumento().name(), candidato.getNroDocumento(), cuestionario.getClave());
			}

			for (int columna = 0; columna < 5; columna++) {
				hoja.setColumnWidth(columna, 24 * 256);
			}
			try (var salida = Files.newOutputStream(archivo)) {
				libro.write(salida);
			}
		} catch (IOException ex) {
			throw new UncheckedIOException("No se pudo generar el archivo Excel.", ex);
		}
	}

	private void escribirFila(Row fila, String... valores) {
		for (int columna = 0; columna < valores.length; columna++) {
			fila.createCell(columna).setCellValue(valores[columna]);
		}
	}

	private void validarSeleccion(Integer idPuesto, List<CandidatoGestionDTO> candidatos) {
		if (idPuesto == null) {
			throw new IllegalArgumentException(
				"Debe seleccionar un puesto"
			);
		}

		if (candidatos == null || candidatos.isEmpty()) {
			throw new IllegalArgumentException(
				"Debe seleccionar al menos un candidato"
			);
		}

		List<CandidatoGestionDTO> candidatosInvalidos =
			validarCandidatosActivos(candidatos);

		if (!candidatosInvalidos.isEmpty()) {
			throw new IllegalArgumentException(
				"Existen candidatos con cuestionarios activos o en proceso: "
				+ candidatosInvalidos.stream()
					.map(c ->
						c.getApellido()
						+ ", "
						+ c.getNombre())
					.toList()
			);
		}

		validarPuestoEvaluable(idPuesto);
	}

	private List<CandidatoConClave> prepararClaves(List<CandidatoGestionDTO> candidatos) {
		List<CandidatoConClave> preparados = new ArrayList<>();
		Set<Integer> incluidos = new HashSet<>();
		for (CandidatoGestionDTO dto : candidatos) {
			if (incluidos.add(dto.getNroCandidato())) {
				preparados.add(new CandidatoConClave(
					mapToDTO(recuperarCandidato(dto.getNroCandidato())), generarClave()));
			}
		}
		return List.copyOf(preparados);
	}

	private Candidato recuperarCandidato(Integer nroCandidato) {
		return candidatoDAO.findById(nroCandidato)
			.orElseThrow(() -> new IllegalArgumentException("No existe el candidato " + nroCandidato));
	}

	private Evaluacion guardarEvaluacion(Integer idPuesto, Usuario usuario, List<CandidatoConClave> candidatos) {
		Puesto puesto = recuperarPuesto(idPuesto);
		Evaluacion evaluacion = crearEvaluacion(puesto, usuario);
		List<Cuestionario> cuestionarios = new ArrayList<>();
		for (CandidatoConClave preparado : candidatos) {
			Candidato candidato = recuperarCandidato(preparado.candidato().getNroCandidato());
			cuestionarios.add(crearCuestionario(candidato, evaluacion, preparado.clave()));
		}
		gestorCuestionario.prepararCuestionarios(puesto, cuestionarios);
		return evaluacion;
	}
}
