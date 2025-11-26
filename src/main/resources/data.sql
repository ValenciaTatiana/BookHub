INSERT INTO libros (isbn, titulo, autor, categoria, estado) VALUES
('9788437604947', 'Don Quijote de la Mancha', 'Miguel de Cervantes', 'Clasico', TRUE),
('9780307743657', 'El codigo Da Vinci', 'Dan Brown', 'Thriller', FALSE),
('9780140449266', 'La Odisea', 'Homero', 'Epico', TRUE),
('9786075192923', 'Cien anos de soledad', 'Gabriel Garcia Marquez', 'Ficcion', FALSE),
('9788499890944', 'Sapiens', 'Yuval Noah Harari', 'Historia', TRUE),
('9780062316110', 'El poder del habito', 'Charles Duhigg', 'Productividad', TRUE),
('9789877251219', 'El nombre del viento', 'Patrick Rothfuss', 'Fantasia', FALSE);

INSERT INTO usuarios (cedula, nombre, email, telefono) VALUES
('1026589741', 'Laura Fernandez', 'laura.fernandez@example.com', '3015552211'),
('1008945672', 'Carlos Medina', 'carlos.medina@example.com', '3127779830'),
('1112347890', 'Valentina Lopez', 'valentina.lopez@example.com', '3001122233'),
('1098765432', 'Andres Salazar', 'andres.salazar@example.com', '3178890044'),
('1054321987', 'Daniela Ruiz', 'daniela.ruiz@example.com', '3185566778');

INSERT INTO prestamos (fecha_prestamo, fecha_devolucion, estado, usuario_id, libro_isbn) VALUES
('2025-09-20', NULL, TRUE, 1, '9786075192923'),     -- Laura, prestamo activo
('2025-09-10', '2025-09-25', FALSE, 2, '9780062316110'), -- Carlos, devuelto
('2025-09-15', NULL, TRUE, 3, '9789877251219'),     -- Valentina, activo
('2025-09-18', '2025-10-03', TRUE, 4, '9780307743657'),  -- Andres, activo
('2025-09-01', '2025-09-16', FALSE, 5, '9788437604947'); -- Daniela, devuelto
