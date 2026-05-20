# GRANDSPICY - Documentación del Proyecto

## Descripción del Proyecto

GrandSpicy es una aplicación web de tienda online especializada en productos picantes.
Desarrollada con Spring Boot, permite a los usuarios navegar por un catálogo de productos,
registrarse, iniciar sesión, dejar reseñas y realizar pedidos. Los administradores pueden
gestionar productos y usuarios desde un panel de control.

## Funcionalidades

### Usuarios no registrados
- Ver la página principal con productos destacados
- Navegar por el catálogo de productos
- Ver detalles de cada producto
- Registrarse como nuevo usuario
- Iniciar sesión

### Usuarios registrados
- Gestionar su perfil
- Realizar pedidos de productos
- Ver historial de pedidos
- Dejar reseñas en productos

### Administradores
- Panel de administración con estadísticas
- Añadir, editar y eliminar productos
- Gestionar usuarios (eliminar)

## Tecnologías utilizadas

| Tecnología | Versión | Propósito |
|------------|---------|-----------|
| Java | 17 | Lenguaje de programación |
| Spring Boot | 3.2.0 | Framework principal |
| Spring MVC | - | Patrón Modelo-Vista-Controlador |
| Spring Data JPA | - | Acceso a base de datos |
| Spring Security | - | Autenticación y autorización |
| Thymeleaf | - | Motor de plantillas del lado servidor |
| MySQL | 8.0 | Base de datos relacional |
| Maven | - | Gestión de dependencias |
| HTML5 | - | Estructura de páginas |
| CSS3 | - | Estilos visuales |
| Docker | - | Contenedores |


## Estructura del proyecto

```
grandspicy/
├── Dockerfile
├── docker-compose.yml
├── pom.xml
├── DOCUMENTACION.md
├── src/main/java/com/grandspicy/
│   ├── GrandSpicyApplication.java     # Clase principal
│   ├── config/
│   │   ├── SecurityConfig.java        # Configuración de Spring Security
│   │   └── MvcConfig.java             # Configuración de MVC
│   ├── controller/
│   │   ├── HomeController.java        # Página principal
│   │   ├── AuthController.java        # Registro de usuarios
│   │   ├── ProductController.java     # Catálogo y detalle
│   │   ├── ReviewController.java      # Reseñas
│   │   ├── OrderController.java       # Pedidos
│   │   ├── ProfileController.java     # Perfil de usuario
│   │   └── AdminController.java       # Administración
│   ├── model/
│   │   ├── User.java                  # Entidad usuario
│   │   ├── Product.java               # Entidad producto
│   │   ├── Review.java                # Entidad reseña
│   │   ├── Order.java                 # Entidad pedido
│   │   └── OrderItem.java             # Entidad detalle pedido
│   ├── repository/                    # Repositorios JPA
│   └── service/                       # Lógica de negocio
└── src/main/resources/
    ├── application.properties         # Configuración
    ├── data.sql                       # Datos iniciales
    ├── static/css/style.css           # Estilos
    └── templates/                     # Plantillas Thymeleaf
```

## Puertos utilizados

| Puerto | Servicio | Descripción |
|--------|----------|-------------|
| 8080 | Spring Boot | Aplicación web |
| 3306 | MySQL | Base de datos (local) |
| 3307 | MySQL | Base de datos (Docker) |

## Base de Datos

### Tablas

- **users**: Almacena los usuarios del sistema
- **products**: Catálogo de productos picantes
- **reviews**: Reseñas de usuarios sobre productos
- **orders**: Pedidos realizados por usuarios
- **order_items**: Detalle de cada pedido

### Relaciones

- Un usuario tiene muchos pedidos (1:N)
- Un usuario tiene muchas reseñas (1:N)
- Un producto tiene muchas reseñas (1:N)
- Un pedido tiene muchos items (1:N)
- Un producto aparece en muchos items de pedido (1:N)

## Usuarios por defecto

| Usuario | Contraseña | Rol |
|---------|------------|-----|
| admin | admin123 | ADMIN |
| user | user123 | USER |

## Despliegue con Docker

### Requisitos
- Docker
- Docker Compose

### Pasos

1. Construir y ejecutar los contenedores:
   ```bash
   docker-compose up --build
   ```

2. Acceder a la aplicación:
   ```
   http://localhost:8080
   ```

### Detener los contenedores
```bash
docker-compose down
```

## HTTPS

Para habilitar HTTPS en producción se recomienda usar Nginx como proxy inverso
con Certbot para certificados SSL gratuitos de Let's Encrypt.

### Configuración básica

```bash
sudo apt install nginx certbot python3-certbot-nginx
sudo nano /etc/nginx/sites-available/grandspicy
```

Configuración de Nginx:
```nginx
server {
    listen 80;
    server_name tudominio.com;

    location / {
        proxy_pass http://localhost:8080;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
    }
}
```

```bash
sudo certbot --nginx -d tudominio.com
```

## Copias de Seguridad

### Backup de la base de datos MySQL
```bash
docker exec grandspicy-mysql mysqldump -u root -proot grandspicy > backup.sql
```

### Restaurar backup
```bash
cat backup.sql | docker exec -i grandspicy-mysql mysql -u root -proot grandspicy
```

## Análisis DAFO

### Debilidades
- Sin JavaScript, la interfaz es menos dinámica
- Dependencia de MySQL como única base de datos
- Seguridad básica sin verificación de email
- Sin pasarela de pago real

### Amenazas
- Competencia de grandes plataformas de comercio electrónico
- Posibles vulnerabilidades de seguridad por configuración simple

### Fortalezas
- Tecnología Java/Spring Boot robusta y escalable
- Arquitectura limpia y fácil de entender
- Despliegue sencillo con Docker
- Código abierto y personalizable

### Oportunidades
- Ampliación a más categorías de productos
- Integración de pasarela de pago
- Mejora de la interfaz con diseño responsive
- Internacionalización

## Análisis CAME

### Corregir Debilidades
- Añadir validación de email en el registro
- Implementar pruebas automatizadas
- Mejorar la seguridad con HTTPS y buenas prácticas

### Afrontar Amenazas
- Diferenciarse con productos especializados y únicos
- Mantener el software actualizado con parches de seguridad

### Mantener Fortalezas
- Seguir usando Spring Boot como base sólida
- Mantener la arquitectura limpia y documentada
- Conservar la facilidad de despliegue

### Explotar Oportunidades
- Añadir nuevas funcionalidades según demanda
- Mejorar la experiencia de usuario progresivamente
- Expandir el catálogo de productos

## Estructura del Repositorio GitHub

```
grandspicy/
├── .gitignore
├── Dockerfile
├── docker-compose.yml
├── pom.xml
├── DOCUMENTACION.md
├── src/
│   ├── main/
│   │   ├── java/com/grandspicy/
│   │   │   ├── GrandSpicyApplication.java
│   │   │   ├── config/
│   │   │   ├── controller/
│   │   │   ├── model/
│   │   │   ├── repository/
│   │   │   └── service/
│   │   └── resources/
│   │       ├── application.properties
│   │       ├── data.sql
│   │       ├── static/css/style.css
│   │       ├── static/img/
│   │       └── templates/
│   └── test/
└── README.md
```
