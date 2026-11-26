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
WHERE nombre='VERDADERO_FALSO'
