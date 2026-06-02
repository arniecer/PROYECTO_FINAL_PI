package com.grandspicy.servlet;

import com.grandspicy.dao.BaseDatos;
import com.grandspicy.modelo.Producto;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;

@WebServlet("/admin/*")
@MultipartConfig(maxFileSize = 10485760, maxRequestSize = 10485760)
public class AdminServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest peticion, HttpServletResponse respuesta)
            throws ServletException, IOException {
        String ruta = peticion.getRequestURI();

        if (ruta.endsWith("/products")) {
            peticion.setAttribute("productos", BaseDatos.obtenerProductos());
            peticion.getRequestDispatcher("/jsp/admin/products.jsp").forward(peticion, respuesta);

        } else if (ruta.endsWith("/product/new")) {
            peticion.setAttribute("producto", new Producto());
            peticion.getRequestDispatcher("/jsp/admin/product-form.jsp").forward(peticion, respuesta);

        } else if (ruta.endsWith("/product/edit")) {
            Long id = Long.parseLong(peticion.getParameter("id"));
            Producto producto = BaseDatos.buscarProductoPorId(id);
            if (producto == null) {
                respuesta.sendRedirect(peticion.getContextPath() + "/admin/products");
                return;
            }
            peticion.setAttribute("producto", producto);
            peticion.getRequestDispatcher("/jsp/admin/product-form.jsp").forward(peticion, respuesta);

        } else if (ruta.endsWith("/users")) {
            peticion.setAttribute("usuarios", BaseDatos.obtenerUsuarios());
            peticion.getRequestDispatcher("/jsp/admin/users.jsp").forward(peticion, respuesta);

        } else if (ruta.endsWith("/user/delete")) {
            Long id = Long.parseLong(peticion.getParameter("id"));
            BaseDatos.eliminarUsuario(id);
            respuesta.sendRedirect(peticion.getContextPath() + "/admin/users");

        } else {
            peticion.setAttribute("cantidadProductos", BaseDatos.obtenerProductos().size());
            peticion.setAttribute("cantidadUsuarios", BaseDatos.obtenerUsuarios().size());
            peticion.getRequestDispatcher("/jsp/admin/dashboard.jsp").forward(peticion, respuesta);
        }
    }

    @Override
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

    private void guardarProducto(HttpServletRequest peticion, HttpServletResponse respuesta)
            throws IOException, ServletException {
        peticion.setCharacterEncoding("UTF-8");
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
}
