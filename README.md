# GrandSpicy

Plataforma web de catalogo de productos picantes con sistema de reseñas de comunidad y panel de administracion. Desarrollada con Servlets, JSP, JDBC y MySQL.

---

## Stack tecnologico

| Capa | Tecnologia |
|------|-----------|
| Lenguaje | Java 17 (OpenJDK 21 compatible) |
| Backend | Servlets 4.0 + JSP + JSTL |
| Frontend | HTML5, CSS3 (tema oscuro responsive) |
| Base de datos | MySQL 8.0 |
| Acceso a datos | JDBC puro (PreparedStatement) |
| Servidor web | Apache Tomcat 9 (embebido o Docker) |
| Build | Maven 3.9 |
| Contenedores | Docker / Docker Compose |
| Control de versiones | Git |

Sin frameworks externos (no Spring Boot, no JPA, no Hibernate). Tecnologias puras de Java EE correspondientes a 1º de DAM.

---

## Funcionalidades

### Visitantes (no autenticados)

- Pagina principal con productos destacados y reseñas recientes
- Catalogo completo con filtro por categoria
- Detalle de producto con descripcion, precio, nivel Scoville, pais de origen y enlace de compra
- Registro de nuevo usuario

### Usuarios registrados (rol USER)

- Inicio y cierre de sesion
- Escritura de reseñas y valoraciones (1-5) en productos
- Perfil personal con historial de reseñas

### Administradores (rol ADMIN)

- Dashboard con estadisticas (conteo de productos y usuarios)
- CRUD completo de productos (alta, edicion, eliminacion)
- Subida de imagenes almacenadas como BLOB en base de datos
- Gestion de usuarios (listado y eliminacion)

---

## Inicio rapido

### Con Docker

```bash
docker-compose up --build
```

Acceder en `http://localhost:8080`.

### En desarrollo (Tomcat embebido)

```bash
mvn clean compile exec:java
```

Requiere MySQL 8.0 corriendo en `localhost:3306`. La base de datos y tablas se crean automaticamente al iniciar.

---

## Usuarios por defecto

| Usuario | Contraseña | Rol |
|---------|------------|-----|
| admin | admin1234 | ADMIN |
| user | user123 | USER |

---

## Estructura del proyecto

```
src/main/java/com/grandspicy/
  ├── Main.java                  # Punto de entrada (Tomcat embebido)
  ├── dao/
  │   └── BaseDatos.java         # Unica clase de acceso a BD (JDBC puro)
  ├── filtro/
  │   └── FiltroAutenticacion.java  # Protege rutas sensibles
  ├── modelo/
  │   ├── Producto.java          # POJO producto
  │   ├── Usuario.java           # POJO usuario
  │   └── Resena.java            # POJO reseña
  ├── servlet/
  │   ├── InicioServlet.java     # Home y catalogo
  │   ├── AutenticacionServlet.java  # Login, registro, logout
  │   ├── DetalleProductoServlet.java  # Detalle de producto
  │   ├── ResenaServlet.java     # Crear reseña
  │   ├── PerfilServlet.java     # Perfil de usuario
  │   ├── AdminServlet.java      # Panel de administracion
  │   └── ImagenServlet.java     # Servir imagenes BLOB
  └── util/
      └── PasswordUtil.java      # Cifrado SHA-256 + salt
```

---

## Seguridad

- **Autenticacion por sesion** (HttpSession)
- **Filtro de autorizacion** que protege `/admin/*`, `/profile` y `/review`
- **Contraseñas cifradas** con SHA-256 + salt aleatorio de 16 bytes (formato `salt:hash` en base64)
- **PreparedStatement** en todas las consultas SQL (previene inyeccion SQL)
- **Contenedores Docker separados**: base de datos no accesible desde el exterior

---

## Documentacion

- `DOCUMENTACION_TECNICA.txt` - Documentacion completa del proyecto (arquitectura, BD, despliegue, DAFO, CAME)
- `DEFENSA_PROYECTO.md` - Guion para defensa oral (5-10 min) con preguntas frecuentes y apendices
- `EXPLICACION_CODIGO.md` - Explicacion linea por linea de todo el codigo fuente

---

## Licencia

Proyecto academico desarrollado para 1º de DAM.
