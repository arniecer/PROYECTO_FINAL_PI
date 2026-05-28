package com.grandspicy.modelo;

import java.util.Date;

// Representa un producto picante del catalogo. Mapea la tabla 'products'.
public class Producto {

    private Long id;
    private String nombre;
    private String descripcion;
    private Double precio;
    private String imagen;
    private byte[] imagenDatos;
    private String categoria;
    private Integer nivelScoville;
    private String paisOrigen;
    private String enlaceCompra;
    private Double valoracion;
    private Date fechaCreacion;

    public Producto() {
        this.valoracion = 0.0;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public Double getPrecio() { return precio; }
    public void setPrecio(Double precio) { this.precio = precio; }
    public String getImagen() { return imagen; }
    public void setImagen(String imagen) { this.imagen = imagen; }
    public byte[] getImagenDatos() { return imagenDatos; }
    public void setImagenDatos(byte[] imagenDatos) { this.imagenDatos = imagenDatos; }
    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }
    public Integer getNivelScoville() { return nivelScoville; }
    public void setNivelScoville(Integer nivelScoville) { this.nivelScoville = nivelScoville; }
    public String getPaisOrigen() { return paisOrigen; }
    public void setPaisOrigen(String paisOrigen) { this.paisOrigen = paisOrigen; }
    public String getEnlaceCompra() { return enlaceCompra; }
    public void setEnlaceCompra(String enlaceCompra) { this.enlaceCompra = enlaceCompra; }
    public Double getValoracion() { return valoracion; }
    public void setValoracion(Double valoracion) { this.valoracion = valoracion; }
    public Date getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(Date fechaCreacion) { this.fechaCreacion = fechaCreacion; }
}
