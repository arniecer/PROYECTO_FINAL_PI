package com.grandspicy.servlet;

import com.grandspicy.dao.BaseDatos;
import com.grandspicy.modelo.Producto;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/images/product")
// Sirve la imagen de un producto desde la BD (BLOB) como JPEG
public class ImagenServlet extends HttpServlet {

    @Override
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
