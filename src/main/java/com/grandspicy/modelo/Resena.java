package com.grandspicy.modelo;

import java.util.Date;

// Representa una reseña de un producto hecha por un usuario. Mapea la tabla 'reviews'.
public class Resena {

    private Long id;
    private Usuario usuario;
    private Producto producto;
    private Integer valoracion;
    private String comentario;
    private Date fechaCreacion;

    public Resena() {}

    public Resena(Usuario usuario, Producto producto, Integer valoracion, String comentario) {
        this.usuario = usuario;
        this.producto = producto;
        this.valoracion = valoracion;
        this.comentario = comentario;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
    public Producto getProducto() { return producto; }
    public void setProducto(Producto producto) { this.producto = producto; }
    public Integer getValoracion() { return valoracion; }
    public void setValoracion(Integer valoracion) { this.valoracion = valoracion; }
    public String getComentario() { return comentario; }
    public void setComentario(String comentario) { this.comentario = comentario; }
    public Date getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(Date fechaCreacion) { this.fechaCreacion = fechaCreacion; }
}
