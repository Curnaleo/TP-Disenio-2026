INSERT INTO usuario (nombre, activo)
VALUES ('ADMIN', true)
ON CONFLICT (nombre) DO NOTHING;

INSERT INTO usuario (nombre, activo)
VALUES ('JUAN', true)
ON CONFLICT (nombre) DO NOTHING;

-- =====================================================
-- COMPETENCIAS
-- =====================================================

INSERT INTO competencia (
	    codigo,
	    nombre,
	    descripcion,
	    activo
)
VALUES
('COMP001', 'GESTION DE PROYECTOS', 'COMPETENCIA TECNICA', true),
('COMP002', 'LEALTAD', 'COMPETENCIA ACTITUDINAL', true)
ON CONFLICT (codigo) DO NOTHING;

-- =====================================================
-- FACTORES
-- =====================================================

INSERT INTO factor (
	    codigo,
	    nombre,
	    descripcion,
	    nro_orden,
	    activo,
	    codigo_competencia
)
VALUES
(
	    'FAC001',
	    'GESTION DEL ALCANCE',
	    'GESTION DEL ALCANCE',
	    1,
	    true,
	    'COMP001'
),
(
	    'FAC002',
	    'GESTION DEL TIEMPO',
	    'GESTION DEL TIEMPO',
	    2,
	    true,
	    'COMP001'
),
(
	    'FAC003',
	    'CREDIBILIDAD',
	    'CREDIBILIDAD PROFESIONAL',
	    1,
	    true,
	    'COMP002'
),
(
	    'FAC004',
	    'COMPROMISO',
	    'COMPROMISO ORGANIZACIONAL',
	    2,
	    true,
	    'COMP002'
)
ON CONFLICT (codigo) DO NOTHING;

-- =====================================================
-- OPCIONES DE RESPUESTA
-- =====================================================

INSERT INTO opcion_respuesta (
	    nombre,
	    descripcion,
	    activo
)
VALUES
(
	    'ESCALA_5',
	    'MALO A EXCELENTE',
	    true
),
(
	    'VERDADERO_FALSO',
	    'OPCIONES BINARIAS',
	    true
);

-- =====================================================
-- ITEMS ESCALA_5
-- =====================================================

INSERT INTO item_opcion_respuesta (
	    orden,
	    descripcion,
	    id_opcion_respuesta
)
SELECT 1,'MALO',id_opcion_respuesta
FROM opcion_respuesta
WHERE nombre='ESCALA_5';

INSERT INTO item_opcion_respuesta (
	    orden,
	    descripcion,
	    id_opcion_respuesta
)
SELECT 2,'REGULAR',id_opcion_respuesta
FROM opcion_respuesta
WHERE nombre='ESCALA_5';

INSERT INTO item_opcion_respuesta (
	    orden,
	    descripcion,
	    id_opcion_respuesta
)
SELECT 3,'BUENO',id_opcion_respuesta
FROM opcion_respuesta
WHERE nombre='ESCALA_5';

INSERT INTO item_opcion_respuesta (
	    orden,
	    descripcion,
	    id_opcion_respuesta
)
SELECT 4,'MUY BUENO',id_opcion_respuesta
FROM opcion_respuesta
WHERE nombre='ESCALA_5';

INSERT INTO item_opcion_respuesta (
	    orden,
	    descripcion,
	    id_opcion_respuesta
)
SELECT 5,'EXCELENTE',id_opcion_respuesta
FROM opcion_respuesta
WHERE nombre='ESCALA_5';

-- =====================================================
-- ITEMS VERDADERO/FALSO
-- =====================================================

INSERT INTO item_opcion_respuesta (
	    orden,
	    descripcion,
	    id_opcion_respuesta
)
SELECT 1,'VERDADERO',id_opcion_respuesta
FROM opcion_respuesta
WHERE nombre='VERDADERO_FALSO';

INSERT INTO item_opcion_respuesta (
	    orden,
	    descripcion,
	    id_opcion_respuesta
)
SELECT 2,'FALSO',id_opcion_respuesta
FROM opcion_respuesta
WHERE nombre='VERDADERO_FALSO';

-- =====================================================
-- PREGUNTAS
-- =====================================================

INSERT INTO pregunta (
	    nombre,
	    texto_pregunta,
	    descripcion,
	    activo,
	    codigo_factor,
	    id_opcion_respuesta
)
SELECT
    'WBS',
    'LA DESCOMPOSICION DE ESTRUCTURA DE TRABAJO FORMA PARTE DE LA DEFINICION DEL ALCANCE?',
    'PREGUNTA DE PRUEBA',
    true,
    'FAC001',
    id_opcion_respuesta
FROM opcion_respuesta
WHERE nombre='VERDADERO_FALSO';

INSERT INTO pregunta (
	    nombre,
	    texto_pregunta,
	    descripcion,
	    activo,
	    codigo_factor,
	    id_opcion_respuesta
)
SELECT
    'LINEA_BASE',
    'CUANDO SE DEFINE UN ALCANCE DEBE DEFINIRSE UNA LINEA BASE DE REQUERIMIENTOS?',
    'PREGUNTA DE PRUEBA',
    true,
    'FAC001',
    id_opcion_respuesta
FROM opcion_respuesta
WHERE nombre='VERDADERO_FALSO';

INSERT INTO pregunta (
	    nombre,
	    texto_pregunta,
	    descripcion,
	    activo,
	    codigo_factor,
	    id_opcion_respuesta
)
SELECT
    'VALOR_GANADO',
    'EL VALOR GANADO PERMITE PREDECIR CUANTO DINERO SE GASTARA EN UN PROYECTO?',
    'PREGUNTA DE PRUEBA',
    true,
    'FAC002',
    id_opcion_respuesta
FROM opcion_respuesta
WHERE nombre='VERDADERO_FALSO';

-- =====================================================
-- PUESTO DE PRUEBA (CU028)
-- =====================================================
INSERT INTO puesto (codigo, nombre, descripcion, empresa, activo)
VALUES ('P001', 'ANALISTA DE DATOS', 'PUESTO DE PRUEBA PARA CU028', 'GRINGOTTS', true)
ON CONFLICT (codigo) DO NOTHING;

INSERT INTO caracteristica_puesto (ponderacion_minima, id_puesto, codigo_competencia)
SELECT 7.0, p.id_puesto, 'COMP001'
FROM puesto p
WHERE p.codigo = 'P001'
  AND NOT EXISTS (
      SELECT 1 FROM caracteristica_puesto cp
      WHERE cp.id_puesto = p.id_puesto AND cp.codigo_competencia = 'COMP001'
  );

INSERT INTO caracteristica_puesto (ponderacion_minima, id_puesto, codigo_competencia)
SELECT 6.0, p.id_puesto, 'COMP002'
FROM puesto p
WHERE p.codigo = 'P001'
  AND NOT EXISTS (
      SELECT 1 FROM caracteristica_puesto cp
      WHERE cp.id_puesto = p.id_puesto AND cp.codigo_competencia = 'COMP002'
  );

-- =====================================================
-- CANDIDATOS DE PRUEBA
-- =====================================================
INSERT INTO candidato (nro_candidato, apellido, nombre, nro_documento, tipo_documento, eliminado)
VALUES
(1001, 'LOPEZ', 'ANDREA', '30567890', 'DNI', false),
(1002, 'SANCHEZ', 'DIEGO', '33445566', 'DNI', false),
(1003, 'GOMEZ', 'LAURA', '32123456', 'DNI', false),
(1004, 'RODRIGUEZ', 'MARTIN', '29667147', 'DNI', false)
ON CONFLICT (nro_candidato) DO NOTHING;

-- =====================================================
-- EVALUACION DE PRUEBA
-- =====================================================
INSERT INTO evaluacion (nombre_evaluacion, descripcion, fecha_creacion, activo, id_puesto, id_usuario)
SELECT 'EVALUACION ANALISTA 2026', 'EVALUACION DE PRUEBA CU028', CURRENT_DATE, true, p.id_puesto, u.id_usuario
FROM puesto p, usuario u
WHERE p.codigo = 'P001' AND u.nombre = 'ADMIN'
  AND NOT EXISTS (
      SELECT 1 FROM evaluacion e WHERE e.nombre_evaluacion = 'EVALUACION ANALISTA 2026'
  );

-- =====================================================
-- CUESTIONARIOS DE PRUEBA
-- =====================================================
-- 1) COMPLETO que cumple ponderaciones mínimas (mayor puntaje)
INSERT INTO cuestionario (clave, estado, fecha_hora_inicio, fecha_hora_fin, fecha_hora_ultimo_ingreso, cantidad_accesos, id_evaluacion, nro_candidato)
SELECT 'CLAVE_CU028_1', 'COMPLETO', NOW() - INTERVAL '2 hours', NOW() - INTERVAL '1 hour', NOW() - INTERVAL '1 hour', 1, e.id_evaluacion, 1001
FROM evaluacion e
WHERE e.nombre_evaluacion = 'EVALUACION ANALISTA 2026'
  AND NOT EXISTS (SELECT 1 FROM cuestionario q WHERE q.clave = 'CLAVE_CU028_1');

-- 2) COMPLETO que cumple ponderaciones mínimas (menor puntaje)
INSERT INTO cuestionario (clave, estado, fecha_hora_inicio, fecha_hora_fin, fecha_hora_ultimo_ingreso, cantidad_accesos, id_evaluacion, nro_candidato)
SELECT 'CLAVE_CU028_2', 'COMPLETO', NOW() - INTERVAL '3 hours', NOW() - INTERVAL '2 hours', NOW() - INTERVAL '2 hours', 2, e.id_evaluacion, 1002
FROM evaluacion e
WHERE e.nombre_evaluacion = 'EVALUACION ANALISTA 2026'
  AND NOT EXISTS (SELECT 1 FROM cuestionario q WHERE q.clave = 'CLAVE_CU028_2');

-- 3) COMPLETO que NO alcanza ponderación mínima en COMP002 (5.0 < 6.0)
INSERT INTO cuestionario (clave, estado, fecha_hora_inicio, fecha_hora_fin, fecha_hora_ultimo_ingreso, cantidad_accesos, id_evaluacion, nro_candidato)
SELECT 'CLAVE_CU028_3', 'COMPLETO', NOW() - INTERVAL '4 hours', NOW() - INTERVAL '3 hours', NOW() - INTERVAL '3 hours', 3, e.id_evaluacion, 1003
FROM evaluacion e
WHERE e.nombre_evaluacion = 'EVALUACION ANALISTA 2026'
  AND NOT EXISTS (SELECT 1 FROM cuestionario q WHERE q.clave = 'CLAVE_CU028_3');

-- 4) INCOMPLETO (no muestra puntaje ni fecha_fin)
INSERT INTO cuestionario (clave, estado, fecha_hora_inicio, fecha_hora_fin, fecha_hora_ultimo_ingreso, cantidad_accesos, id_evaluacion, nro_candidato)
SELECT 'CLAVE_CU028_4', 'INCOMPLETO', NOW() - INTERVAL '1 day', NULL, NOW() - INTERVAL '12 hours', 2, e.id_evaluacion, 1004
FROM evaluacion e
WHERE e.nombre_evaluacion = 'EVALUACION ANALISTA 2026'
  AND NOT EXISTS (SELECT 1 FROM cuestionario q WHERE q.clave = 'CLAVE_CU028_4');

-- =====================================================
-- RESULTADOS POR COMPETENCIA (para los 3 COMPLETO)
-- =====================================================
INSERT INTO resultado_competencia (puntaje_obtenido, id_cuestionario, codigo_competencia)
SELECT 9.0, q.id_cuestionario, 'COMP001' FROM cuestionario q
WHERE q.clave = 'CLAVE_CU028_1'
  AND NOT EXISTS (SELECT 1 FROM resultado_competencia rc WHERE rc.id_cuestionario = q.id_cuestionario AND rc.codigo_competencia = 'COMP001');

INSERT INTO resultado_competencia (puntaje_obtenido, id_cuestionario, codigo_competencia)
SELECT 9.0, q.id_cuestionario, 'COMP002' FROM cuestionario q
WHERE q.clave = 'CLAVE_CU028_1'
  AND NOT EXISTS (SELECT 1 FROM resultado_competencia rc WHERE rc.id_cuestionario = q.id_cuestionario AND rc.codigo_competencia = 'COMP002');

INSERT INTO resultado_competencia (puntaje_obtenido, id_cuestionario, codigo_competencia)
SELECT 7.5, q.id_cuestionario, 'COMP001' FROM cuestionario q
WHERE q.clave = 'CLAVE_CU028_2'
  AND NOT EXISTS (SELECT 1 FROM resultado_competencia rc WHERE rc.id_cuestionario = q.id_cuestionario AND rc.codigo_competencia = 'COMP001');

INSERT INTO resultado_competencia (puntaje_obtenido, id_cuestionario, codigo_competencia)
SELECT 7.5, q.id_cuestionario, 'COMP002' FROM cuestionario q
WHERE q.clave = 'CLAVE_CU028_2'
  AND NOT EXISTS (SELECT 1 FROM resultado_competencia rc WHERE rc.id_cuestionario = q.id_cuestionario AND rc.codigo_competencia = 'COMP002');

INSERT INTO resultado_competencia (puntaje_obtenido, id_cuestionario, codigo_competencia)
SELECT 9.0, q.id_cuestionario, 'COMP001' FROM cuestionario q
WHERE q.clave = 'CLAVE_CU028_3'
  AND NOT EXISTS (SELECT 1 FROM resultado_competencia rc WHERE rc.id_cuestionario = q.id_cuestionario AND rc.codigo_competencia = 'COMP001');

INSERT INTO resultado_competencia (puntaje_obtenido, id_cuestionario, codigo_competencia)
SELECT 5.0, q.id_cuestionario, 'COMP002' FROM cuestionario q
WHERE q.clave = 'CLAVE_CU028_3'
  AND NOT EXISTS (SELECT 1 FROM resultado_competencia rc WHERE rc.id_cuestionario = q.id_cuestionario AND rc.codigo_competencia = 'COMP002');

INSERT INTO configuracion_sistema
(clave, valor, descripcion)
VALUES
(
	    'DIAS_MAXIMO_CUESTIONARIO_ACTIVO',
	    '15',
	    'Dias maximos para iniciar cuestionario'
);

INSERT INTO configuracion_sistema
(clave, valor, descripcion)
VALUES
(
	    'CANTIDAD_PREGUNTAS_BLOQUE',
	    '2',
	    'Cantidad de preguntas por bloque'
);
