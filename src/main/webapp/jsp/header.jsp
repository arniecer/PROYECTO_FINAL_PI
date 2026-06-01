<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<!-- header.jsp: Cabecera con logo, navegacion y enlaces de login/perfil. Se incluye en todas las paginas. -->
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${param.title} - GrandSpicy</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/estilo.css">
</head>
<body>

<header class="header">
    <div class="logo"><a href="${pageContext.request.contextPath}/home"><img src="${pageContext.request.contextPath}/images/logo.svg" alt="GrandSpicy"></a></div>
    <nav>
        <a href="${pageContext.request.contextPath}/home">Home</a>
        <a href="${pageContext.request.contextPath}/catalog">Catalog</a>
        <c:if test="${sessionScope.usuario != null && sessionScope.usuario.rol == 'ADMIN'}">
            <a href="${pageContext.request.contextPath}/admin">Admin</a>
        </c:if>
    </nav>
    <div class="auth-links">
        <c:choose>
            <c:when test="${sessionScope.usuario == null}">
                <a href="${pageContext.request.contextPath}/login">Sign In</a>
            </c:when>
            <c:otherwise>
                <a href="${pageContext.request.contextPath}/profile">My Profile</a>
                <a href="${pageContext.request.contextPath}/logout">Logout</a>
            </c:otherwise>
        </c:choose>
    </div>
</header>

<main>
