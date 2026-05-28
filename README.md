# GrandSpicy

Plataforma web de catalogo de productos picantes. Los usuarios pueden descubrir salsas, especias, snacks y aceites picantes, ver su informacion, valorarlos y dejar reseñas. Los administradores tienen un panel para gestionar productos y usuarios.

---

## Que ofrece

**Cualquier visitante** puede ver el catalogo, los detalles de cada producto y registrarse.

**Un usuario registrado** puede iniciar sesion, escribir reseñas y acceder a su perfil con el historial de reseñas.

**Un administrador** tiene un panel desde el que anadir, editar y eliminar productos, ademas de gestionar los usuarios registrados.

---

## Tecnologias usadas

| Que | Como |
|-----|------|
| Lenguaje | Java 17 |
| Backend | Servlets 4.0 + JSP + JSTL |
| Frontend | HTML5 + CSS3 (tema oscuro, responsive) |
| Base de datos | MySQL 8.0 |
| Acceso a datos | JDBC puro con PreparedStatement |
| Servidor | Apache Tomcat 9 |
| Build | Maven |
| Contenedores | Docker + Docker Compose |

No se han usado frameworks externos como Spring Boot, JPA o Hibernate. Todo es Java EE puro, que es lo que se ha visto durante el curso.

---

## Como arrancarlo

### Con Docker

```bash
docker-compose up --build
```

La aplicacion arranca en `http://localhost:8080`.

### En desarrollo (Tomcat embebido)

```bash
mvn clean compile exec:java
```

Necesitas tener MySQL 8.0 en `localhost:3306`. La base de datos y las tablas se crean solas al arrancar.

---

## Usuarios por defecto

| Usuario | Contrasena | Rol |
|---------|------------|-----|
| admin | admin1234 | ADMIN |
| user | user123 | USER |

---

## Docker y despliegue

La aplicacion se despliega con dos contenedores:

```
docker-compose.yml
  |
  +-- grandspicy-db (MySQL 8.0)
  |     - Puerto 3306 (solo red interna)
  |     - Datos persistentes en volumen docker
  |     - init.sql se ejecuta al crear la base de datos
  |     - Healthcheck que espera a que MySQL este listo
  |
  +-- grandspicy-app (Tomcat 9)
        - Puerto 8080 mapeado al host
        - Depende de que grandspicy-db este saludable
        - Variables de entorno: DB_URL, DB_USER, DB_PASSWORD
```

El Dockerfile compila el proyecto con Maven y genera un WAR que se despliega en Tomcat 9.

**Para desplegar en produccion (AWS EC2 por ejemplo):**

```bash
# En la maquina virtual
git clone https://github.com/arniecer/PROYECTO_FINAL_PI.git
cd PROYECTO_FINAL_PI
docker-compose up -d --build
```

Ambos contenedores tienen `restart: unless-stopped`, asi que se levantan solos al reiniciar la maquina.

---

## Seguridad

- Autenticacion por sesion (HttpSession)
- Las rutas `/admin/*`, `/profile` y `/review` estan protegidas por un filtro
- Las contraseñas se guardan cifradas con SHA-256 + salt aleatorio
- Todas las consultas SQL usan PreparedStatement para evitar inyeccion SQL
- En produccion, la base de datos no esta expuesta al exterior

---

## Estructura del codigo

```
src/main/java/com/grandspicy/
  Main.java                  - Arranca Tomcat embebido
  dao/BaseDatos.java         - Todo el acceso a base de datos
  filtro/FiltroAutenticacion.java  - Control de acceso
  modelo/                    - Producto, Usuario, Resena (POJOs)
  servlet/                   - Controladores HTTP (7 servlets)
  util/PasswordUtil.java     - Cifrado de contrasenas
```

---

## Licencia

Proyecto academico - 1º de DAM.
