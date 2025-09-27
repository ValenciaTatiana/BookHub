-- Tabla Libros
CREATE TABLE IF NOT EXISTS libros (
    isbn VARCHAR(20) PRIMARY KEY,
    titulo VARCHAR(100) NOT NULL,
    autor VARCHAR(100) NOT NULL,
    categoria VARCHAR(50) NOT NULL,
    estado BOOLEAN DEFAULT TRUE -- TRUE = disponible, FALSE = prestado
);

-- Tabla Usuarios
CREATE TABLE IF NOT EXISTS usuarios (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    telefono VARCHAR(20)
);

-- Tabla Prestamos
CREATE TABLE IF NOT EXISTS prestamos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    fecha_prestamo DATE NOT NULL,
    fecha_devolucion DATE,
    estado BOOLEAN DEFAULT TRUE, -- TRUE = prestado, FALSE = devuelto
    usuario_id INT NOT NULL,
    libro_isbn VARCHAR(20) NOT NULL,
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE,
    FOREIGN KEY (libro_isbn) REFERENCES libros(isbn) ON DELETE CASCADE
);
