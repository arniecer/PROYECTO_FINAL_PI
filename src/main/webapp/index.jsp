<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
    // Redirige a /home al entrar en la raiz
    response.sendRedirect(request.getContextPath() + "/home");
%>
