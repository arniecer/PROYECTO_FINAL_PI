package com.grandspicy.servlet;

import com.grandspicy.dao.BaseDatos;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(urlPatterns = {"/home", "/catalog"})
// Pagina principal (home) y catalogo de productos. Carga productos y reseñas recientes.
public class InicioServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest peticion, HttpServletResponse respuesta)
            throws ServletException, IOException {
        String ruta = peticion.getRequestURI();
        peticion.setAttribute("productos", BaseDatos.obtenerProductos());

        if (ruta.endsWith("/catalog")) {
            peticion.getRequestDispatcher("/jsp/catalog.jsp").forward(peticion, respuesta);
        } else {
            peticion.setAttribute("resenasRecientes", BaseDatos.obtenerResenasRecientes(6));
            peticion.getRequestDispatcher("/jsp/index.jsp").forward(peticion, respuesta);
        }
    }
}
