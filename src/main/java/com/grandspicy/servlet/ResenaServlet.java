package com.grandspicy.servlet;

import com.grandspicy.dao.BaseDatos;
import com.grandspicy.modelo.Producto;
import com.grandspicy.modelo.Resena;
import com.grandspicy.modelo.Usuario;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/review")
// Guarda una reseña nueva (POST) y redirige al producto reseñado
public class ResenaServlet extends HttpServlet {

    @Override
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
