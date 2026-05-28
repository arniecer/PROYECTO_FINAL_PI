package com.grandspicy.servlet;

import com.grandspicy.dao.BaseDatos;
import com.grandspicy.modelo.Usuario;
import com.grandspicy.util.PasswordUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(urlPatterns = {"/login", "/register", "/logout"})
// Maneja login, registro y cierre de sesion de usuarios
public class AutenticacionServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest peticion, HttpServletResponse respuesta)
            throws ServletException, IOException {
        String ruta = peticion.getRequestURI();

        if (ruta.endsWith("/logout")) {
            HttpSession sesion = peticion.getSession(false);
            if (sesion != null) {
                sesion.invalidate();
            }
            respuesta.sendRedirect(peticion.getContextPath() + "/home");
            return;
        }

        if (ruta.endsWith("/register")) {
            peticion.getRequestDispatcher("/jsp/register.jsp").forward(peticion, respuesta);
            return;
        }

        peticion.getRequestDispatcher("/jsp/login.jsp").forward(peticion, respuesta);
    }

    @Override
    protected void doPost(HttpServletRequest peticion, HttpServletResponse respuesta)
            throws IOException, ServletException {
        String ruta = peticion.getRequestURI();

        if (ruta.endsWith("/register")) {
            registrar(peticion, respuesta);
        } else {
            iniciarSesion(peticion, respuesta);
        }
    }

    // Valida usuario y contraseña, si ok guarda en sesion y redirige segun rol
    private void iniciarSesion(HttpServletRequest peticion, HttpServletResponse respuesta)
            throws IOException, ServletException {
        String usuario = peticion.getParameter("username");
        String contrasena = peticion.getParameter("password");

        Usuario u = BaseDatos.buscarUsuarioPorNombreUsuario(usuario);

        if (u != null && PasswordUtil.verificar(contrasena, u.getContrasena())) {
            HttpSession sesion = peticion.getSession();
            sesion.setAttribute("usuario", u);
            if ("ADMIN".equals(u.getRol())) {
                respuesta.sendRedirect(peticion.getContextPath() + "/admin");
            } else {
                respuesta.sendRedirect(peticion.getContextPath() + "/home");
            }
        } else {
            peticion.setAttribute("error", "Invalid username or password");
            peticion.getRequestDispatcher("/jsp/login.jsp").forward(peticion, respuesta);
        }
    }

    // Crea un usuario nuevo. Verifica que el username no exista, cifra la contraseña y guarda
    private void registrar(HttpServletRequest peticion, HttpServletResponse respuesta)
            throws IOException, ServletException {
        String usuario = peticion.getParameter("username");
        String email = peticion.getParameter("email");
        String contrasena = peticion.getParameter("password");
        String nombre = peticion.getParameter("name");

        if (BaseDatos.buscarUsuarioPorNombreUsuario(usuario) != null) {
            peticion.setAttribute("error", "Username already exists");
            peticion.getRequestDispatcher("/jsp/register.jsp").forward(peticion, respuesta);
            return;
        }

        Usuario nuevo = new Usuario(usuario, email, PasswordUtil.cifrar(contrasena), "USER", nombre);
        BaseDatos.guardarUsuario(nuevo);
        respuesta.sendRedirect(peticion.getContextPath() + "/login");
    }
}
