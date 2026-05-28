package com.grandspicy.servlet;

import com.grandspicy.dao.BaseDatos;
import com.grandspicy.modelo.Usuario;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/profile")
// Muestra el perfil del usuario logueado con todas sus reseñas
public class PerfilServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest peticion, HttpServletResponse respuesta)
            throws ServletException, IOException {
        HttpSession sesion = peticion.getSession();
        Usuario usuario = (Usuario) sesion.getAttribute("usuario");

        peticion.setAttribute("usuario", usuario);
        peticion.setAttribute("resenas", BaseDatos.obtenerResenasPorUsuario(usuario.getId()));
        peticion.getRequestDispatcher("/jsp/profile.jsp").forward(peticion, respuesta);
    }
}
