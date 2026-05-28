===============================================================================
DEFENSA ORAL - GRANDSPICY
===============================================================================
Alumno: Arnau Cerro
Curso: 1º DAM
Duración: 5-10 minutos

Este documento es el guión para la presentación oral del proyecto.
Cada sección incluye qué decir y qué mostrar en pantalla.

Documentos relacionados:
  - DOCUMENTACION_TECNICA.txt: Documentación técnica completa del proyecto
  - EXPLICACION_CODIGO.md: Explicación línea por línea de TODO el código fuente

===============================================================================
ESTRUCTURA DE LA DEFENSA (5-10 min)
===============================================================================
1.  Introducción y descripción del proyecto           (1 minuto)
2.  Stack tecnológico y arquitectura                   (1,5 minutos)
3.  Demostración en vivo                               (2 minutos)
4.  Identidad visual y diseño                          (30 segundos)
5.  Plan de negocio (DAFO/CAME)                       (30 segundos)
6.  Infraestructura y despliegue                       (1 minuto)
7.  Seguridad                                          (30 segundos)
8.  Copias de seguridad                                (30 segundos)
9.  Conclusión y preguntas                             (1 minuto)


===============================================================================
1. INTRODUCCIÓN Y DESCRIPCIÓN DEL PROYECTO (~1 minuto)
===============================================================================

>> MOSTRAR: Pantalla de inicio del proyecto funcionando

GUION:

"Buenos días. Mi proyecto se llama GrandSpicy.

GrandSpicy es una aplicación web de catálogo de productos picantes.
Los usuarios pueden descubrir salsas, especias y snacks picantes,
ver su información, valorarlos y dejar reseñas.

Los administradores tienen un panel para gestionar productos y usuarios.

El objetivo es crear una plataforma donde la comunidad del picante
pueda compartir opiniones y descubrir nuevos productos.

Está dirigido a aficionados al picante, tiendas del sector,
y cualquier persona interesada en la gastronomía picante."

>> MOSTRAR: Si tienes el logo, enséñalo aquí

"Los casos de uso principales son:
  - Visitante: ver catálogo, ver detalle de producto, registrarse
  - Usuario: iniciar sesión, escribir reseñas, ver perfil
  - Admin: panel de administración, CRUD de productos, gestionar usuarios"


===============================================================================
2. STACK TECNOLÓGICO Y ARQUITECTURA (~1,5 minutos)
===============================================================================

>> MOSTRAR: Diagrama de arquitectura (de la documentación técnica)

GUION:

"La aplicación sigue el patrón MVC:

  - El MODELO son los POJOs: Producto, Usuario, Resena.
  - La VISTA son las JSP con JSTL.
  - El CONTROLADOR son los Servlets.
   - El ACCESO A DATOS es una única clase BaseDatos.java con JDBC puro.

NO se ha usado Spring Boot, JPA ni Hibernate.
Todo es Java EE puro: Servlets + JSP + JDBC + JSTL,
que es lo que hemos dado en 1º de DAM.

El backend está en Java 17 con Servlets 4.0.
El frontend son páginas JSP que generan HTML en el servidor.
La base de datos es MySQL 8.0 con tres tablas relacionadas:
  - users: almacena los usuarios con su rol (USER o ADMIN)
  - products: almacena los productos, incluyendo la imagen como BLOB
  - reviews: almacena las reseñas, con claves foráneas a users y products

El servidor web es Apache Tomcat 9.
Para compilar usamos Maven.
Para desplegar usamos Docker y Docker Compose.

Los puertos utilizados:
  - 8080 para la aplicación web (Tomcat)
  - 3306 para la base de datos MySQL (solo redes interna Docker)
  - 443 para HTTPS en producción"

>> MOSTRAR: El esquema de la BD (sección 8 de la documentación)

"La BD tiene tres tablas en MySQL.
La tabla reviews tiene claves foráneas: user_id apunta a users,
product_id apunta a products.
Esto asegura la integridad referencial y con ON DELETE CASCADE
si borramos un usuario o producto, se borran sus reseñas automáticamente."


===============================================================================
3. DEMOSTRACIÓN EN VIVO (~2 minutos)
===============================================================================

>> ABRIR: http://localhost:8080 (o la URL de AWS)

GUION:

"Voy a hacer una demostración rápida del funcionamiento."

>> 3.1. PÁGINA PRINCIPAL

"Hago clic en Home. Vemos la página principal con el catálogo de productos
y las reseñas recientes. Todo viene de la base de datos."

>> 3.2. CATÁLOGO

"Vamos al catálogo. Aquí vemos todos los productos disponibles.
Cada tarjeta muestra nombre, precio, nivel de picor Scoville,
país de origen y valoración."

>> 3.3. DETALLE DE PRODUCTO

"Hago clic en un producto. Vemos la imagen, la descripción completa,
los datos del producto y las reseñas de otros usuarios."

>> 3.4. REGISTRO

"Me registro como nuevo usuario."

>> 3.5. INICIO DE SESIÓN

"Inicio sesión con el usuario que acabo de crear."

>> 3.6. ESCRIBIR RESEÑA

"Vuelvo a un producto y escribo una reseña.
Al enviarla, se recarga la página y aparece mi reseña."

>> 3.7. PERFIL

"Voy a mi perfil. Aquí veo todas las reseñas que he escrito."

>> 3.8. PANEL DE ADMIN (si eres ADMIN)

"Inicio sesión como admin / admin1234.
Entro al panel de administración.
Voy a productos, edito un producto, cambio el precio.
Elimino un producto.
Voy a usuarios y veo el listado."

>> 3.9. SEGURIDAD

"Intento acceder a /admin sin estar logueado.
El filtro me redirige automáticamente a /login."


===============================================================================
4. IDENTIDAD VISUAL Y DISEÑO (~30 segundos)
===============================================================================

>> MOSTRAR: Logotipo de GrandSpicy

GUION:

"He creado un logotipo completo para GrandSpicy, disponible en:
https://github.com/arniecer/logosPI
Incluye versión completa, icono, versión monocromática,
y versiones para fondo claro y oscuro.

La guía de estilo usa:
  - Tema oscuro (#0d0d0d) para aspecto moderno
  - Rojo (#d32f2f) como acento, asociado al picante
  - Tipografía 'Segoe UI' sans-serif para legibilidad

El prototipo se diseñó con Figma antes de programar."


===============================================================================
5. PLAN DE NEGOCIO (~30 segundos)
===============================================================================

>> MOSTRAR: Tabla DAFO o diapositiva

GUION:

"He realizado un análisis DAFO:

  FORTALEZAS: comunidad de usuarios, panel admin, Docker, código abierto.
  DEBILIDADES: marca nueva, sin app móvil, contenido inicial limitado.
  OPORTUNIDADES: crecimiento del interés por picante, nicho sin dominante.
  AMENAZAS: competencia de Amazon, cambios en AWS.

Y la propuesta CAME:
  - CORREGIR: redes sociales, mejorar responsive
  - AFRONTAR: diferenciarse con la comunidad
  - MANTENER: código limpio y documentado
  - EXPLOTAR: SEO, blog de recetas

Económicamente es viable con unos costes iniciales de ~20€/año
(AWS Free Tier + dominio)."


===============================================================================
6. INFRAESTRUCTURA Y DESPLIEGUE (~1 minuto)
===============================================================================

>> MOSTRAR: docker-compose.yml y estructura del repositorio

GUION:

"El despliegue en producción se hace con Docker en AWS EC2.

El docker-compose.yml levanta dos contenedores:
  - grandspicy-db: MySQL 8.0 con los datos persistentes
    - Incluye healthcheck: espera a que MySQL acepte conexiones
     - Ejecuta init.sql al iniciar (tablas, datos de ejemplo, permisos)
     - init.sql incluye CREATE USER y GRANT para root@'%'
    - restart: unless-stopped
  - grandspicy-app: Tomcat 9 con la aplicación
    - Depende de grandspicy-db con condition: service_healthy
      (la app no arranca hasta que MySQL está listo)
    - restart: unless-stopped

El dominio sería grandspicy.com (simulado).
El certificado SSL se obtendría con Let's Encrypt.
El tráfico HTTP redirigiría a HTTPS.

El código está en GitHub:
  https://github.com/arniecer/proyecto_jdbc

El repositorio está organizado con:
  - Código fuente en src/
  - Configuración Maven en pom.xml
  - Despliegue Docker en Dockerfile y docker-compose.yml
  - Documentación en DOCUMENTACION_TECNICA.txt
  - .gitignore para excluir compilados y temporales"

>> NOTA: Si la app NO está desplegada en AWS por falta de recursos,
>> explica que está preparada para desplegarse y muestra
>> docker-compose.yml funcionando en local.


===============================================================================
7. SEGURIDAD (~30 segundos)
===============================================================================

GUION:

"La seguridad se basa en varios niveles:

  1. AUTENTICACIÓN: por sesión (HttpSession).
     Cuando te logueas, el usuario se guarda en la sesión.

  2. AUTORIZACIÓN: el FiltroAutenticacion protege las rutas sensibles:
     /admin/* solo para ADMIN, /profile y /review para cualquier
     usuario logueado. Si no estás autenticado, te redirige a /login.

  3. CONTRA INYECCIÓN SQL: todas las consultas usan PreparedStatement
     con parámetros (?). Nunca concatenamos SQL con datos del usuario.
     Esto evita ataques de inyección SQL.

  4. RED: en Docker la base de datos NO está expuesta al exterior.
     Solo la aplicación puede conectar a MySQL.

  5. CONTRASEÑAS CIFRADAS: no están en texto plano.
     Usamos SHA-256 con salt aleatorio de 16 bytes.
     El formato guardado en BD es "salt:hash" en base64.
     Para verificarlas, el código extrae el salt, recalcula
     el hash y compara con MessageDigest.isEqual().
     Esto lo hace la clase PasswordUtil.

  6. HTTPS: en producción se usaría Let's Encrypt con Certbot."


===============================================================================
8. COPIAS DE SEGURIDAD (~30 segundos)
===============================================================================

GUION:

"La política de copias de seguridad incluye:

  - COPIA TOTAL semanal (domingos):
    mysqldump completo de la base de datos.

  - COPIA INCREMENTAL diaria:
    solo los cambios desde la última copia.

  - Las copias se automatizan con un script bash
    ejecutado por cron.

  - Para restaurar, basta con ejecutar el dump SQL
    en MySQL: mysql -u root -p < backup.sql

  Comando para hacer un backup manual ahora mismo:
    docker exec grandspicy-db mysqldump -u root -p$DB_PASSWORD
      --databases grandspicy > backup.sql"

===============================================================================
9. CONCLUSIÓN Y PREGUNTAS (~1 minuto)
===============================================================================

GUION:

"En resumen, GrandSpicy es una aplicación web completa que integra:

  - Una base de datos MySQL relacional con 3 tablas
  - Un frontend dinámico con JSP + JSTL
  - Un backend con Servlets + JDBC
  - Autenticación y control de acceso
  - Despliegue automatizado con Docker

Todo el código está en GitHub, documentado y preparado
para desplegarse en AWS.

Y lo más importante: usa solo tecnologías de 1º de DAM.
No hay Spring Boot, no hay JPA, no hay frameworks externos."

"Muchas gracias. Estoy abierto a vuestras preguntas."


===============================================================================
POSIBLES PREGUNTAS Y RESPUESTAS
===============================================================================

P: ¿Por qué no usaste Spring Boot si es más moderno?
R: Porque el enunciado dice: "No s'admetran tecnologies o conceptes
   que no hagen segut treballats durant el curs". Spring Boot no lo
   hemos dado en 1º DAM. He usado Servlets, JSP, JDBC y JSTL, que
   es lo que hemos visto en clase.

P: ¿Cómo guardas las contraseñas? ¿Son seguras?
R: No están en texto plano. Las cifro con SHA-256 y un salt
   aleatorio de 16 bytes. El salt se genera con SecureRandom
   y el hash con MessageDigest. En la BD se guarda como
   "salt:hash" en base64. Para verificar, extraigo el salt,
   recalculo el hash y comparo con MessageDigest.isEqual()
   que es resistente a ataques de temporización.
   La clase PasswordUtil encapsula toda esta lógica.
 
P: Explícame cómo organizaste el acceso a base de datos.
R: Toda la lógica de BD está en una única clase: BaseDatos.java.
    Agrupa todos los métodos estáticos: obtenerProductos(),
    guardarProducto(), buscarUsuarioPorNombreUsuario(), guardarResena(),
    etc. Los Servlets llaman directamente a BaseDatos.metodo().
    Antes tenía 5 clases separadas (DatabaseUtil, DatabaseInit,
    ProductoDAO, UsuarioDAO, ResenaDAO), pero las fusioné en una
    para simplificar. Para 1º DAM es más claro tener todo en un
    solo archivo que puedes abrir y ver todas las consultas SQL
    de la aplicación.
    (Ver EXPLICACION_CODIGO.md sección 3 para el análisis detallado
    de cada método de BaseDatos.java)

 P: ¿Por qué no usaste JavaScript?
R: Porque no es necesario. Las JSP generan HTML completo en el servidor.
   JavaScript no lo hemos dado aún en profundidad en 1º DAM.

P: ¿Cómo evitas la inyección SQL?
R: Uso PreparedStatement con parámetros (?) en todas las consultas.
   Los datos del usuario nunca se concatenan directamente en el SQL.

P: ¿Qué harías si la aplicación creciera?
R: Añadiría un sistema de caché, pasaría a Spring Boot para
   simplificar el código, y separaría frontend y backend con
   una API REST.

P: ¿Por qué elegiste MySQL y no otra BD?
R: Porque MySQL es la que hemos usado durante el curso y es la
   más compatible con Java y JDBC.

P: Explícame cómo funciona el flujo cuando alguien se loguea.
R: 1. El usuario rellena el formulario de login.
   2. El navegador hace POST a /login con username y password.
   3. AutenticacionServlet.doPost() recibe los datos.
    4. Llama a BaseDatos.buscarUsuarioPorNombreUsuario(username) que ejecuta
      un SELECT con PreparedStatement.
   5. Si el usuario existe, llama a PasswordUtil.verificar(password, usuario.getContrasena()).
      - Este método extrae el salt del formato "salt:hash" guardado en BD.
      - Calcula SHA-256(salt + password introducida).
      - Compara el resultado con el hash guardado usando MessageDigest.isEqual().
   6. Si coincide:
      - Guarda el objeto Usuario en la sesión HTTP.
      - Redirige a /home o /admin según el rol.
   7. Si no: vuelve a login.jsp con mensaje de error.

P: ¿Y cómo proteges las rutas de administración?
R: Con un FiltroAutenticacion que se ejecuta antes que cualquier
   Servlet. Comprueba la sesión y el rol. Si no es ADMIN en rutas
   /admin, redirige a /login. El Servlet ni siquiera llega a ejecutarse.

P: ¿Has probado la aplicación en otros navegadores?
R: Sí, funciona en Chrome, Firefox y Edge. Es HTML y CSS estándar.

P: ¿Cuánto tiempo te llevó hacerlo?
R: [Responde con honestidad según tu caso]

P: Explícame la clase PasswordUtil.
R: Está en util/PasswordUtil.java. Tiene dos métodos estáticos:
    - cifrar(contrasena): genera un salt aleatorio de 16 bytes
      con SecureRandom, concatena salt+contraseña, calcula
      SHA-256 con MessageDigest, y devuelve "base64(salt):base64(hash)".
    - verificar(contrasena, hashAlmacenado): separa el salt del hash,
      recalcula SHA-256(salt+contraseña) y compara con
      MessageDigest.isEqual() que es seguro contra ataques de tiempo.
    No usa ninguna librería externa, solo java.security.
    (Ver EXPLICACION_CODIGO.md sección 4 para el análisis detallado)

P: ¿Qué es el salt y por qué lo usas?
R: El salt es un valor aleatorio que se añade a la contraseña
   antes de calcular el hash. Así, aunque dos usuarios tengan la
   misma contraseña, el hash guardado será diferente. También
   impide que un atacante use tablas rainbow para descifrar
   contraseñas.

P: ¿MessageDigest.isEqual() por qué en lugar de .equals()?
R: .equals() de String deja de comparar en cuanto encuentra
   una diferencia, lo que permite ataques de temporización.
   isEqual() compara siempre todos los bytes sin importar
   si coinciden o no, siendo resistente a ese tipo de ataque.

P: ¿Qué harías si la aplicación creciera?
R: Añadiría un sistema de caché, pasaría a Spring Boot para
   simplificar el código, y separaría frontend y backend con
   una API REST.

P: ¿Por qué elegiste MySQL y no otra BD?
R: Porque MySQL es la que hemos usado durante el curso y es la
   más compatible con Java y JDBC.


===============================================================================
APÉNDICE A: CHULETA DE COMANDOS (imprimir y llevar)
===============================================================================

Arrancar la app (desarrollo):
  mvn clean compile exec:java

Arrancar solo MySQL (si usas Docker):
  docker-compose up -d grandspicy-db

Arrancar todo con Docker:
  docker compose up -d --build

Ver logs:
  docker compose logs -f grandspicy-app

Hacer backup manual:
  mysqldump -u root -p --databases grandspicy > backup.sql

Restaurar backup:
  mysql -u root -p < backup.sql

Forzar reinicio de BD (borrar datos):
  docker compose down -v && docker compose up -d

Conectar a la base de datos:
  docker compose exec mysql mysql -uroot grandspicy

Compilar sin ejecutar:
  mvn clean compile

Ver estructura del proyecto:
  tree -I 'target|tomcat*'


===============================================================================
APÉNDICE B: PLAN DE CONTINGENCIA (SI LA DEMO FALLA)
===============================================================================

Si la app NO arranca:
  "En local funciona correctamente. Déjame mostrar el código
   y la documentación mientras lo reviso."
  → Abre DOCUMENTACION_TECNICA.txt
   → Abre el código de AutenticacionServlet.java o BaseDatos.java
  → Explica el flujo señalando las líneas

Si el navegador da error 404:
  "Puede que el contexto no se haya cargado. Voy a reiniciar."
  → Ctrl+C en terminal y vuelve a ejecutar mvn clean compile exec:java

Si la BD no responde:
  "Compruebo que MySQL esté arrancado."
  → sudo systemctl start mysql  o  docker-compose up -d grandspicy-db

Si no entran las credenciales:
  "Las credenciales por defecto son admin / admin1234 y user / user123.
   Asegúrate de que la BD se haya inicializado correctamente."

Si no sabes responder una pregunta:
  "Eso no lo hemos dado en 1º de DAM, pero en producción lo haría
   de tal forma..." o "Déjame consultar la documentación..."


===============================================================================
APÉNDICE C: GUION DETALLADO DE LA DEMO (paso a paso)
===============================================================================

Antes de la defensa:
  1. Abre terminal y ejecuta: mvn clean compile exec:java
  2. Abre Chrome en http://localhost:8080
  3. Abre el código en el IDE (Eclipse/VS Code)
  4. Abre DOCUMENTACION_TECNICA.txt
  5. Abre DEFENSA_PROYECTO.md (este archivo) para leer
  6. Ten docker-compose.yml abierto por si preguntan por despliegue

Demo paso a paso:

1. PÁGINA PRINCIPAL (10s)
   Click en "Home" → muestra productos + reseñas recientes
   "Todo esto viene de la base de datos MySQL"

2. CATÁLOGO (10s)
   Click en "Catalog" → lista todos los productos
   "Cada tarjeta tiene nombre, precio, nivel Scoville y valoración"

3. DETALLE DE PRODUCTO (15s)
   Click en "Habanero XXX Sauce" → foto, descripción, reseñas
   "La imagen se sirve desde la BD como BLOB a través de ImagenServlet"

4. REGISTRO (15s)
   Click en "Sign Up" → rellena: test / test@test.com / test123 / Test
   Click "Register" → redirige a login
   "La contraseña se cifra con SHA-256 + salt antes de guardarse"

5. LOGIN (10s)
   Login: test / test123
   "PasswordUtil.verificar() compara el hash con el guardado"

6. RESEÑA (15s)
   Click en un producto → escribe reseña "Muy bueno!" valoración 5
   Click "Submit Review" → la reseña aparece abajo
   "Se hace un INSERT en la tabla reviews y se recarga la página"

7. PERFIL (10s)
   Click en "Profile" → muestra el usuario y sus reseñas
   "SELECT con JOIN entre users y reviews"

8. ADMIN (20s)
   Cerrar sesión → Login: admin / admin1234
   Click "Admin Panel" → dashboard con estadísticas
   Click "Products" → lista
   Click "Edit" en un producto → cambia precio, guarda
   Click "Delete" en otro → lo elimina
   Click "Users" → lista de usuarios
   "El filtro FiltroAutenticacion protege todas estas rutas"

9. SEGURIDAD (10s)
   Cerrar sesión → escribe en URL: http://localhost:8080/admin
   → Redirige automáticamente a /login
   "El filtro se ejecuta ANTES que el Servlet, si no hay sesión redirige"


===============================================================================
NOTAS PARA EL PROFESOR/A
===============================================================================

- La defensa debe durar entre 5 y 10 minutos.
- Es mejor que sobre tiempo a que falte.
- Lo más importante es:
  1. Que la aplicación FUNCIONE (demo en vivo)
  2. Que SEPAS explicar el código
  3. Que JUSTIFIQUES las decisiones técnicas
- Si algo falla en la demo, di "esto funciona en local"
  y muestra el código o una captura.
- Si no sabes algo, no inventes. Di "eso no lo hemos dado
  en 1º DAM" o "lo haría de otra forma en producción".
- Lleva la documentación abierta por si necesitas consultar algo.

===============================================================================
