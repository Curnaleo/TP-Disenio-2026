package com.grupo30.capitalhumano.evaluaciones.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.grupo30.capitalhumano.evaluaciones.dao.IBloquePreguntasDAO;
import com.grupo30.capitalhumano.evaluaciones.dao.ICuestionarioDAO;
import com.grupo30.capitalhumano.evaluaciones.dao.IPreguntaCuestionarioDAO;
import com.grupo30.capitalhumano.evaluaciones.dao.IResultadoCompetenciaDAO;
import com.grupo30.capitalhumano.evaluaciones.dao.IResultadoFactorDAO;
import com.grupo30.capitalhumano.evaluaciones.entity.BloquePreguntas;
import com.grupo30.capitalhumano.evaluaciones.entity.CaracteristicaPuesto;
import com.grupo30.capitalhumano.evaluaciones.entity.Competencia;
import com.grupo30.capitalhumano.evaluaciones.entity.Cuestionario;
import com.grupo30.capitalhumano.evaluaciones.entity.Evaluacion;
import com.grupo30.capitalhumano.evaluaciones.entity.Factor;
import com.grupo30.capitalhumano.evaluaciones.entity.OpcionPregunta;
import com.grupo30.capitalhumano.evaluaciones.entity.Pregunta;
import com.grupo30.capitalhumano.evaluaciones.entity.PreguntaCuestionario;
import com.grupo30.capitalhumano.evaluaciones.entity.Puesto;
import com.grupo30.capitalhumano.evaluaciones.entity.ResultadoCompetencia;
import com.grupo30.capitalhumano.evaluaciones.entity.ResultadoFactor;

@ExtendWith(MockitoExtension.class)
class GestorCuestionarioTest {

    @Mock private ICuestionarioDAO cuestionarios;
    @Mock private IBloquePreguntasDAO bloques;
    @Mock private IPreguntaCuestionarioDAO preguntas;
    @Mock private IResultadoFactorDAO resultadosFactor;
    @Mock private IResultadoCompetenciaDAO resultadosCompetencia;
    @InjectMocks private GestorCuestionario gestor;

    @Test
    void cu026_promediaLasDosRespuestasPorFactorYExcluyeFactoresNoEvaluadosDelPromedio() {
        Factor alcance = factor("ALC");
        Factor tiempo = factor("TIE");
        Factor costo = factor("COS");
        Competencia competencia = new Competencia("PRO", "PROYECTOS", "GESTION", true,
                List.of(alcance, tiempo, costo));
        Puesto puesto = new Puesto();
        puesto.setCaracteristicas(List.of(new CaracteristicaPuesto(1, 5.0, puesto, competencia)));
        Evaluacion evaluacion = new Evaluacion();
        evaluacion.setPuesto(puesto);
        Cuestionario cuestionario = Cuestionario.builder().idCuestionario(7).evaluacion(evaluacion).build();
        when(cuestionarios.findById(7)).thenReturn(Optional.of(cuestionario));
        when(bloques.recuperarBloquesPorCuestionario(7)).thenReturn(List.of(
                BloquePreguntas.builder().idBloque(11).build(), BloquePreguntas.builder().idBloque(12).build()));
        when(preguntas.recuperarPorBloque(11)).thenReturn(List.of(respuesta(alcance, 0), respuesta(tiempo, 7)));
        when(preguntas.recuperarPorBloque(12)).thenReturn(List.of(respuesta(alcance, 5), respuesta(tiempo, 10)));

        gestor.calcularPuntaje(7);

        var porFactor = ArgumentCaptor.forClass(ResultadoFactor.class);
        verify(resultadosFactor, times(2)).guardar(porFactor.capture());
        assertThat(porFactor.getAllValues())
                .extracting(r -> r.getFactor().getCodigo(), ResultadoFactor::getPuntajeObtenido)
                .containsExactlyInAnyOrder(tuple("ALC", 2.5), tuple("TIE", 8.5));
        var porCompetencia = ArgumentCaptor.forClass(ResultadoCompetencia.class);
        verify(resultadosCompetencia).guardar(porCompetencia.capture());
        assertThat(porCompetencia.getValue().getCompetencia()).isSameAs(competencia);
        assertThat(porCompetencia.getValue().getPuntajeObtenido()).isEqualTo(5.5);
    }

    private static Factor factor(String codigo) {
        Factor factor = new Factor();
        factor.setCodigo(codigo);
        return factor;
    }

    private static PreguntaCuestionario respuesta(Factor factor, double ponderacion) {
        Pregunta pregunta = new Pregunta();
        pregunta.setFactor(factor);
        return PreguntaCuestionario.builder().pregunta(pregunta)
                .opcionElegida(new OpcionPregunta(null, ponderacion, pregunta, null)).build();
    }
}
