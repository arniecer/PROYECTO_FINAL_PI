# Explicación completa del código — GrandSpicy

Documentos relacionados:
  - DOCUMENTACION_TECNICA.txt: Documentación técnica completa (arquitectura, despliegue, BD, seguridad)
  - DEFENSA_PROYECTO.md: Guión para la defensa oral (5-10 min) con preguntas frecuentes

Este documento explica LÍNEA POR LÍNEA todo el código fuente del proyecto.
Se complementa con DOCUMENTACION_TECNICA.txt (visión general del sistema)
y DEFENSA_PROYECTO.md (preparación para la defensa oral).

## Índice

1. [Estructura del proyecto](#1-estructura-del-proyecto)
2. [Paquete modelo (POJOs)](#2-paquete-modelo-pojo)
   - 2.1. Producto.java
   - 2.2. Usuario.java
   - 2.3. Resena.java
3. [Paquete dao — BaseDatos.java](#3-paquete-dao-basedatosjava)
   - 3.1. Atributos y getEnv()
   - 3.2. conectar()
   - 3.3. Métodos de productos
   - 3.4. Métodos de usuarios
   - 3.5. Métodos de reseñas
   - 3.6. inicializar()
   - 3.7. Mapeadores privados
4. [Paquete util — PasswordUtil.java](#4-paquete-util-passwordutiljava)
5. [Paquete filtro — FiltroAutenticacion.java](#5-paquete-filtro-filtroautenticacionjava)
6. [Paquete servlet (7 servlets)](#6-paquete-servlet)
   - 6.1. InicioServlet.java
   - 6.2. AutenticacionServlet.java
   - 6.3. DetalleProductoServlet.java
   - 6.4. ResenaServlet.java
   - 6.5. PerfilServlet.java
   - 6.6. AdminServlet.java
   - 6.7. ImagenServlet.java
7. [Main.java](#7-mainjava)
8. [init.sql](#8-initsql)
9. [Flujo completo de una petición](#9-flujo-completo)
10. [Conceptos clave explicados](#10-conceptos-clave)

---

## 1. Estructura del proyecto

```
src/main/java/com/grandspicy/
├── Main.java                 ← Punto de entrada (Tomcat embebido)
├── dao/
│   └── BaseDatos.java        ← TODO el acceso a base de datos (1 sola clase)
├── filtro/
│   └── FiltroAutenticacion.java  ← Protege rutas (login requerido)
├── modelo/
│   ├── Producto.java         ← Datos de un producto
│   ├── Usuario.java          ← Datos de un usuario
│   └── Resena.java           ← Datos de una reseña
├── servlet/
│   ├── AdminServlet.java     ← Panel de administración (CRUD)
│   ├── AutenticacionServlet.java  ← Login / registro / logout
│   ├── DetalleProductoServlet.java ← Ver detalle de producto
│   ├── ImagenServlet.java    ← Servir imágenes desde la BD
│   ├── InicioServlet.java    ← Home y catálogo
│   ├── PerfilServlet.java    ← Perfil del usuario
│   └── ResenaServlet.java    ← Escribir reseña
└── util/
    └── PasswordUtil.java     ← Cifrar y verificar contraseñas
```

**Arquitectura MVC:**

| Capa | Qué es | Cómo se llama |
|---|---|---|
| **Modelo** | Clases que representan datos (POJOs) | `Producto`, `Usuario`, `Resena` |
| **Vista** | JSP que generan HTML | `index.jsp`, `catalog.jsp`, ... |
| **Controlador** | Servlets que reciben peticiones HTTP | `InicioServlet`, `AdminServlet`, ... |
| **Acceso a datos** | Una clase con JDBC puro | `BaseDatos` (17 métodos estáticos) |

---

## 2. Paquete modelo (POJOs)

Los POJOs (Plain Old Java Objects) son clases simples con:
- Atributos privados (los datos)
- Constructor vacío y/ocon parámetros
- Getters y setters (métodos para leer/escribir cada atributo)

### 2.1. Producto.java (49 líneas)

```java
package com.grandspicy.modelo;
import java.util.Date;

public class Producto {
```

**Línea 6:** `public class Producto` — Declara la clase, pública para que cualquier otra clase pueda usarla. Una clase es como un "molde" para crear objetos. Producto representa una fila de la tabla `products` de MySQL.

**Línea 8:** `private Long id;` — `Long` es el tipo "objeto" de `long` (número entero grande). Se usa `Long` en lugar de `long` porque puede ser `null` (cuando el producto aún no se ha guardado en la BD, no tiene ID). Es la clave primaria en la tabla.

**Línea 9:** `private String nombre;` — `String` es una cadena de texto. Guarda el nombre del producto (ej: "Habanero XXX Sauce").

**Línea 10:** `private String descripcion;` — Texto largo con la descripción del producto.

**Línea 11:** `private Double precio;` — `Double` (objeto, puede ser null). El precio del producto.

**Línea 12:** `private String imagen;` — Nombre del archivo de imagen (ej: "habanero.jpg"). Solo el nombre, no el contenido.

**Línea 13:** `private byte[] imagenDatos;` — `byte[]` es un array de bytes. Aquí se guarda el contenido binario de la imagen (lo que en MySQL es un BLOB). Cuando el admin sube una foto, se guarda aquí como bytes. SI tiene datos, se usa; si no, el JSP muestra un placeholder.

**Línea 14:** `private String categoria;` — Categoría del producto: Salsas, Especias, Snacks, Aceites.

**Línea 15:** `private Integer nivelScoville;` — `Integer` (objeto, puede ser null). Escala Scoville que mide el picor (0-16.000.000). Null si no aplica.

**Línea 16:** `private String paisOrigen;` — País de origen del producto.

**Línea 17:** `private String enlaceCompra;` — URL donde comprar el producto.

**Línea 18:** `private Double valoracion;` — Valoración media del producto (0.0 a 5.0).

**Línea 19:** `private Date fechaCreacion;` — `java.util.Date`. Cuándo se creó el producto en la BD.

**Línea 21:** `public Producto() { this.valoracion = 0.0; }` — Constructor sin parámetros. Inicializa valoracion a 0.0 por defecto. Se llama con `new Producto()`.

**Línea 25:** `public Long getId() { return id; }` — Getter. Método público que devuelve el valor del atributo privado `id`. Se llama con `producto.getId()`.

**Línea 26:** `public void setId(Long id) { this.id = id; }` — Setter. Método público que asigna un valor al atributo privado `id`. `this.id` se refiere al atributo de la clase, `id` (sin this) es el parámetro del método. Se llama con `producto.setId(5L)`.

**Líneas 25-48:** Getters y setters del resto de atributos. Cada atributo privado tiene su getter (lectura) y setter (escritura). Es el estándar JavaBean.

> **Concepto: POJO** — Plain Old Java Object. Una clase que solo tiene atributos privados, constructor vacío, y getters/setters. No tiene lógica de negocio, solo datos.

### 2.2. Usuario.java (40 líneas)

```java
public class Usuario {
    private Long id;
    private String usuario;     // Nombre de usuario (login)
    private String email;       // Correo electrónico
    private String contrasena;  // Contraseña cifrada (NUNCA en texto plano)
    private String rol;         // "USER" o "ADMIN"
    private String nombre;      // Nombre real o nickname
    private Date fechaCreacion;
```

**Línea 9:** `private String usuario;` — El nombre de usuario con el que se loguea (ej: "admin", "user"). No confundir con `nombre` que es el nombre real.

**Línea 11:** `private String contrasena;` — **Importante:** aquí nunca se guarda la contraseña en texto plano. Se guarda el resultado de `PasswordUtil.cifrar()`, que es un String en formato `"salt:hash"` en base64.

**Línea 12:** `private String rol;` — El rol determina qué puede hacer el usuario: `"USER"` (puede ver y reseñar) o `"ADMIN"` (puede gestionar productos y usuarios).

**Línea 16:** `public Usuario() {}` — Constructor vacío. Se usa cuando se crea un usuario desde los datos de la BD (en `mapearUsuario`).

**Línea 18:** `public Usuario(String usuario, String email, String contrasena, String rol, String nombre)` — Constructor con parámetros. Se usa en `AutenticacionServlet.registrar()` para crear un usuario nuevo con todos los datos.

### 2.3. Resena.java (36 líneas)

```java
public class Resena {
    private Long id;
    private Usuario usuario;    // OBJETO Usuario completo, no solo un ID
    private Producto producto;  // OBJETO Producto completo
    private Integer valoracion; // 1 a 5
    private String comentario;  // Texto de la reseña
    private Date fechaCreacion;
```

**Línea 9:** `private Usuario usuario;` — **Atención:** esto NO es un `Long userId`, es un OBJETO `Usuario` completo. La reseña "tiene un" usuario. Esto es composición de objetos (OOP real). Cuando cargamos una reseña de la BD, también cargamos el usuario que la escribió.

**Línea 10:** `private Producto producto;` — Lo mismo: la reseña contiene el objeto `Producto` completo al que pertenece. Para cargarlo, `mapearResena()` llama a `buscarProductoPorId()`.

**Línea 17:** `public Resena(Usuario usuario, Producto producto, Integer valoracion, String comentario)` — Constructor que recibe los objetos completos. Se usa en `ResenaServlet` para crear una reseña nueva: `new Resena(usuario, producto, 5, "Muy bueno!")`.

> Concepto: **relaciones entre objetos** — Una Resena TIENE UN Usuario y TIENE UN Producto. En la BD esto se representa con claves foráneas (`user_id`, `product_id`). En Java se representa teniendo atributos del tipo de los objetos relacionados.

---

## 3. Paquete dao — BaseDatos.java (376 líneas)

Este es el archivo más importante. Es la ÚNICA clase que habla con MySQL. Todos los métodos son `static` (se llaman sin crear objetos: `BaseDatos.obtenerProductos()`).

### 3.1. Atributos y getEnv()

**Línea 12:** `public class BaseDatos {` — La clase es pública. Solo tiene métodos estáticos, no necesita constructor.

**Línea 14:** `private static boolean driverCargado = false;` — Variable booleana (true/false) que recuerda si ya hemos cargado el driver JDBC de MySQL. Se usa en `conectar()` para cargarlo solo una vez. Inicialmente `false`.

**Línea 16:** `private static String getEnv(String variable, String defecto)` — Método privado (solo se usa dentro de esta clase) y estático (no necesita objeto). Sirve para leer variables de entorno del sistema operativo.

**Línea 17:** `String valor = System.getenv(variable);` — `System.getenv()` busca una variable de entorno. Por ejemplo, `System.getenv("DB_URL")` devuelve el valor de la variable de entorno `DB_URL`, o `null` si no existe.

**Línea 18-20:** `if (valor != null) { return valor; }` — Si la variable de entorno existe, devuelve su valor.

**Línea 21:** `return defecto;` — Si no existe, devuelve el valor por defecto (el segundo parámetro).

> **¿Por qué existe getEnv?** — Para poder configurar la conexión a MySQL sin cambiar el código. En desarrollo usamos valores por defecto (`localhost:3306`, `root`, `root`). En producción (Docker), las variables de entorno `DB_URL`, `DB_USER`, `DB_PASSWORD` se configuran en `docker-compose.yml`.

### 3.2. conectar()

**Línea 24:** `private static Connection conectar() throws SQLException` — Método privado que crea y devuelve una conexión a la BD. Lanza `SQLException` si algo falla (BD no disponible, credenciales incorrectas, etc.). `Connection` es un objeto de Java que representa una conexión abierta a MySQL.

**Línea 25:** `if (!driverCargado) {` — Si el driver NO se ha cargado aún (`!` es negación, `!false = true` la primera vez)...

**Línea 27:** `Class.forName("com.mysql.cj.jdbc.Driver");` — Carga la clase del driver JDBC de MySQL en la memoria. Sin esto, Java no sabe cómo conectarse a MySQL. "com.mysql.cj.jdbc.Driver" es el nombre completo de la clase (con paquete) del driver MySQL 8.

**Línea 28:** `driverCargado = true;` — Marcamos que ya está cargado para no repetirlo.

**Línea 29-30:** `catch (ClassNotFoundException e) { throw new SQLException(...); }` — Si la clase del driver no se encuentra (por ejemplo, falta el JAR de MySQL en el classpath), capturamos la excepción y lanzamos una SQLException.

**Línea 33-37:** `return DriverManager.getConnection(url, user, password);` — `DriverManager.getConnection()` es el método estándar de JDBC para abrir una conexión. Recibe:
1. URL de la BD: `jdbc:mysql://localhost:3306/grandspicy?...` — el protocolo es `jdbc:mysql://`, seguido del servidor (`localhost`), puerto (`3306`), y nombre de la BD (`grandspicy`). Los parámetros `?useSSL=false&...` son opciones de conexión.
2. Usuario: de la variable de entorno `DB_USER` o "root" por defecto.
3. Contraseña: de la variable de entorno `DB_PASSWORD` o "root" por defecto.

> **Concepto: JDBC** — Java Database Connectivity. Es la API estándar de Java para conectarse a bases de datos relacionales. Usamos `DriverManager.getConnection()` para abrir una conexión, `PreparedStatement` para ejecutar SQL, y `ResultSet` para leer resultados.

### 3.3. Métodos de productos

#### obtenerProductos()

```java
public static List<Producto> obtenerProductos() {
    List<Producto> lista = new ArrayList<>();
    String sql = "SELECT * FROM products ORDER BY created_at DESC";
    try (Connection c = conectar();
         Statement s = c.createStatement();
         ResultSet rs = s.executeQuery(sql)) {
        while (rs.next()) {
            lista.add(mapearProducto(rs));
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return lista;
}
```

**Línea 42:** `public static List<Producto> obtenerProductos()` — Método público y estático. Devuelve una `List<Producto>` (lista de objetos Producto). Los servlets llaman `BaseDatos.obtenerProductos()`.

**Línea 43:** `List<Producto> lista = new ArrayList<>();` — Crea una lista vacía donde iremos metiendo los productos.

**Línea 44:** `String sql = "SELECT * FROM products ORDER BY created_at DESC";` — Consulta SQL que selecciona TODAS las columnas (`*`) de la tabla `products`, ordenadas por fecha de creación de más reciente a más antigua (`DESC`).

**Línea 45-47:** `try (Connection c = conectar(); Statement s = c.createStatement(); ResultSet rs = s.executeQuery(sql))` — Esto es **try-with-resources** (Java 7+). Los recursos se abren entre paréntesis y se cierran automáticamente al salir del try, aunque haya error.
- `conectar()` → abre conexión a MySQL
- `c.createStatement()` → crea un objeto Statement para ejecutar SQL
- `s.executeQuery(sql)` → ejecuta la SELECT y devuelve un ResultSet (tabla de resultados)

**Línea 48-50:** `while (rs.next()) { lista.add(mapearProducto(rs)); }` — `rs.next()` avanza a la siguiente fila del ResultSet. Devuelve `false` cuando no hay más filas. Por cada fila, llama a `mapearProducto(rs)` que convierte la fila en un objeto `Producto`, y lo añade a la lista.

**Línea 51-52:** `catch (SQLException e) { e.printStackTrace(); }` — Si hay error SQL (BD caída, tabla no existe, etc.), imprime el error en la consola. El programa no se rompe, solo devuelve la lista vacía.

**Línea 54:** `return lista;` — Devuelve la lista de productos (puede estar vacía si no hay productos o si hubo error).

> **Concepto: try-with-resources** — Es una forma de asegurar que los recursos (Connection, Statement, ResultSet) se cierran automáticamente. Es como un `try { ... } finally { recurso.close(); }` automático.

#### buscarProductoPorId()

```java
public static Producto buscarProductoPorId(Long id) {
    String sql = "SELECT * FROM products WHERE id = ?";
    try (Connection c = conectar();
         PreparedStatement ps = c.prepareStatement(sql)) {
        ps.setLong(1, id);
        try (ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return mapearProducto(rs);
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return null;
}
```

**Línea 57:** `public static Producto buscarProductoPorId(Long id)` — Devuelve un SOLO producto (o `null` si no existe).

**Línea 58:** `String sql = "SELECT * FROM products WHERE id = ?";` — SQL con parámetro `?`. El `?` es un marcador de posición que se reemplaza con el valor real en tiempo de ejecución.

**Línea 60:** `PreparedStatement ps = c.prepareStatement(sql)` — `PreparedStatement` es como un Statement pero más seguro: permite usar parámetros `?` y PREVIENE la inyección SQL. El SQL se "precompila" en la BD y luego se le pasan los parámetros.

**Línea 61:** `ps.setLong(1, id);` — Asigna el valor `id` al primer parámetro `?` (el índice 1). Si `id` es 5, el SQL se convierte en: `SELECT * FROM products WHERE id = 5`.

**Línea 62:** `try (ResultSet rs = ps.executeQuery())` — Ejecuta la consulta y obtiene un ResultSet con los resultados.

**Línea 63:** `if (rs.next())` — Como solo esperamos 0 o 1 resultado, usamos `if` en lugar de `while`. Si hay resultado...

**Línea 64:** `return mapearProducto(rs);` — Convierte la fila en objeto Producto y lo devuelve directamente.

**Línea 70:** `return null;` — Si no se encontró el producto (o hubo error), devuelve `null`.

> **Concepto: PreparedStatement vs Statement** — `Statement` se usa para SQL sin parámetros (ej: `SELECT * FROM products`). `PreparedStatement` se usa cuando hay parámetros (ej: `WHERE id = ?`). PreparedStatement es SEGURO contra inyección SQL: los valores se pasan por separado, no se concatenan en el SQL.

#### buscarProductoPorNombre()

**Línea 73-87:** Igual que `buscarProductoPorId()` pero busca por `name = ?` en lugar de `id = ?`. Se usa en AdminServlet para comprobar duplicados (aunque esa comprobación ya no se hace en el servlet).

#### asignarParametrosProducto()

```java
private static void asignarParametrosProducto(PreparedStatement ps, Producto p) throws SQLException {
    ps.setString(1, p.getNombre());
    ps.setString(2, p.getDescripcion());
    ps.setDouble(3, p.getPrecio());
    ps.setString(4, p.getImagen());
    if (p.getImagenDatos() != null) {
        ps.setBytes(5, p.getImagenDatos());
    } else {
        ps.setNull(5, Types.BLOB);
    }
    ps.setString(6, p.getCategoria());
    if (p.getNivelScoville() != null) {
        ps.setInt(7, p.getNivelScoville());
    } else {
        ps.setNull(7, Types.INTEGER);
    }
    ps.setString(8, p.getPaisOrigen());
    ps.setString(9, p.getEnlaceCompra());
    ps.setDouble(10, p.getValoracion());
}
```

**Línea 89:** Método PRIVADO y estático. No devuelve nada (`void`). Recibe un `PreparedStatement` (que ya tiene el SQL) y un `Producto`. Asigna los 10 primeros parámetros del SQL con los valores del producto.

**Línea 90-93:** `setString(1, nombre)`, `setString(2, descripcion)`, `setDouble(3, precio)`, `setString(4, imagen)` — Asigna Strings y un Double a los parámetros 1-4.

**Línea 94-97:** `if (p.getImagenDatos() != null) { ps.setBytes(5, ...); } else { ps.setNull(5, Types.BLOB); }` — Si el producto tiene datos de imagen, los asigna como bytes. Si no, asigna NULL. `Types.BLOB` indica que la columna es de tipo BLOB en MySQL.

**Línea 99-104:** Lo mismo para `nivelScoville`: si tiene valor, asigna entero; si no, NULL.

**Línea 105-107:** `setString(8, paisOrigen)`, `setString(9, enlaceCompra)`, `setDouble(10, valoracion)`.

> **¿Por qué es privado?** — Porque solo `insertarProducto()` y `actualizarProducto()` lo necesitan. Al ser privado, no "ensucia" la API pública de la clase.

#### insertarProducto() y actualizarProducto()

```java
public static void insertarProducto(Producto p) {
    String sql = "INSERT INTO products (...) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    try (Connection c = conectar();
         PreparedStatement ps = c.prepareStatement(sql)) {
        asignarParametrosProducto(ps, p);
        ps.setTimestamp(11, new Timestamp(System.currentTimeMillis()));
        ps.executeUpdate();
    } catch (SQLException e) {
        e.printStackTrace();
    }
}
```

**Línea 110:** `public static void insertarProducto(Producto p)` — Inserta un NUEVO producto. No devuelve nada.

**Línea 111:** SQL INSERT con 11 parámetros `?`. La columna `created_at` se rellena con la fecha actual.

**Línea 113:** `asignarParametrosProducto(ps, p);` — Asigna los 10 primeros parámetros (name, description, price, image, image_data, category, scoville_level, country_of_origin, purchase_link, rating).

**Línea 115:** `ps.setTimestamp(11, new Timestamp(System.currentTimeMillis()));` — Parámetro 11: la fecha de creación. `System.currentTimeMillis()` da los milisegundos actuales, `new Timestamp(...)` los convierte a formato SQL TIMESTAMP.

**Línea 116:** `ps.executeUpdate();` — Ejecuta el INSERT. `executeUpdate()` se usa para INSERT, UPDATE, DELETE (operaciones que modifican datos). Devuelve el número de filas afectadas (pero no lo usamos).

```java
public static void actualizarProducto(Producto p) {
    String sql = "UPDATE products SET name=?, ..., rating=? WHERE id=?";
    try (Connection c = conectar();
         PreparedStatement ps = c.prepareStatement(sql)) {
        asignarParametrosProducto(ps, p);
        ps.setLong(11, p.getId());
        ps.executeUpdate();
    } catch (SQLException e) {
        e.printStackTrace();
    }
}
```

**Línea 122:** `public static void actualizarProducto(Producto p)` — Actualiza un producto EXISTENTE. Necesita que `p.getId()` NO sea null.

**Línea 123:** SQL UPDATE: los parámetros 1-10 son los mismos que en INSERT (vía `asignarParametrosProducto`), el parámetro 11 es el ID del producto (en el `WHERE id=?`).

**Línea 127:** `ps.setLong(11, p.getId());` — El ID va en la posición 11, después de los 10 campos.

> **¿Por qué dos métodos separados?** — Antes era un solo método `guardarProducto()` que decidía si era INSERT o UPDATE según si el producto tenía ID. Separándolos, el código es más explícito: el servlet decide qué operación hacer.

#### eliminarProducto()

```java
public static void eliminarProducto(Long id) {
    String sql = "DELETE FROM products WHERE id = ?";
    try (Connection c = conectar();
         PreparedStatement ps = c.prepareStatement(sql)) {
        ps.setLong(1, id);
        ps.executeUpdate();
    } catch (SQLException e) {
        e.printStackTrace();
    }
}
```

**Línea 134-143:** DELETE FROM con WHERE id = ?. El método más simple de la clase.

### 3.4. Métodos de usuarios

#### buscarUsuarioPorNombreUsuario()

```java
public static Usuario buscarUsuarioPorNombreUsuario(String username) {
    String sql = "SELECT * FROM users WHERE username = ?";
    try (Connection c = conectar();
         PreparedStatement ps = c.prepareStatement(sql)) {
        ps.setString(1, username);
        try (ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return mapearUsuario(rs);
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return null;
}
```

**Línea 147-161:** Busca un usuario por su nombre de usuario (campo `username`). Devuelve el objeto `Usuario` completo o `null` si no existe. Es el método usado en el login para comprobar si el usuario existe.

#### buscarUsuarioPorId()

**Línea 163-177:** Igual pero busca por `id`. Menos usado, pero necesario para完整idad.

#### obtenerUsuarios()

**Línea 179-192:** `SELECT * FROM users ORDER BY created_at DESC`. Devuelve una `List<Usuario>` con todos los usuarios. Se usa en el panel de admin para listar usuarios.

#### guardarUsuario()

```java
public static void guardarUsuario(Usuario u) {
    String sql = "INSERT INTO users (username, email, password, role, name, created_at) VALUES (?, ?, ?, ?, ?, ?)";
    try (Connection c = conectar();
         PreparedStatement ps = c.prepareStatement(sql)) {
        ps.setString(1, u.getUsuario());
        ps.setString(2, u.getEmail());
        ps.setString(3, u.getContrasena());
        ps.setString(4, u.getRol());
        ps.setString(5, u.getNombre());
        ps.setTimestamp(6, new Timestamp(System.currentTimeMillis()));
        ps.executeUpdate();
    } catch (SQLException e) {
        e.printStackTrace();
    }
}
```

**Línea 194-208:** Inserta un nuevo usuario. **Importante:** la contraseña (`u.getContrasena()`) ya viene CIFRADA (se cifró antes con `PasswordUtil.cifrar()` en el servlet). Nunca se guarda texto plano.

#### eliminarUsuario()

**Línea 210-219:** `DELETE FROM users WHERE id = ?`. Elimina un usuario. Las reseñas de ese usuario también se borran automáticamente gracias a `ON DELETE CASCADE` en la BD.

### 3.5. Métodos de reseñas

#### obtenerResenasPorProducto()

```java
public static List<Resena> obtenerResenasPorProducto(Long productoId) {
    List<Resena> lista = new ArrayList<>();
    String sql = "SELECT r.*, u.username, u.name as nombre_usuario, u.email, "
               + "u.role, u.password as contrasena_usuario, u.created_at as fecha_usuario "
               + "FROM reviews r JOIN users u ON r.user_id = u.id "
               + "WHERE r.product_id = ? ORDER BY r.created_at DESC";
    try (Connection c = conectar();
         PreparedStatement ps = c.prepareStatement(sql)) {
        ps.setLong(1, productoId);
        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(mapearResena(rs));
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return lista;
}
```

**Línea 225:** SQL con JOIN — esta es la consulta más compleja de la aplicación:

```sql
SELECT r.*, u.username, u.name as nombre_usuario, u.email,
       u.role, u.password as contrasena_usuario, u.created_at as fecha_usuario
FROM reviews r
JOIN users u ON r.user_id = u.id
WHERE r.product_id = ?
ORDER BY r.created_at DESC
```

- `r.*` — todas las columnas de la tabla reviews
- `u.username, u.name as nombre_usuario, ...` — columnas de la tabla users, con alias (AS) para que tengan nombres descriptivos en el ResultSet
- `FROM reviews r JOIN users u ON r.user_id = u.id` — combina cada reseña con el usuario que la escribió. `r` y `u` son alias de las tablas.
- `WHERE r.product_id = ?` — solo reseñas del producto indicado
- `ORDER BY r.created_at DESC` — de más reciente a más antigua

> **Concepto: JOIN** — Combina filas de dos tablas basándose en una relación. Aquí: por cada reseña, busca el usuario cuyo `id` coincide con `reviews.user_id`, y trae sus columnas (username, name, email, etc.) junto con los datos de la reseña.

#### obtenerResenasPorUsuario()

**Línea 240-255:** Mismo JOIN pero filtra por `r.user_id = ?` en lugar de `product_id`. Se usa en el perfil del usuario.

#### obtenerResenasRecientes()

**Línea 257-272:** Mismo JOIN pero sin WHERE, con `LIMIT ?`. Se usa en la página principal para mostrar las últimas 6 reseñas.

#### guardarResena()

```java
public static void guardarResena(Resena r) {
    String sql = "INSERT INTO reviews (user_id, product_id, rating, comment, created_at) VALUES (?, ?, ?, ?, ?)";
    try (Connection c = conectar();
         PreparedStatement ps = c.prepareStatement(sql)) {
        ps.setLong(1, r.getUsuario().getId());
        ps.setLong(2, r.getProducto().getId());
        ps.setInt(3, r.getValoracion());
        ps.setString(4, r.getComentario());
        ps.setTimestamp(5, new Timestamp(System.currentTimeMillis()));
        ps.executeUpdate();
    } catch (SQLException e) {
        e.printStackTrace();
    }
}
```

**Línea 274-287:** Inserta una reseña nueva. Recibe un objeto `Resena` que ya contiene el `Usuario` y el `Producto` asociados. Extrae los IDs con `r.getUsuario().getId()` y `r.getProducto().getId()` para guardarlos en las columnas `user_id` y `product_id`.

### 3.6. inicializar()

```java
public static void inicializar() {
    try {
        try (Connection c = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC",
                getEnv("DB_USER", "root"), getEnv("DB_PASSWORD", "root"));
             Statement s = c.createStatement()) {
            s.executeUpdate("CREATE DATABASE IF NOT EXISTS grandspicy CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci");
        }

        String sql = new String(BaseDatos.class.getClassLoader()
            .getResourceAsStream("init.sql").readAllBytes(), StandardCharsets.UTF_8);
        String[] sentencias = sql.split(";");
        try (Connection c = conectar()) {
            for (String sentencia : sentencias) {
                String st = sentencia.trim();
                if (st.isEmpty() || st.startsWith("--")) continue;
                try (Statement stmt = c.createStatement()) {
                    stmt.execute(st);
                }
            }
        }

        System.out.println("[BaseDatos] Base de datos inicializada correctamente");
    } catch (Exception e) {
        System.err.println("[BaseDatos] Error al inicializar la BD: " + e.getMessage());
        e.printStackTrace();
    }
}
```

**Línea 291:** `public static void inicializar()` — Se llama desde `Main.main()` al arrancar la app. Solo en desarrollo (Tomcat embebido). Hace dos cosas:

**PASO 1 (líneas 293-298):** Crear la base de datos si no existe.
- Conecta a MySQL **sin especificar base de datos**: `jdbc:mysql://localhost:3306/?...` (sin `/grandspicy`).
- Ejecuta `CREATE DATABASE IF NOT EXISTS grandspicy CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci` — crea la BD con soporte para emojis y caracteres especiales (utf8mb4).

**PASO 2 (líneas 300-310):** Ejecutar init.sql.
- `BaseDatos.class.getClassLoader().getResourceAsStream("init.sql")` — busca el archivo `init.sql` dentro del classpath (en `src/main/resources/`).
- `.readAllBytes()` — lee todo el archivo como bytes.
- `new String(..., StandardCharsets.UTF_8)` — convierte los bytes a texto (String) usando codificación UTF-8.
- `sql.split(";")` — divide el texto por el carácter `;`, dando un array de sentencias SQL individuales.
- Bucle: por cada sentencia, la recorta (`.trim()`), salta las vacías y los comentarios (`--`), y la ejecuta.

**Importante:** En Docker, esto NO se ejecuta (no se usa Main.java). Docker monta `init.sql` en `/docker-entrypoint-initdb.d/` y MySQL lo ejecuta automáticamente al iniciar el contenedor por primera vez.

### 3.7. Mapeadores privados

Son métodos que convierten una fila de ResultSet en un objeto Java.

#### mapearProducto()

```java
private static Producto mapearProducto(ResultSet rs) throws SQLException {
    Producto p = new Producto();
    p.setId(rs.getLong("id"));
    p.setNombre(rs.getString("name"));
    p.setDescripcion(rs.getString("description"));
    p.setPrecio(rs.getDouble("price"));
    p.setImagen(rs.getString("image"));
    Blob blob = rs.getBlob("image_data");
    if (blob != null) {
        p.setImagenDatos(blob.getBytes(1, (int) blob.length()));
    }
    p.setCategoria(rs.getString("category"));
    int sl = rs.getInt("scoville_level");
    if (!rs.wasNull()) {
        p.setNivelScoville(sl);
    }
    p.setPaisOrigen(rs.getString("country_of_origin"));
    p.setEnlaceCompra(rs.getString("purchase_link"));
    p.setValoracion(rs.getDouble("rating"));
    p.setFechaCreacion(rs.getTimestamp("created_at"));
    return p;
}
```

**Línea 321-342:** Toma un `ResultSet` (una fila del resultado SQL) y crea un objeto `Producto` con todos sus campos.

**Línea 328:** `Blob blob = rs.getBlob("image_data");` — `Blob` es un objeto de JDBC que representa datos binarios grandes (una imagen). `rs.getBlob("image_data")` obtiene el BLOB de la columna `image_data`.

**Línea 329-330:** `if (blob != null) { p.setImagenDatos(blob.getBytes(1, (int) blob.length())); }` — Si hay imagen, la convierte a `byte[]` (array de bytes). `blob.getBytes(1, (int) blob.length())` extrae todos los bytes del BLOB. Luego los guarda en el producto.

**Línea 333-335:** `int sl = rs.getInt("scoville_level"); if (!rs.wasNull()) { ... }` — `rs.getInt()` devuelve 0 si la columna es NULL. Para distinguir entre "es 0" y "es NULL", llamamos a `rs.wasNull()` después. Si `wasNull()` devuelve `true`, significa que la columna era NULL y no asignamos el nivel Scoville.

#### mapearUsuario()

**Línea 344-354:** Similar, pero más simple (no hay BLOBs ni nullable Ints). Convierte una fila de la tabla `users` en un objeto `Usuario`.

#### mapearResena()

```java
private static Resena mapearResena(ResultSet rs) throws SQLException {
    Resena r = new Resena();
    r.setId(rs.getLong("id"));
    r.setValoracion(rs.getInt("rating"));
    r.setComentario(rs.getString("comment"));
    r.setFechaCreacion(rs.getTimestamp("created_at"));

    Usuario u = new Usuario();
    u.setId(rs.getLong("user_id"));
    u.setUsuario(rs.getString("username"));
    u.setNombre(rs.getString("nombre_usuario"));
    u.setEmail(rs.getString("email"));
    u.setRol(rs.getString("role"));
    u.setContrasena(rs.getString("contrasena_usuario"));
    u.setFechaCreacion(rs.getTimestamp("fecha_usuario"));
    r.setUsuario(u);

    r.setProducto(buscarProductoPorId(rs.getLong("product_id")));
    return r;
}
```

**Línea 356:** Convierte una fila del JOIN entre `reviews` y `users` en un objeto `Resena`.

**Línea 358-361:** Datos propios de la reseña: id, valoracion (rating), comentario (comment), fecha (created_at).

**Línea 363-370:** Crea un objeto `Usuario` a partir de los datos del JOIN (columnas con alias: `nombre_usuario`, `contrasena_usuario`, `fecha_usuario`). Asigna el usuario a la reseña.

**Línea 372:** `r.setProducto(buscarProductoPorId(rs.getLong("product_id")));` — **Este es el N+1.** Para obtener el producto de la reseña, hace OTRA consulta SQL: `buscarProductoPorId()`. Si hay 10 reseñas, hace 1 consulta para las reseñas + 10 consultas para los productos = 11 consultas totales (N+1). Es ineficiente pero muy didáctico: muestra cómo un método puede llamar a otro método de la misma clase.

---

## 4. Paquete util — PasswordUtil.java (47 líneas)

```java
private static final String ALGORITMO = "SHA-256";
```

**Línea 10:** Constante con el nombre del algoritmo de hash: SHA-256. Produce un hash de 256 bits (32 bytes).

#### cifrar()

```java
public static String cifrar(String contrasena) {
    String sal = generarSal();
    String hash = calcularHash(contrasena, sal);
    return sal + ":" + hash;
}
```

**Línea 12-16:** Cifra una contraseña:
1. Genera un **salt** aleatorio (cadena de 16 bytes aleatorios en base64). El salt es único para cada usuario.
2. Calcula el **hash** SHA-256 de `salt + contraseña`.
3. Devuelve `"salt:hash"` — los dos valores separados por dos puntos, ambos en base64.

**¿Por qué salt?** — Si dos usuarios tienen la misma contraseña, con salt los hashes son diferentes. Además, impide usar tablas rainbow (ataques precomputados).

#### verificar()

```java
public static boolean verificar(String contrasena, String hashAlmacenado) {
    if (hashAlmacenado == null) return false;
    String[] partes = hashAlmacenado.split(":");
    if (partes.length != 2) return false;
    String sal = partes[0];
    String hashEsperado = partes[1];
    String hashCalculado = calcularHash(contrasena, sal);
    return MessageDigest.isEqual(
        hashEsperado.getBytes(),
        hashCalculado.getBytes()
    );
}
```

**Línea 18-29:** Verifica una contraseña contra un hash almacenado:
1. Divide el hash almacenado por `:` → `partes[0]` es el salt, `partes[1]` es el hash esperado.
2. Recalcula el hash con la contraseña proporcionada y el salt extraído.
3. Compara los dos hashes con `MessageDigest.isEqual()` (seguro contra ataques de temporización, a diferencia de `String.equals()`).

#### generarSal() y calcularHash()

```java
private static String generarSal() {
    byte[] salBytes = new byte[16];
    new SecureRandom().nextBytes(salBytes);
    return Base64.getEncoder().encodeToString(salBytes);
}
```

**Línea 31-35:** `SecureRandom` genera 16 bytes criptográficamente aleatorios (no predecibles). Los convierte a base64 para tener un String.

```java
private static String calcularHash(String contrasena, String sal) {
    try {
        MessageDigest md = MessageDigest.getInstance(ALGORITMO);
        md.update(sal.getBytes());
        byte[] hashBytes = md.digest(contrasena.getBytes());
        return Base64.getEncoder().encodeToString(hashBytes);
    } catch (NoSuchAlgorithmException e) {
        throw new RuntimeException("Error: " + ALGORITMO + " no disponible", e);
    }
}
```

**Línea 37-46:** Calcula SHA-256 de `sal + contraseña`:
1. `MessageDigest.getInstance("SHA-256")` — obtiene el objeto que implementa SHA-256.
2. `md.update(sal.getBytes())` — añade el salt al cálculo.
3. `md.digest(contrasena.getBytes())` — añade la contraseña y finaliza el hash. Devuelve 32 bytes.
4. Convierte los bytes a String base64.

> **Concepto: SHA-256** — Algoritmo de hash criptográfico. Toma cualquier entrada y produce 32 bytes (256 bits) de salida. Es unidireccional: no se puede obtener la entrada original a partir del hash. Se usa para almacenar contraseñas de forma segura.

---

## 5. Paquete filtro — FiltroAutenticacion.java (46 líneas)

```java
@WebFilter(urlPatterns = {"/admin/*", "/profile", "/review"})
public class FiltroAutenticacion implements Filter {
```

**Línea 11:** `@WebFilter(urlPatterns = {...})` — Anotación que indica a Tomcat que este filtro se ejecute antes que los servlets para las rutas: `/admin/*`, `/profile` y `/review`.

**Línea 12:** `public class FiltroAutenticacion implements Filter` — Implementa la interfaz `Filter` de Java Servlet API. Un filtro intercepta peticiones HTTP antes de que lleguen al servlet.

```java
public void doFilter(ServletRequest peticion, ServletResponse respuesta, FilterChain cadena)
        throws IOException, ServletException {
    HttpServletRequest req = (HttpServletRequest) peticion;
    HttpServletResponse res = (HttpServletResponse) respuesta;
    String ruta = req.getRequestURI();
```

**Línea 15-19:** El método `doFilter` se ejecuta para cada petición que coincide con `urlPatterns`. Convierte los parámetros genéricos a HTTP (`HttpServletRequest` y `HttpServletResponse`) y obtiene la URL solicitada.

```java
    boolean rutaProtegida = ruta.equals(req.getContextPath() + "/profile")
                         || ruta.equals(req.getContextPath() + "/review")
                         || ruta.startsWith(req.getContextPath() + "/admin");

    if (!rutaProtegida) {
        cadena.doFilter(peticion, respuesta);
        return;
    }
```

**Línea 21-28:** Comprueba si la ruta es una de las protegidas. Si NO lo es, deja pasar la petición (`cadena.doFilter()`). Esto es una salvaguarda: aunque el filtro solo se ejecuta para las rutas de `urlPatterns`, esta comprobación extra es por seguridad.

```java
    HttpSession sesion = req.getSession(false);
    if (sesion == null || sesion.getAttribute("usuario") == null) {
        res.sendRedirect(req.getContextPath() + "/login");
        return;
    }
```

**Línea 30-34:** `req.getSession(false)` — Obtiene la sesión HTTP actual, SIN crearla si no existe (el `false`). Si no hay sesión, o si la sesión no tiene un atributo "usuario" (no ha hecho login), redirige al login y detiene el filtro.

```java
    if (ruta.startsWith(req.getContextPath() + "/admin")) {
        Usuario usuario = (Usuario) sesion.getAttribute("usuario");
        if (!"ADMIN".equals(usuario.getRol())) {
            res.sendRedirect(req.getContextPath() + "/login");
            return;
        }
    }

    cadena.doFilter(peticion, respuesta);
}
```

**Línea 36-44:** Si la ruta es de administración (`/admin/...`), además comprueba que el usuario tenga rol "ADMIN". Si no, redirige al login. Si todo está bien, `cadena.doFilter()` deja pasar la petición al servlet.

> **Flujo del filtro:** Llega una petición a `/admin/products` → el filtro ve que la ruta está protegida → comprueba sesión → comprueba rol ADMIN → si todo ok, deja pasar al AdminServlet.

---

## 6. Paquete servlet

### 6.1. InicioServlet.java (29 líneas)

```java
@WebServlet(urlPatterns = {"/home", "/catalog"})
public class InicioServlet extends HttpServlet {
```

**Línea 12:** Sirve dos rutas: `/home` (página principal) y `/catalog` (catálogo completo).

**Línea 20:** `peticion.setAttribute("productos", BaseDatos.obtenerProductos());` — Carga todos los productos de la BD y los guarda en la petición para que el JSP los muestre. Se hace para AMBAS rutas.

**Línea 22-26:** Si la ruta termina en `/catalog`, reenvía a `catalog.jsp`. Si no (es `/home`), también carga las 6 reseñas más recientes (`obtenerResenasRecientes(6)`) y reenvía a `index.jsp`.

> **Concepto: setAttribute / getRequestDispatcher** — `setAttribute("clave", valor)` guarda datos en la petición para que el JSP los acceda con `${clave}`. `getRequestDispatcher("/jsp/...").forward(peticion, respuesta)` envía la petición al JSP para que genere el HTML.

### 6.2. AutenticacionServlet.java (93 líneas)

```java
@WebServlet(urlPatterns = {"/login", "/register", "/logout"})
```

**Línea 15:** Gestiona tres rutas de autenticación.

#### doGet() — mostrar formularios o cerrar sesión

**Línea 24-30:** Si la ruta es `/logout`, invalida la sesión (`sesion.invalidate()`) y redirige al home.

**Línea 33-35:** Si es `/register`, muestra el formulario de registro.

**Línea 38:** Si no es ninguna de las anteriores (es `/login`), muestra el formulario de login.

#### iniciarSesion() (POST a /login)

```java
private void iniciarSesion(HttpServletRequest peticion, HttpServletResponse respuesta)
        throws IOException, ServletException {
    String usuario = peticion.getParameter("username");
    String contrasena = peticion.getParameter("password");

    Usuario u = BaseDatos.buscarUsuarioPorNombreUsuario(usuario);

    if (u != null && PasswordUtil.verificar(contrasena, u.getContrasena())) {
        HttpSession sesion = peticion.getSession();
        sesion.setAttribute("usuario", u);
        if ("ADMIN".equals(u.getRol())) {
            respuesta.sendRedirect(peticion.getContextPath() + "/admin");
        } else {
            respuesta.sendRedirect(peticion.getContextPath() + "/home");
        }
    } else {
        peticion.setAttribute("error", "Invalid username or password");
        peticion.getRequestDispatcher("/jsp/login.jsp").forward(peticion, respuesta);
    }
}
```

**Línea 56-57:** Lee los parámetros del formulario: `username` y `password`.

**Línea 59:** Busca el usuario en la BD por nombre de usuario.

**Línea 61:** `if (u != null && PasswordUtil.verificar(contrasena, u.getContrasena()))` — Si existe el usuario Y la contraseña es correcta:
- **Línea 62-63:** Crea una sesión HTTP y guarda el objeto `Usuario` completo en la sesión con clave `"usuario"`.
- **Línea 64-68:** Según el rol, redirige al panel de admin o al home.

**Línea 70-71:** Si falla (usuario no existe o contraseña incorrecta), guarda un mensaje de error y vuelve a mostrar el formulario de login.

#### registrar() (POST a /register)

```java
private void registrar(HttpServletRequest peticion, HttpServletResponse respuesta)
        throws IOException, ServletException {
    String usuario = peticion.getParameter("username");
    String email = peticion.getParameter("email");
    String contrasena = peticion.getParameter("password");
    String nombre = peticion.getParameter("name");

    if (BaseDatos.buscarUsuarioPorNombreUsuario(usuario) != null) {
        peticion.setAttribute("error", "Username already exists");
        peticion.getRequestDispatcher("/jsp/register.jsp").forward(peticion, respuesta);
        return;
    }

    Usuario nuevo = new Usuario(usuario, email, PasswordUtil.cifrar(contrasena), "USER", nombre);
    BaseDatos.guardarUsuario(nuevo);
    respuesta.sendRedirect(peticion.getContextPath() + "/login");
}
```

**Línea 78-81:** Lee los parámetros del formulario de registro.

**Línea 83-87:** Comprueba si el nombre de usuario ya existe (consulta a BD). Si existe, muestra error.

**Línea 89:** `PasswordUtil.cifrar(contrasena)` — **Antes de guardar, la contraseña se cifra.** El método `cifrar()` devuelve el String `"salt:hash"` en base64.

**Línea 90:** Guarda el nuevo usuario en la BD.

**Línea 91:** Redirige al login para que el usuario se loguee con su nueva cuenta.

### 6.3. DetalleProductoServlet.java (37 líneas)

```java
@WebServlet("/product")
public class DetalleProductoServlet extends HttpServlet {
    protected void doGet(HttpServletRequest peticion, HttpServletResponse respuesta)
            throws ServletException, IOException {
        String idParam = peticion.getParameter("id");
        if (idParam == null || idParam.isEmpty()) {
            respuesta.sendRedirect(peticion.getContextPath() + "/catalog");
            return;
        }

        Long id = Long.parseLong(idParam);
        Producto producto = BaseDatos.buscarProductoPorId(id);
        if (producto == null) {
            respuesta.sendRedirect(peticion.getContextPath() + "/catalog");
            return;
        }

        peticion.setAttribute("producto", producto);
        peticion.setAttribute("resenas", BaseDatos.obtenerResenasPorProducto(id));
        peticion.getRequestDispatcher("/jsp/product-detail.jsp").forward(peticion, respuesta);
    }
}
```

**Línea 20-24:** Lee el parámetro `id` de la URL (ej: `/product?id=3`). Si no hay ID, redirige al catálogo.

**Línea 26-31:** Convierte el ID a `Long` y busca el producto en la BD. Si no existe, redirige al catálogo.

**Línea 33-35:** Guarda el producto y sus reseñas como atributos, y reenvía al JSP de detalle.

### 6.4. ResenaServlet.java (39 líneas)

```java
@WebServlet("/review")
public class ResenaServlet extends HttpServlet {
    protected void doPost(HttpServletRequest peticion, HttpServletResponse respuesta)
            throws IOException {
        HttpSession sesion = peticion.getSession();
        Usuario usuario = (Usuario) sesion.getAttribute("usuario");
        Long productoId = Long.parseLong(peticion.getParameter("productId"));
        Integer valoracion = Integer.parseInt(peticion.getParameter("rating"));
        String comentario = peticion.getParameter("comment");

        Producto producto = BaseDatos.buscarProductoPorId(productoId);
        if (producto == null) {
            respuesta.sendRedirect(peticion.getContextPath() + "/catalog");
            return;
        }

        Resena resena = new Resena(usuario, producto, valoracion, comentario);
        BaseDatos.guardarResena(resena);
        respuesta.sendRedirect(peticion.getContextPath() + "/product?id=" + productoId);
    }
}
```

**Línea 23-27:** Obtiene el usuario de la sesión (el filtro ya garantiza que está logueado). Lee los parámetros del formulario: `productId`, `rating`, `comment`.

**Línea 29-33:** Busca el producto en la BD para asegurar que existe. Si no, redirige al catálogo.

**Línea 35:** Crea un objeto `Resena` con el usuario, el producto, la valoración y el comentario. Usa el constructor con parámetros.

**Línea 36:** Guarda la reseña en la BD.

**Línea 37:** Redirige al detalle del producto para que se vea la reseña nueva.

### 6.5. PerfilServlet.java (28 líneas)

```java
@WebServlet("/profile")
public class PerfilServlet extends HttpServlet {
    protected void doGet(HttpServletRequest peticion, HttpServletResponse respuesta)
            throws ServletException, IOException {
        HttpSession sesion = peticion.getSession();
        Usuario usuario = (Usuario) sesion.getAttribute("usuario");

        peticion.setAttribute("usuario", usuario);
        peticion.setAttribute("resenas", BaseDatos.obtenerResenasPorUsuario(usuario.getId()));
        peticion.getRequestDispatcher("/jsp/profile.jsp").forward(peticion, respuesta);
    }
}
```

**Línea 21-22:** Obtiene el usuario de la sesión (ya está logueado gracias al filtro).

**Línea 24-26:** Guarda el usuario y sus reseñas como atributos, y reenvía al JSP de perfil. Las reseñas se obtienen llamando a `obtenerResenasPorUsuario(usuario.getId())`.

### 6.6. AdminServlet.java (119 líneas)

El servlet más grande. Gestiona todo el panel de administración.

#### doGet() — mostrar páginas

```java
protected void doGet(HttpServletRequest peticion, HttpServletResponse respuesta)
        throws ServletException, IOException {
    String ruta = peticion.getRequestURI();

    if (ruta.endsWith("/products")) {
        // Listar productos
        peticion.setAttribute("productos", BaseDatos.obtenerProductos());
        peticion.getRequestDispatcher("/jsp/admin/products.jsp").forward(peticion, respuesta);

    } else if (ruta.endsWith("/product/new")) {
        // Formulario para nuevo producto
        peticion.setAttribute("producto", new Producto());
        peticion.getRequestDispatcher("/jsp/admin/product-form.jsp").forward(peticion, respuesta);

    } else if (ruta.endsWith("/product/edit")) {
        // Formulario para editar producto existente
        Long id = Long.parseLong(peticion.getParameter("id"));
        Producto producto = BaseDatos.buscarProductoPorId(id);
        if (producto == null) {
            respuesta.sendRedirect(peticion.getContextPath() + "/admin/products");
            return;
        }
        peticion.setAttribute("producto", producto);
        peticion.getRequestDispatcher("/jsp/admin/product-form.jsp").forward(peticion, respuesta);

    } else if (ruta.endsWith("/users")) {
        // Listar usuarios
        peticion.setAttribute("usuarios", BaseDatos.obtenerUsuarios());
        peticion.getRequestDispatcher("/jsp/admin/users.jsp").forward(peticion, respuesta);

    } else if (ruta.endsWith("/user/delete")) {
        // Eliminar usuario
        Long id = Long.parseLong(peticion.getParameter("id"));
        BaseDatos.eliminarUsuario(id);
        respuesta.sendRedirect(peticion.getContextPath() + "/admin/users");

    } else {
        // Dashboard (raíz de /admin)
        peticion.setAttribute("cantidadProductos", BaseDatos.obtenerProductos().size());
        peticion.setAttribute("cantidadUsuarios", BaseDatos.obtenerUsuarios().size());
        peticion.getRequestDispatcher("/jsp/admin/dashboard.jsp").forward(peticion, respuesta);
    }
}
```

**Línea 22:** Obtiene la URL completa de la petición.

**Línea 24-26:** `/admin/products` → lista todos los productos.

**Línea 28-30:** `/admin/product/new` → crea un `Producto` vacío y lo envía al formulario. El formulario detecta que no tiene ID y muestra "Añadir producto".

**Línea 32-40:** `/admin/product/edit?id=X` → busca el producto por ID y lo envía al formulario. El formulario detecta que tiene ID y muestra "Editar producto" con los campos rellenos.

**Línea 42-44:** `/admin/users` → lista todos los usuarios.

**Línea 46-49:** `/admin/user/delete?id=X` → elimina un usuario y redirige al listado.

**Línea 51-55:** Cualquier otra ruta bajo `/admin` → muestra el dashboard con estadísticas (conteo de productos y usuarios).

#### doPost() — procesar formularios

```java
protected void doPost(HttpServletRequest peticion, HttpServletResponse respuesta)
        throws IOException, ServletException {
    String ruta = peticion.getRequestURI();

    if (ruta.endsWith("/product/save")) {
        guardarProducto(peticion, respuesta);
    } else if (ruta.endsWith("/product/delete")) {
        Long id = Long.parseLong(peticion.getParameter("id"));
        BaseDatos.eliminarProducto(id);
        respuesta.sendRedirect(peticion.getContextPath() + "/admin/products");
    }
}
```

**Línea 63-64:** `/admin/product/save` → llama al método `guardarProducto()`.

**Línea 65-69:** `/admin/product/delete` → elimina producto por ID y redirige.

#### guardarProducto() — el método más largo

```java
private void guardarProducto(HttpServletRequest peticion, HttpServletResponse respuesta)
        throws IOException, ServletException {
    String idParam = peticion.getParameter("id");
    Producto producto = new Producto();
    if (idParam != null && !idParam.isEmpty()) {
        producto.setId(Long.parseLong(idParam));
    }

    producto.setNombre(peticion.getParameter("name"));
    producto.setDescripcion(peticion.getParameter("description"));
    producto.setPrecio(Double.parseDouble(peticion.getParameter("price")));
    producto.setCategoria(peticion.getParameter("category"));

    String scoville = peticion.getParameter("scovilleLevel");
    if (scoville != null && !scoville.isEmpty()) {
        producto.setNivelScoville(Integer.parseInt(scoville));
    }

    producto.setPaisOrigen(peticion.getParameter("countryOfOrigin"));
    producto.setEnlaceCompra(peticion.getParameter("purchaseLink"));

    String valoracion = peticion.getParameter("rating");
    if (valoracion != null && !valoracion.isEmpty()) {
        producto.setValoracion(Double.parseDouble(valoracion));
    }

    Part parteImagen = peticion.getPart("imageFile");
    if (parteImagen != null && parteImagen.getSize() > 0) {
        producto.setImagenDatos(parteImagen.getInputStream().readAllBytes());
        producto.setImagen(parteImagen.getSubmittedFileName());
    }

    if (producto.getId() != null && producto.getImagenDatos() == null) {
        Producto original = BaseDatos.buscarProductoPorId(producto.getId());
        if (original != null) {
            producto.setImagenDatos(original.getImagenDatos());
            producto.setImagen(original.getImagen());
        }
    }

    if (producto.getId() != null) {
        BaseDatos.actualizarProducto(producto);
    } else {
        BaseDatos.insertarProducto(producto);
    }
    respuesta.sendRedirect(peticion.getContextPath() + "/admin/products");
}
```

**Línea 74-78:** Lee el parámetro `id`. Si viene en la URL (edición), lo asigna al producto. Si no (nuevo producto), el ID queda null.

**Línea 80-83:** Lee y asigna nombre, descripción, precio, categoría. Siembre vienen del formulario.

**Línea 85-88:** `scovilleLevel` es opcional. Si no se rellena, no se asigna (el producto tendrá `nivelScoville = null` en la BD).

**Línea 90-96:** País de origen, enlace de compra, valoración. La valoración también es opcional.

**Línea 98-102:** **Manejo de imagen:**
- `peticion.getPart("imageFile")` — obtiene el archivo subido en el formulario (input type="file" name="imageFile").
- `parteImagen.getInputStream().readAllBytes()` — lee todos los bytes del archivo.
- `parteImagen.getSubmittedFileName()` — obtiene el nombre original del archivo.
- Si se ha subido un archivo, lo guarda como bytes y como nombre.

**Línea 104-110:** **Preservar imagen al editar:** Si estamos editando (tiene ID) y NO se ha subido una imagen nueva, busca el producto original en la BD y copia su imagen. Así, al editar un producto, si no se cambia la foto, se conserva la anterior.

**Línea 112-116:** Decide si insertar o actualizar según tenga ID o no, y llama al método correspondiente de `BaseDatos`.

**Línea 117:** Redirige al listado de productos.

### 6.7. ImagenServlet.java (37 líneas)

```java
@WebServlet("/images/product")
public class ImagenServlet extends HttpServlet {
    protected void doGet(HttpServletRequest peticion, HttpServletResponse respuesta)
            throws IOException {
        String idParam = peticion.getParameter("id");
        if (idParam == null || idParam.isEmpty()) {
            respuesta.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        Long id = Long.parseLong(idParam);
        Producto producto = BaseDatos.buscarProductoPorId(id);

        if (producto == null || producto.getImagenDatos() == null) {
            respuesta.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        respuesta.setContentType("image/jpeg");
        respuesta.getOutputStream().write(producto.getImagenDatos());
    }
}
```

**Línea 20-24:** Si no hay parámetro `id`, devuelve error 404.

**Línea 26-31:** Busca el producto. Si no existe o no tiene imagen, error 404.

**Línea 33-35:** `setContentType("image/jpeg")` — indica al navegador que la respuesta es una imagen JPEG. `getOutputStream().write(producto.getImagenDatos())` — envía los bytes de la imagen al navegador.

**¿Cómo se usa?** En los JSP, las imágenes se cargan con:
```html
<img src="/images/product?id=3">
```
El navegador hace una petición GET a `/images/product?id=3`, el servlet busca el producto con ID=3 y devuelve sus bytes de imagen.

---

## 7. Main.java (62 líneas)

```java
public static void main(String[] args) throws Exception {
    BaseDatos.inicializar();
```

**Línea 15-16:** Punto de entrada. Primero, inicializa la BD (crea la base de datos y ejecuta init.sql).

```java
    System.setProperty("org.apache.catalina.startup.EXIT_ON_INIT_FAILURE", "true");
    String puerto = System.getenv("PORT");
    if (puerto == null) puerto = "8080";
```

**Línea 18-21:** Configura Tomcat para que se cierre si falla al iniciar. Lee el puerto de la variable de entorno `PORT`, o usa 8080 por defecto.

```java
    String rutaBase = new File("target/tomcat").getAbsolutePath();
    Tomcat tomcat = new Tomcat();
    tomcat.setBaseDir(rutaBase);
    tomcat.setPort(Integer.parseInt(puerto));
```

**Línea 23-26:** Crea una instancia de Tomcat embebido. `setBaseDir()` es el directorio de trabajo de Tomcat (para logs, temp, etc.). `setPort()` configura el puerto.

```java
    String rutaWebapp = new File("src/main/webapp").getAbsolutePath();
    if (!new File(rutaWebapp).exists()) {
        rutaWebapp = new File("webapp").getAbsolutePath();
    }
    ...
    Context ctx = tomcat.addWebapp("", rutaWebapp);
    ctx.setParentClassLoader(Main.class.getClassLoader());
```

**Línea 28-39:** Busca el directorio de la aplicación web (donde están los JSP, WEB-INF, CSS). Intenta varias rutas posibles. Luego añade la aplicación a Tomcat con `addWebapp()`.

```java
    File clasesCompiladas = new File("target/classes");
    if (clasesCompiladas.exists()) {
        WebResourceRoot recursos = new StandardRoot(ctx);
        recursos.addPreResources(
            new DirResourceSet(recursos, "/WEB-INF/classes",
                clasesCompiladas.getAbsolutePath(), "/"));
        ctx.setResources(recursos);
    }
```

**Línea 41-48:** Añade las clases compiladas (target/classes) como recursos de Tomcat. Esto permite que Tomcat encuentre los servlets, filtros, etc.

```java
    tomcat.getConnector();
    tomcat.start();
    ...
    tomcat.getServer().await();
```

**Línea 50-60:** Inicia Tomcat. `getConnector()` configura el conector HTTP por defecto. `start()` arranca el servidor. `getServer().await()` hace que el programa espere hasta que se pare Tomcat (el hilo principal se queda bloqueado).

> **Nota:** Main.java solo se usa en desarrollo (`mvn exec:java`). En producción con Docker, Tomcat se inicia con `catalina.sh run` (el Dockerfile copia el WAR en Tomcat).

---

## 8. init.sql (61 líneas)

```sql
CREATE DATABASE IF NOT EXISTS grandspicy CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE grandspicy;

CREATE USER IF NOT EXISTS 'root'@'%' IDENTIFIED BY 'root';
GRANT ALL PRIVILEGES ON *.* TO 'root'@'%' WITH GRANT OPTION;
FLUSH PRIVILEGES;
```

**Línea 1:** Crea la base de datos `grandspicy` si no existe, con charset utf8mb4 (soporta caracteres especiales y emojis).

**Línea 2:** Selecciona la base de datos para las siguientes operaciones.

**Línea 4-6:** Crea el usuario `root` que pueda conectar desde cualquier host (`'%'`). Esto es necesario en Docker porque los contenedores se conectan entre sí con IPs dinámicas.

```sql
CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL DEFAULT 'USER',
    name VARCHAR(100) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

**Línea 8-16:** Tabla `users`:
- `id BIGINT AUTO_INCREMENT PRIMARY KEY` — Número entero grande, se auto-incrementa, es la clave primaria.
- `username VARCHAR(50) NOT NULL UNIQUE` — Texto de hasta 50 caracteres, obligatorio, no puede repetirse.
- `email VARCHAR(100) NOT NULL UNIQUE` — Email, obligatorio, único.
- `password VARCHAR(255) NOT NULL` — Contraseña cifrada (formato "salt:hash" en base64).
- `role VARCHAR(20) NOT NULL DEFAULT 'USER'` — Rol: 'USER' o 'ADMIN'. Por defecto 'USER'.
- `name VARCHAR(100) NOT NULL` — Nombre visible del usuario.
- `created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP` — Fecha de creación, se asigna automáticamente.

```sql
CREATE TABLE IF NOT EXISTS products (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    description TEXT,
    price DOUBLE NOT NULL,
    image VARCHAR(255),
    image_data LONGBLOB,
    category VARCHAR(50) NOT NULL,
    scoville_level INT,
    country_of_origin VARCHAR(100),
    purchase_link VARCHAR(500),
    rating DOUBLE NOT NULL DEFAULT 0.0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

**Línea 18-31:** Tabla `products`:
- `description TEXT` — Texto largo (sin límite fijo).
- `image VARCHAR(255)` — Nombre del archivo de imagen (no el contenido).
- `image_data LONGBLOB` — Contenido binario de la imagen (hasta 4GB). `LONGBLOB` es el tipo BLOB más grande de MySQL.
- `scoville_level INT` — Nivel Scoville (opcional, puede ser NULL).
- `purchase_link VARCHAR(500)` — URL de compra.
- `rating DOUBLE NOT NULL DEFAULT 0.0` — Valoración media.

```sql
CREATE TABLE IF NOT EXISTS reviews (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    rating INT NOT NULL,
    comment TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE
);
```

**Línea 33-42:** Tabla `reviews`:
- `user_id BIGINT NOT NULL` — ID del usuario que escribe la reseña.
- `product_id BIGINT NOT NULL` — ID del producto reseñado.
- `rating INT NOT NULL` — Valoración (1-5).
- `comment TEXT` — Comentario.
- `FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE` — Clave foránea: `user_id` debe existir en `users(id)`. `ON DELETE CASCADE`: si se borra el usuario, se borran sus reseñas automáticamente.
- `FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE` — Lo mismo para productos.

> **Concepto: FOREIGN KEY + CASCADE** — Una clave foránea asegura que no se puedan insertar reseñas con `user_id` que no exista en `users`. `ON DELETE CASCADE` significa que si borramos un usuario, MySQL borra automáticamente todas sus reseñas.

```sql
INSERT IGNORE INTO users (username, email, password, role, name) VALUES
('admin', 'admin@gmail.com', '7aRGXXn5aBllCWS1UxrX8Q==:5RN2fIW/G91NTblXDZO5wz43ESsxJjNXOHxftBIEf08=', 'ADMIN', 'Administrator'),
('user', 'user@grandspicy.com', 'P9kcocDA7auSYxalqe19vw==:H7Bq7LsIC3C0eg69K9DmKBnaGpFDVuDjSvP5VqIjGrQ=', 'USER', 'Test User');
```

**Línea 44-46:** Inserta dos usuarios de ejemplo. `INSERT IGNORE` significa "inserta si no existe ya" (no da error si ya están). Las contraseñas están pre-cifradas: `admin1234` para admin, `user123` para user.

```sql
INSERT IGNORE INTO products (...) VALUES
('Habanero XXX Sauce', ...),
...
('Green Sauce', ...);
```

**Línea 48-56:** 8 productos de ejemplo (salsas, especias, snacks, aceites).

```sql
INSERT IGNORE INTO reviews (user_id, product_id, rating, comment) VALUES
(2, 1, 5, 'Amazing! Very spicy but with great flavor.'),
(2, 5, 4, 'Perfect for those who do not tolerate too much spice.'),
(2, 6, 5, 'This is another level. Just a tiny bit and you feel it.');
```

**Línea 58-61:** 3 reseñas de ejemplo, todas del usuario con ID=2 (el usuario "user").

---

## 9. Flujo completo de una petición

El mismo flujo está documentado en DOCUMENTACION_TECNICA.txt sección 9
y en el guión de demostración de DEFENSA_PROYECTO.md apéndice C.

Ejemplo: **Un usuario ve el detalle del producto con ID=1**

```
1. Navegador: GET http://localhost:8080/product?id=1
                              │
2. Tomcat recibe la petición  │
   Busca @WebServlet("/product") → DetalleProductoServlet
                              │
3. FiltroAutenticacion.doFilter():
   - ¿/product está en {"profile", "review", "/admin/*"}?
   - NO → cadena.doFilter() → deja pasar
                              │
4. DetalleProductoServlet.doGet():
   a. peticion.getParameter("id") → "1"
   b. Long.parseLong("1") → 1L
   c. BaseDatos.buscarProductoPorId(1L):
      - conectar() → Connection a MySQL
      - PreparedStatement: "SELECT * FROM products WHERE id = ?"
      - ps.setLong(1, 1)
      - executeQuery() → ResultSet con 1 fila
      - mapearProducto(rs):
         new Producto()
         setId(1), setNombre("Habanero XXX Sauce"), ...
         Blob (imagen) → setImagenDatos(bytes)
         → devuelve Producto
   d. BaseDatos.obtenerResenasPorProducto(1L):
      - SQL con JOIN: "SELECT r.*, u.username, ... FROM reviews r
        JOIN users u ON r.user_id = u.id WHERE r.product_id = 1
        ORDER BY r.created_at DESC"
      - executeQuery() → ResultSet con reseñas
      - Por cada fila → mapearResena(rs):
         new Resena()
         setId(), setValoracion(), setComentario()
         new Usuario() con datos del JOIN
         buscarProductoPorId(product_id) → OTRA consulta SQL
         → devuelve Resena con Usuario + Producto
   e. peticion.setAttribute("producto", producto)
   f. peticion.setAttribute("resenas", lista)
                              │
5. getRequestDispatcher("/jsp/product-detail.jsp").forward()
                              │
6. product-detail.jsp procesa:
   - Incluye header.jsp (menú)
   - ${producto.nombre} → "Habanero XXX Sauce"
   - <img src="/images/product?id=1"> → ImagenServlet
   - <c:forEach> sobre ${resenas}:
     ${resena.usuario.nombre} → "Test User"
     ${resena.valoracion} → 5
     ${resena.comentario} → "Amazing!..."
   - Si hay sesión: formulario de reseña
   - Si no: "Sign in to leave a review."
   - Incluye footer.jsp
                              │
7. HTML completo se envía al navegador
```

---

## 10. Conceptos clave explicados

| Concepto | Explicación |
|---|---|
| **POJO** | Clase simple con atributos privados, getters/setters y constructor vacío. Representa datos, no tiene lógica. |
| **Servlet** | Clase Java que recibe peticiones HTTP (GET/POST) y genera respuestas. Es el "controlador" del MVC. |
| **JSP** | Página que mezcla HTML con código Java (a través de JSTL). Es la "vista" del MVC. |
| **JSTL** | Librería de etiquetas para JSP: `<c:forEach>`, `<c:if>`, `${expresion}`. Permite recorrer listas y mostrar datos sin escribir código Java en la JSP. |
| **JDBC** | API de Java para conectar y ejecutar SQL en bases de datos. Usamos `Connection`, `Statement`, `PreparedStatement`, `ResultSet`. |
| **PreparedStatement** | SQL con parámetros `?`. Previene inyección SQL porque los valores se pasan por separado, no se concatenan. |
| **try-with-resources** | `try (Recurso r = new Recurso()) { ... }` — cierra el recurso automáticamente al salir del try. |
| **JOIN** | Combina filas de dos tablas. `reviews JOIN users ON reviews.user_id = users.id` — trae datos de ambas tablas en una sola consulta. |
| **FOREIGN KEY** | Columna que referencia a otra tabla. Asegura integridad referencial: no puedes crear una reseña con un user_id que no exista. |
| **ON DELETE CASCADE** | Si se borra una fila de la tabla padre (users), se borran automáticamente las filas relacionadas (reviews). |
| **BLOB / LONGBLOB** | Tipo de dato binario en MySQL para guardar archivos (imágenes, documentos). LONGBLOB permite hasta 4GB. |
| **Salt (criptografía)** | Valor aleatorio que se añade a la contraseña antes de calcular el hash. Hace que dos contraseñas iguales tengan hashes diferentes. |
| **SHA-256** | Algoritmo de hash que produce 256 bits (32 bytes). Unidireccional: no se puede obtener la contraseña original. |
| **Sesión HTTP** | Espacio en el servidor asociado a un navegador. Se crea con `request.getSession()`. Permite recordar datos entre peticiones (como el usuario logueado). |
| **Filter (Filtro)** | Clase que intercepta peticiones HTTP antes de que lleguen al servlet. Se usa para autenticación, logging, etc. |
| **Try-catch** | `try { código peligroso } catch (Exception e) { manejar error }` — captura excepciones para que el programa no se rompa. |
| **Método estático** | `public static tipo metodo()` — se llama sin crear objetos: `Clase.metodo()`. No necesita `new`. |
| **MVC** | Modelo-Vista-Controlador. Patrón que separa datos (modelo), presentación (vista) y lógica (controlador). |
| **DAO** | Data Access Object. Clase que encapsula el acceso a base de datos. En este proyecto, solo `BaseDatos.java`. |
| **DTO / POJO** | Objeto que transporta datos entre capas. Producto, Usuario, Resena son POJOs que pasan de BaseDatos al Servlet y de ahí al JSP. |
