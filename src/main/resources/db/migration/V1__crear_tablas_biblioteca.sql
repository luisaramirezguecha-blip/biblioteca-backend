-- V1: catálogo de libros, socios de la biblioteca y préstamos
CREATE TABLE libro (
    id                     BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    titulo                 VARCHAR(150) NOT NULL,
    autor                  VARCHAR(100) NOT NULL,
    isbn                   VARCHAR(17)  NOT NULL,
    categoria              VARCHAR(50)  NOT NULL,
    anio_publicacion       INTEGER      NOT NULL,
    ejemplares_totales     INTEGER      NOT NULL,
    ejemplares_disponibles INTEGER      NOT NULL,
    creado_en              TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    actualizado_en         TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT uq_libro_isbn UNIQUE (isbn),
    CONSTRAINT ck_libro_totales CHECK (ejemplares_totales >= 1),
    CONSTRAINT ck_libro_disponibles CHECK (ejemplares_disponibles BETWEEN 0 AND ejemplares_totales)
);

CREATE TABLE socio (
    id        BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    nombre    VARCHAR(100) NOT NULL,
    email     VARCHAR(120) NOT NULL,
    creado_en TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT uq_socio_email UNIQUE (email)
);

CREATE TABLE prestamo (
    id                BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    libro_id          BIGINT      NOT NULL REFERENCES libro (id),
    socio_id          BIGINT      NOT NULL REFERENCES socio (id),
    fecha_prestamo    DATE        NOT NULL,
    fecha_vencimiento DATE        NOT NULL,
    fecha_devolucion  DATE,
    estado            VARCHAR(10) NOT NULL DEFAULT 'ACTIVO',

    CONSTRAINT ck_prestamo_estado CHECK (estado IN ('ACTIVO', 'DEVUELTO')),
    CONSTRAINT ck_prestamo_fechas CHECK (fecha_vencimiento >= fecha_prestamo)
);

CREATE INDEX idx_prestamo_socio_estado ON prestamo (socio_id, estado);
CREATE INDEX idx_prestamo_libro_estado ON prestamo (libro_id, estado);
-- Defensa en profundidad: un socio no puede tener dos préstamos ACTIVOS del mismo libro
CREATE UNIQUE INDEX uq_prestamo_activo ON prestamo (socio_id, libro_id) WHERE estado = 'ACTIVO';
