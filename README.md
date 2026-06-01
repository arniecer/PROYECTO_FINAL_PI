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
| Acceso a datos | JDBC con PreparedStatement |
| Servidor | Apache Tomcat 9 |
| Build | Maven |
| Contenedores | Docker + Docker Compose |

---

## Como arrancarlo

### Con Docker (desarrollo local)

```bash
docker compose up --build
```

La aplicacion arranca en `http://localhost`.

### En produccion (AWS EC2 con HTTPS)

```bash
# 1. Clonar y arrancar
git clone https://github.com/arniecer/PROYECTO_FINAL_PI.git
cd PROYECTO_FINAL_PI
docker compose up --build -d

# 2. Configurar SSL con Certbot
./setup-ssl.sh
```

La aplicacion arranca en `https://GrandS.yatat.es`.

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

La aplicacion se despliega con cuatro contenedores:

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
  |     - Solo red interna (sin puerto expuesto al host)
  |     - Depende de que grandspicy-db este saludable
  |     - Variables de entorno: DB_URL, DB_USER, DB_PASSWORD
  |
  +-- grandspicy-nginx (Nginx 1.25)
  |     - Puerto 80 y 443 expuestos al host
  |     - Proxy inverso hacia grandspicy-app:8080
  |     - Sirve estaticos (/img/, /images/) directamente
  |     - Config SSL con Certbot
  |
  +-- grandspicy-certbot (Certbot)
        - Solo se ejecuta bajo demanda (profile: ssl-setup)
        - Genera certificados SSL para GrandS.yatat.es
```

El Dockerfile compila el proyecto con Maven y genera un WAR que se despliega en Tomcat 9.

**Para desplegar en produccion (AWS EC2):**

```bash
# 1. En la maquina virtual
git clone https://github.com/arniecer/PROYECTO_FINAL_PI.git
cd PROYECTO_FINAL_PI

# 2. Arrancar todo (sin SSL aun)
docker compose up --build -d

# 3. Configurar SSL con Certbot
./setup-ssl.sh
```

Todos los contenedores tienen `restart: unless-stopped`, asi que se levantan solos al reiniciar la maquina.

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


