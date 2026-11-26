INSERT INTO usuario (nombre, activo)
VALUES ('ADMIN', true)
ON CONFLICT (nombre) DO NOTHING;

INSERT INTO usuario (nombre, activo)
VALUES ('JUAN', true)
ON CONFLICT (nombre) DO NOTHING
