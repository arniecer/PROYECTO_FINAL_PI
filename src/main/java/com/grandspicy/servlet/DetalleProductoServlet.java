package com.grandspicy.servlet;

import com.grandspicy.dao.BaseDatos;
import com.grandspicy.modelo.Producto;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/product")
// Muestra el detalle de un producto: info completa + reseñas de otros usuarios
public class DetalleProductoServlet extends HttpServlet {

    @Override
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
