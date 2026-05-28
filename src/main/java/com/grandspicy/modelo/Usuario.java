package com.grandspicy.modelo;

import java.util.Date;

// Representa un usuario del sistema. Mapea la tabla 'users'.
public class Usuario {

    private Long id;
    private String usuario;
    private String email;
    private String contrasena;
    private String rol;
    private String nombre;
    private Date fechaCreacion;

    public Usuario() {}

    public Usuario(String usuario, String email, String contrasena, String rol, String nombre) {
        this.usuario = usuario;
        this.email = email;
        this.contrasena = contrasena;
        this.rol = rol;
        this.nombre = nombre;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getUsuario() { return usuario; }
    public void setUsuario(String usuario) { this.usuario = usuario; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getContrasena() { return contrasena; }
    public void setContrasena(String contrasena) { this.contrasena = contrasena; }
    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public Date getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(Date fechaCreacion) { this.fechaCreacion = fechaCreacion; }
}
