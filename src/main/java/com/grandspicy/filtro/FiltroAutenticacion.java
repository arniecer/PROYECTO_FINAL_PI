package com.grandspicy.filtro;

import com.grandspicy.modelo.Usuario;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter(urlPatterns = {"/admin/*", "/profile", "/review"})
public class FiltroAutenticacion implements Filter {

    @Override
    public void doFilter(ServletRequest peticion, ServletResponse respuesta, FilterChain cadena)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) peticion;
        HttpServletResponse res = (HttpServletResponse) respuesta;
        String ruta = req.getRequestURI();

        boolean rutaProtegida = ruta.equals(req.getContextPath() + "/profile")
                             || ruta.equals(req.getContextPath() + "/review")
                             || ruta.startsWith(req.getContextPath() + "/admin");

        if (!rutaProtegida) {
            cadena.doFilter(peticion, respuesta);
            return;
        }

        HttpSession sesion = req.getSession(false);
        if (sesion == null || sesion.getAttribute("usuario") == null) {
            res.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        if (ruta.startsWith(req.getContextPath() + "/admin")) {
            Usuario usuario = (Usuario) sesion.getAttribute("usuario");
            if (!"ADMIN".equals(usuario.getRol())) {
                res.sendRedirect(req.getContextPath() + "/login");
                return;
            }
        }

        cadena.doFilter(peticion, respuesta);
    }
}
