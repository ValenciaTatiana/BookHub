INSERT INTO libros (isbn, titulo, autor, categoria, estado) VALUES
('123456', 'Libro Ejemplo 1', 'Autor 1', 'Ficcion', TRUE),
('789012', 'Libro Ejemplo 2', 'Autor 2', 'No Ficcion', FALSE),
('345678', 'Historia del Mundo', 'Autor 3', 'Historia', TRUE),
('901234', 'Ciencia Facil', 'Autor 4', 'Ciencia', TRUE),
('567890', 'Aventura Epica', 'Autor 5', 'Fantasia', FALSE);

INSERT INTO usuarios (nombre, email, telefono) VALUES
('Andres Felipe', 'andres@example.com', '1234567890'),
('Angie Tatiana', 'angie@example.com', '0987654321'),
('Yeison Andres', 'yeison@example.com', '1112223333'),
('Maria Jose', 'maria@example.com', '4445556666');

INSERT INTO prestamos (fecha_prestamo, fecha_devolucion, estado, usuario_id, libro_isbn) VALUES
('2025-09-20', '2025-10-05', TRUE, 1, '789012'),  -- Andres, prestamo activo
('2025-09-10', '2025-09-25', FALSE, 2, '567890'), -- Angie, devuelto
('2025-09-15', NULL, TRUE, 2, '123456'),         -- Angie, segundo prestamo activo
('2025-09-18', '2025-10-03', TRUE, 3, '345678'), -- Yeison, prestamo activo
('2025-09-01', '2025-09-16', FALSE, 4, '901234'); -- Maria, devuelto
