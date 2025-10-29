# BookHub

Aplicacion de gestion bibliotecaria construida con Spring Boot y JDBC Template. Permite administrar libros, usuarios y prestamos desde una API REST y una interfaz web estatica incluida.

## Requisitos previos

- Java 17 (puedes usar el wrapper `mvnw` sin instalar Maven)
- Maven 3.9+ (opcional; `mvnw`/`mvnw.cmd` ya vienen en el repo)
- Motor de base de datos accesible por JDBC (por defecto H2 en memoria)

## Estructura principal

```
bookhub/
|-- src/main/java/com/bookhub/...   # Codigo fuente (entities, repositories, services, controllers)
|-- src/main/resources/             # Configuracion, scripts SQL e interfaz estatica
|   |-- application.properties
|   |-- schema.sql / data.sql       # Creacion y datos iniciales
|   `-- static/index.html           # Panel web
|-- pom.xml                         # Dependencias Maven
`-- README.md                       # Este archivo
```

## Configuracion inicial

1. **Base de datos**
   - En `application.properties` viene configurada H2 en memoria.
   - Si usas MySQL, PostgreSQL u otro motor, actualiza la URL, usuario y contrasena, y adapta `schema.sql`/`data.sql` o crea las tablas manualmente.

2. **Propiedades JDBC por defecto**
   ```properties
   spring.datasource.url=jdbc:h2:mem:bookhub
   spring.datasource.username=sa
   spring.datasource.password=
   spring.sql.init.mode=always
   ```

3. **Puerto de la aplicacion (opcional)**
   - Ajusta `server.port` y `server.servlet.context-path` si necesitas otro puerto o contexto.

## Como ejecutar el proyecto

### Opcion A: usando Maven Wrapper (recomendado)

```bash
# Linux / macOS
./mvnw spring-boot:run

# Windows
mvnw.cmd spring-boot:run
```

### Opcion B: usando Maven instalado

```bash
mvn spring-boot:run
```

### Compilar y empaquetar el JAR

```bash
./mvnw clean package
java -jar target/bookhub-0.0.1-SNAPSHOT.jar
```

## Endpoints principales de la API REST

Base URL por defecto: `http://localhost:8080`

### Libros (RF001-RF005)
- `GET /api/libros` - Listar todos los libros
- `POST /api/libros` - Registrar nuevo libro
- `GET /api/libros/{isbn}` - Buscar por ISBN
- `GET /api/libros/buscar?q=` - Buscar por titulo o autor
- `PUT /api/libros/{isbn}` - Actualizar informacion
- `PATCH /api/libros/{isbn}/estado?disponible=true|false` - Cambiar estado disponible/prestado

### Usuarios (RF006-RF008, RN001, RN005)
- `POST /api/usuarios` - Registrar usuario
- `GET /api/usuarios` - Listar usuarios
- `GET /api/usuarios/{id}` - Consultar usuario
- `GET /api/usuarios/{id}/prestamos/activos` - Cantidad de prestamos activos
- `GET /api/usuarios/{id}/puede-prestar` - Valida RN001 (maximo 3 prestamos activos)

### Prestamos (RF009-RF013, RN001-RN004)
- `POST /api/prestamos` - Crear prestamo
- `PUT /api/prestamos/devolver` - Registrar devolucion
- `GET /api/prestamos/activos` - Prestamos activos globales
- `GET /api/prestamos/usuario/{id}/activos` - Prestamos activos por usuario
- `GET /api/prestamos/usuario/{id}/historial` - Historial de prestamos por usuario
- `GET /api/prestamos/libros-disponibles` - Libros disponibles para prestar

### Respuestas y errores
- `200 OK` para consultas exitosas
- `201 Created` al crear recursos
- `400 Bad Request` cuando falta informacion o se viola una regla RN001-RN005
- `404 Not Found` cuando el recurso no existe

## Interfaz web incluida

- Navega a `http://localhost:8080/index.html` para usar el panel.
- Desde ahi puedes:
  - Registrar, buscar, editar y cambiar estado de libros.
  - Registrar usuarios y evaluar si pueden solicitar prestamos.
  - Crear/devolver prestamos, ver activos globales, activos por usuario, historial y libros disponibles.

## Scripts SQL

- `schema.sql`: crea las tablas `libros`, `usuarios` y `prestamos`.
- `data.sql`: inserta datos de ejemplo (se ejecuta si `spring.sql.init.mode=always`).

## Pruebas

```bash
./mvnw test
```

Coloca tus pruebas en `src/test/java` y amplia la cobertura para reglas de negocio y consultas SQL segun sea necesario.

## Despliegue

1. Construye el JAR (`./mvnw clean package`).
2. Configura variables de entorno `SPRING_DATASOURCE_URL`, `SPRING_DATASOURCE_USERNAME`, etc., o modifica `application.properties` para el entorno destino.
3. Ejecuta `java -jar bookhub-0.0.1-SNAPSHOT.jar` en el servidor.

## Mantenimiento y contribucion

- La logica de negocio reside en la capa `service`; el acceso a datos en `repository` con consultas preparadas.
- Mantener sincronizados `schema.sql` y las entidades.
- Documenta aqui cualquier nuevo modulo o endpoint y agrega pruebas antes de enviar cambios.

Disfruta usando BookHub.
