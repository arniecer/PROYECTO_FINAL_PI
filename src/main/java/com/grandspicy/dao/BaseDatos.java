package com.grandspicy.dao;

import com.grandspicy.modelo.Producto;
import com.grandspicy.modelo.Resena;
import com.grandspicy.modelo.Usuario;

import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BaseDatos {

    private static boolean driverCargado = false;

    private static String getEnv(String variable, String defecto) {
        String valor = System.getenv(variable);
        if (valor != null) {
            return valor;
        }
        return defecto;
    }

    private static Connection conectar() throws SQLException {
        if (!driverCargado) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                driverCargado = true;
            } catch (ClassNotFoundException e) {
                throw new SQLException("No se encontró el driver JDBC", e);
            }
        }
        return DriverManager.getConnection(
            getEnv("DB_URL", "jdbc:mysql://localhost:3306/grandspicy?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC"),
            getEnv("DB_USER", "root"),
            getEnv("DB_PASSWORD", "root")
        );
    }

    // ===== PRODUCTOS =====

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

    public static Producto buscarProductoPorNombre(String nombre) {
        String sql = "SELECT * FROM products WHERE name = ?";
        try (Connection c = conectar();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, nombre);
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

    public static void insertarProducto(Producto p) {
        String sql = "INSERT INTO products (name, description, price, image, image_data, category, scoville_level, country_of_origin, purchase_link, rating, created_at) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection c = conectar();
             PreparedStatement ps = c.prepareStatement(sql)) {
            asignarParametrosProducto(ps, p);
            ps.setTimestamp(11, new Timestamp(System.currentTimeMillis()));
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void actualizarProducto(Producto p) {
        String sql = "UPDATE products SET name=?, description=?, price=?, image=?, image_data=?, category=?, scoville_level=?, country_of_origin=?, purchase_link=?, rating=? WHERE id=?";
        try (Connection c = conectar();
             PreparedStatement ps = c.prepareStatement(sql)) {
            asignarParametrosProducto(ps, p);
            ps.setLong(11, p.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

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

    // ===== USUARIOS =====

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

    public static Usuario buscarUsuarioPorId(Long id) {
        String sql = "SELECT * FROM users WHERE id = ?";
        try (Connection c = conectar();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, id);
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

    public static List<Usuario> obtenerUsuarios() {
        List<Usuario> lista = new ArrayList<>();
        String sql = "SELECT * FROM users ORDER BY created_at DESC";
        try (Connection c = conectar();
             Statement s = c.createStatement();
             ResultSet rs = s.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(mapearUsuario(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

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

    public static void eliminarUsuario(Long id) {
        String sql = "DELETE FROM users WHERE id = ?";
        try (Connection c = conectar();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // ===== RESENAS =====

    public static List<Resena> obtenerResenasPorProducto(Long productoId) {
        List<Resena> lista = new ArrayList<>();
        String sql = "SELECT r.*, u.username, u.name as nombre_usuario, u.email, u.role, u.password as contrasena_usuario, u.created_at as fecha_usuario FROM reviews r JOIN users u ON r.user_id = u.id WHERE r.product_id = ? ORDER BY r.created_at DESC";
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

    public static List<Resena> obtenerResenasPorUsuario(Long usuarioId) {
        List<Resena> lista = new ArrayList<>();
        String sql = "SELECT r.*, u.username, u.name as nombre_usuario, u.email, u.role, u.password as contrasena_usuario, u.created_at as fecha_usuario FROM reviews r JOIN users u ON r.user_id = u.id WHERE r.user_id = ? ORDER BY r.created_at DESC";
        try (Connection c = conectar();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, usuarioId);
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

    public static List<Resena> obtenerResenasRecientes(int limite) {
        List<Resena> lista = new ArrayList<>();
        String sql = "SELECT r.*, u.username, u.name as nombre_usuario, u.email, u.role, u.password as contrasena_usuario, u.created_at as fecha_usuario FROM reviews r JOIN users u ON r.user_id = u.id ORDER BY r.created_at DESC LIMIT ?";
        try (Connection c = conectar();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, limite);
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

    // ===== INICIALIZACION =====

    public static void inicializar() {
        try {
            try (Connection c = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC",
                    getEnv("DB_USER", "root"), getEnv("DB_PASSWORD", "root"));
                 Statement s = c.createStatement()) {
                s.executeUpdate("CREATE DATABASE IF NOT EXISTS grandspicy CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci");
            }

            String sql = new String(BaseDatos.class.getClassLoader().getResourceAsStream("init.sql").readAllBytes(), StandardCharsets.UTF_8);
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

    // ===== MAPEADORES =====

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

    private static Usuario mapearUsuario(ResultSet rs) throws SQLException {
        Usuario u = new Usuario();
        u.setId(rs.getLong("id"));
        u.setUsuario(rs.getString("username"));
        u.setEmail(rs.getString("email"));
        u.setContrasena(rs.getString("password"));
        u.setRol(rs.getString("role"));
        u.setNombre(rs.getString("name"));
        u.setFechaCreacion(rs.getTimestamp("created_at"));
        return u;
    }

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
}
