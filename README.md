# GrandSpicy

Plataforma web para descubrir, valorar y compartir opiniones sobre productos picantes de todo el mundo. Catalogo de salsas, especias, snacks y aceites con sistema de reseñas de comunidad y panel de administracion.

---

## Tabla de contenidos

- [Stack tecnologico](#stack-tecnologico)
- [Funcionalidades](#funcionalidades)
- [Inicio rapido](#inicio-rapido)
- [Estructura del proyecto](#estructura-del-proyecto)
- [Usuarios por defecto](#usuarios-por-defecto)
- [Modelo de datos](#modelo-de-datos)
- [Despliegue](#despliegue)
- [Licencia](#licencia)

---

## Stack tecnologico

| Capa | Tecnologia |
|------|-----------|
| Lenguaje | Java 17 |
| Framework | Spring Boot 3.2.0 |
| Seguridad | Spring Security 6 |
| Persistencia | Spring Data JPA / Hibernate |
| Base de datos | MySQL 8.0 |
| Frontend | Thymeleaf, HTML5, CSS3 |
| Build | Maven |
| Contenedores | Docker / Docker Compose |

La aplicacion sigue el patron **Modelo-Vista-Controlador (MVC)** con una arquitectura en capas: controladores, servicios, repositorios y entidades.

---

## Funcionalidades

### Visitantes (no autenticados)

- Visualizar pagina principal con productos destacados y reseñas recientes
- Navegar por el catalogo completo de productos
- Ver detalle de cada producto (descripcion, precio, nivel Scoville, pais de origen)
- Registrarse en la plataforma

### Usuarios registrados (rol USER)

- Iniciar y cerrar sesion
- Escribir reseñas y valoraciones (1-5) en productos
- Ver perfil personal con historial de reseñas
- Realizar pedidos y consultar historial

### Administradores (rol ADMIN)

- Panel de administracion con dashboard de estadisticas
- CRUD completo de productos (Crear, Leer, Actualizar, Eliminar)
- Gestion de usuarios (listar y eliminar)
- Gestion de pedidos

---

## Inicio rapido

### Con Docker (recomendado)

```bash
docker-compose up --build
```

La aplicacion estara disponible en `http://localhost:8080`.

### Sin Docker (desarrollo local)

1. Asegurate de tener MySQL 8.0 corriendo en `localhost:3306`
2. Crea la base de datos:
   ```sql
   CREATE DATABASE IF NOT EXISTS grandspicy;
   ```
3. Compila y ejecuta:
   ```bash
   ./mvnw spring-boot:run
   ```

---

## Estructura del proyecto

```
src/main/java/com/grandspicy/
  ├── GrandSpicyApplication.java    # Punto de entrada
  ├── config/
  │   ├── SecurityConfig.java       # Configuracion Spring Security
  │   └── MvcConfig.java            # Configuracion Spring MVC
  ├── controller/
  │   ├── HomeController.java       # Pagina principal
  │   ├── AuthController.java       # Login, registro, logout
  │   ├── ProductController.java    # Catalogo y detalle de producto
  │   ├── ReviewController.java     # Reseñas
  │   ├── ProfileController.java    # Perfil de usuario
  │   ├── AdminController.java      # Panel de administracion
  │   └── ImageController.java      # Servir imagenes
  ├── model/
  │   ├── User.java                 # Entidad usuario
  │   ├── Product.java              # Entidad producto
  │   └── Review.java               # Entidad reseña
  ├── repository/                   # Repositorios Spring Data JPA
  └── service/                      # Logica de negocio
```

---

## Usuarios por defecto

| Usuario | Contraseña | Rol |
|---------|------------|-----|
| admin | admin123 | ADMIN |
| user | user123 | USER |

---

## Modelo de datos

La base de datos contiene tres tablas principales:

- **users** - Almacena usuarios del sistema con rol (USER/ADMIN)
- **products** - Catalogo de productos picantes con imagen almacenada como BLOB
- **reviews** - Reseñas de usuarios vinculadas a productos mediante claves foraneas

Las reseñas utilizan `ON DELETE CASCADE` para mantener la integridad referencial.

---

## Despliegue

### Entorno de produccion (AWS EC2)

1. Clonar el repositorio en la instancia EC2
2. Instalar Docker y Docker Compose
3. Configurar variables de entorno si es necesario
4. Ejecutar `docker-compose up -d --build`

### HTTPS

Para entornos de produccion se recomienda utilizar Nginx como proxy inverso con Certbot para certificados SSL de Let's Encrypt.

---

## Licencia

Este proyecto es de codigo abierto y fue desarrollado como proyecto final de 1º de DAM.
