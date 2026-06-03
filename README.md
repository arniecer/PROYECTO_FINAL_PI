# GrandSpicy

Plataforma web de catalogo de productos picantes. Los usuarios pueden descubrir salsas, especias, snacks y aceites picantes, ver su informacion, valorarlos y dejar resenas. Los administradores tienen un panel para gestionar productos y usuarios.

---

## Caracteristicas

- Cualquier visitante puede ver el catalogo, los detalles de cada producto y registrarse.
- Un usuario registrado puede iniciar sesion, escribir resenas y acceder a su perfil con el historial de resenas.
- Un administrador tiene un panel desde el que anadir, editar y eliminar productos, ademas de gestionar los usuarios registrados.

---

## Tecnologias

| Capa | Tecnologia |
|------|------------|
| Lenguaje | Java 17 |
| Backend | Servlets 4.0 + JSP + JSTL |
| Frontend | HTML5 + CSS3 (tema oscuro, responsive) |
| Base de datos | MySQL 8.0 |
| Acceso a datos | JDBC con PreparedStatement |
| Servidor | Apache Tomcat 9 |
| Build | Maven |
| Contenedores | Docker + Docker Compose |

---

## Puesta en marcha

### Con Docker (desarrollo local)

```bash
docker compose up --build
```

La aplicacion arranca en `http://localhost`.

### Con Docker (produccion)

```bash
git clone https://github.com/arniecer/PROYECTO_FINAL_PI.git
cd PROYECTO_FINAL_PI
docker compose up --build -d
```

Todos los contenedores tienen `restart: unless-stopped`, se levantan solos al reiniciar la maquina.

### Con Tomcat embebido (desarrollo sin Docker)

```bash
mvn clean compile exec:java
```

Requiere MySQL 8.0 accesible en `localhost:3306`. La base de datos y las tablas se crean automaticamente al arrancar.

---

## Variables de entorno

La aplicacion y los contenedores se configuran mediante variables de entorno definidas en `docker-compose.yml`.

| Variable | Contenedor | Descripcion | Valor por defecto |
|----------|------------|-------------|-------------------|
| `DB_URL` | app | URL de conexion a MySQL | `jdbc:mysql://mysql:3306/grandspicy?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC` |
| `DB_USER` | app | Usuario de MySQL | `root` |
| `DB_PASSWORD` | app | Contrasena de MySQL | `root` |

Si no se definen, el codigo fuente utiliza valores por defecto para desarrollo local (`localhost:3306`).

---

## Adaptacion a un dominio propio

La configuracion actual del repositorio esta preparada para el dominio `grands.yatat.es`. Para desplegar la aplicacion con un dominio diferente es necesario modificar los siguientes archivos:

### 1. nginx/default.conf

```nginx
server {
    listen 80;
    server_name tudominio.com;   # <-- cambiar aqui

    location /.well-known/acme-challenge/ {
        root /var/www/certbot;
    }

    location /img/ {
        root /usr/share/nginx/html;
    }

    location /images/product {
        proxy_pass http://app:8080;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
    }

    location /images/ {
        root /usr/share/nginx/html;
    }

    location / {
        proxy_pass http://app:8080;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
    }
}
```

### 2. nginx/default-ssl.conf (si se utiliza SSL)

```nginx
server {
    listen 80;
    server_name tudominio.com;       # <-- cambiar aqui
    return 301 https://$server_name$request_uri;
}

server {
    listen 443 ssl;
    server_name tudominio.com;       # <-- cambiar aqui

    ssl_certificate /etc/letsencrypt/live/tudominio.com/fullchain.pem;    # <-- cambiar aqui
    ssl_certificate_key /etc/letsencrypt/live/tudominio.com/privkey.pem;  # <-- cambiar aqui
    ...
}
```

Las rutas de los certificados SSL las genera Certbot automaticamente segun el dominio indicado durante la solicitud.

### 3. setup-ssl.sh

```bash
DOMAIN="tudominio.com"        # <-- cambiar aqui
EMAIL="admin@tudominio.com"   # <-- cambiar aqui
```

### Resumen de cambios necesarios

| Archivo | Lineas a modificar |
|---------|-------------------|
| `nginx/default.conf` | `server_name` |
| `nginx/default-ssl.conf` | `server_name` (x2) y rutas de certificados (x2) |
| `setup-ssl.sh` | `DOMAIN` y `EMAIL` |

---

## Despliegue en produccion con SSL

Requisitos previos:
- DNS del dominio apuntando a la IP publica del servidor.
- Puertos 80 y 443 abiertos en el firewall.
- Docker y Docker Compose instalados.

Pasos:

```bash
# 1. Clonar el repositorio y arrancar los contenedores
git clone https://github.com/arniecer/PROYECTO_FINAL_PI.git
cd PROYECTO_FINAL_PI
docker compose up --build -d

# 2. Configurar SSL con Certbot
./setup-ssl.sh
```

El script `setup-ssl.sh` solicita los certificados a Let's Encrypt y luego sustituye `nginx/default.conf` por `nginx/default-ssl.conf` para habilitar HTTPS con redireccion automatica de HTTP a HTTPS.

---

## Copias de seguridad

El sistema de backups se compone de dos scripts que se ejecutan mediante cron en el servidor de produccion, mas un tercero para restaurar.

### backup_total.sh

Se ejecuta los domingos a las 02:00. Realiza un volcado completo de la base de datos con `mysqldump --flush-logs`, guarda la posicion actual del binlog, y empaqueta los certificados SSL de Let's Encrypt. Los archivos se almacenan en `/home/ubuntu/backups/totales/` con el formato `grandspicy_YYYY-MM-DD.sql.gz`. Los backups con mas de 28 dias se eliminan automaticamente.

El flag `--flush-logs` cierra el binlog activo y abre uno nuevo, marcando el inicio de un nuevo ciclo de cambios incrementales.

### backup_incremental.sh

Se ejecuta de lunes a sabado a las 03:00. Recorre los binlogs disponibles en MySQL y copia aquellos que aun no se hayan guardado a la carpeta `/home/ubuntu/backups/incrementales/`. De esta forma solo se almacenan los cambios ocurridos desde el ultimo backup completo. La retencion es de 28 dias.

### restore.sh

Restaura un backup completo a partir de su nombre de archivo y pregunta si se desean aplicar los cambios incrementales posteriores:

```bash
./scripts/restore.sh grandspicy_2026-06-07.sql.gz
```

Los incrementales se aplican en orden mediante `mysqlbinlog` para recuperar el estado exacto de la base de datos en el momento deseado.

### Requisitos en produccion

- Los scripts estan pensados para ejecutarse en el mismo servidor donde corre Docker.
- MySQL debe tener la opcion `log_bin` activada (binlog habilitado).
- El directorio de backups se crea automaticamente en `/home/ubuntu/backups/`.

---

## Arquitectura de contenedores

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
  |     - Puertos 80 y 443 expuestos al host
  |     - Proxy inverso hacia grandspicy-app:8080
  |     - Sirve recursos estaticos (/img/, /images/) directamente
  |     - Configuracion SSL con Certbot
  |
  +-- grandspicy-certbot (Certbot)
        - Solo se ejecuta bajo demanda (profile: ssl-setup)
        - Genera certificados SSL
```

El Dockerfile compila el proyecto con Maven y genera un WAR que se despliega en Tomcat 9.

---

## Usuarios por defecto

| Usuario | Contrasena | Rol |
|---------|------------|-----|
| admin | admin1234 | ADMIN |
| user | user123 | USER |

---

## Seguridad

- Autenticacion por sesion (HttpSession).
- Las rutas `/admin/*`, `/profile` y `/review` estan protegidas por un filtro.
- Las contrasenas se almacenan cifradas con SHA-256 y salt aleatorio.
- Todas las consultas SQL usan PreparedStatement para evitar inyeccion SQL.
- En produccion la base de datos no esta expuesta al exterior.

---

## Estructura del codigo

```
src/main/java/com/grandspicy/
  Main.java                  - Arranca Tomcat embebido
  dao/BaseDatos.java         - Acceso a base de datos
  filtro/FiltroAutenticacion.java  - Control de acceso
  modelo/                    - Producto, Usuario, Resena (POJOs)
  servlet/                   - Controladores HTTP (7 servlets)
  util/PasswordUtil.java     - Cifrado de contrasenas
```

---

## Licencia

Hecho por Arniecer Obispo - 1 DAM.
