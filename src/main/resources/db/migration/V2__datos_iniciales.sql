-- V2: datos de ejemplo para ver algo al levantar el proyecto.
-- Las copias disponibles ya descuentan los préstamos activos de más abajo.
INSERT INTO libro (titulo, autor, isbn, categoria, anio_publicacion, ejemplares_totales, ejemplares_disponibles) VALUES
    ('Cien años de soledad',          'Gabriel García Márquez', '978-0307474728', 'Novela',      1967, 3, 3),
    ('Clean Code',                    'Robert C. Martin',       '978-0132350884', 'Informática', 2008, 2, 1),
    ('Don Quijote de la Mancha',      'Miguel de Cervantes',    '978-8420412146', 'Clásicos',    1605, 1, 0),
    ('Introducción a los algoritmos', 'Thomas H. Cormen',       '978-0262033848', 'Informática', 2009, 2, 2),
    ('La casa de los espíritus',      'Isabel Allende',         '978-8401242304', 'Novela',      1982, 2, 2),
    ('Sapiens',                       'Yuval Noah Harari',      '978-0062316097', 'Historia',    2011, 3, 3);

INSERT INTO socio (nombre, email) VALUES
    ('Ana Pérez',   'ana.perez@ipss.cl'),
    ('Luis Rojas',  'luis.rojas@ipss.cl'),
    ('Camila Soto', 'camila.soto@ipss.cl');

-- Ana tiene Clean Code (vigente); Luis tiene El Quijote (vencido); Camila ya devolvió Cien años de soledad
INSERT INTO prestamo (libro_id, socio_id, fecha_prestamo, fecha_vencimiento, fecha_devolucion, estado) VALUES
    (2, 1, CURRENT_DATE - 3,  CURRENT_DATE + 11, NULL,              'ACTIVO'),
    (3, 2, CURRENT_DATE - 20, CURRENT_DATE - 6,  NULL,              'ACTIVO'),
    (1, 3, CURRENT_DATE - 30, CURRENT_DATE - 16, CURRENT_DATE - 20, 'DEVUELTO');
