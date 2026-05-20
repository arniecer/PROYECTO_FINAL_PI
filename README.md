# 🌶️ GrandSpicy

Plataforma web para descubrir y reseñar productos picantes de todo el mundo.

## 🚀 Tecnologías

- **Backend**: Spring Boot 3.2, Spring MVC, Spring Data JPA, Spring Security
- **Frontend**: Thymeleaf, HTML, CSS (sin JavaScript)
- **Base de datos**: MySQL 8.0
- **Build**: Maven
- **Despliegue**: Docker / Docker Compose / AWS EC2

## ✨ Funcionalidades

- Catálogo de productos picantes con nivel Scoville, país de origen y enlaces de compra
- Sistema de reseñas y valoraciones de la comunidad
- Registro e inicio de sesión con roles USER y ADMIN
- Perfil de usuario con historial de reseñas
- Panel de administración para gestionar productos y usuarios
- Subida de imágenes a base de datos
- Diseño responsive con paleta rojo/negro

## 🧑‍💻 Usuarios por defecto

| Usuario | Contraseña | Rol |
|---------|------------|-----|
| admin | admin123 | ADMIN |
| user | user123 | USER |

## 🐳 Docker

```bash
docker-compose up --build
```

## 📄 Documentación

Ver [DOCUMENTACION.md](DOCUMENTACION.md) para información detallada sobre despliegue, AWS, HTTPS, DAFO, CAME, etc.
