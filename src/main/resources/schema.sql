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

-- Tabla Reportes
CREATE TABLE IF NOT EXISTS reportes (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    titulo VARCHAR(255) NOT NULL,
    descripcion VARCHAR(2000) NOT NULL,
    fecha_generacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Tabla Reservas
CREATE TABLE IF NOT EXISTS reservas (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    usuario_id INT NOT NULL,
    libro_isbn VARCHAR(20) NOT NULL,
    fecha_reserva TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    atendida BOOLEAN NOT NULL DEFAULT FALSE,
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE,
    FOREIGN KEY (libro_isbn) REFERENCES libros(isbn) ON DELETE CASCADE
);

-- Tabla Auditoria
CREATE TABLE IF NOT EXISTS auditoria (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    entidad VARCHAR(100) NOT NULL,
    accion VARCHAR(100) NOT NULL,
    referencia_id VARCHAR(100) NOT NULL,
    detalles VARCHAR(2000) NOT NULL,
    fecha TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
