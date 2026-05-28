<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<jsp:include page="header.jsp">
    <jsp:param name="title" value="My Profile" />
</jsp:include>
<!-- profile.jsp: Perfil del usuario logueado con sus datos y todas sus reseñas -->

<section class="container">
    <h1>My Profile</h1>
    <div class="profile-info">
        <p><strong>Name:</strong> ${usuario.nombre}</p>
        <p><strong>Username:</strong> ${usuario.usuario}</p>
        <p><strong>Email:</strong> ${usuario.email}</p>
        <p><strong>Role:</strong> ${usuario.rol}</p>
    </div>

    <h2>My Reviews</h2>
    <c:forEach var="resena" items="${resenas}">
        <div class="review">
            <p><strong>${resena.producto.nombre}</strong> - <span class="rating">${resena.valoracion} / 5</span></p>
            <p>${resena.comentario}</p>
            <small><fmt:formatDate value="${resena.fechaCreacion}" pattern="dd/MM/yyyy" /></small>
        </div>
    </c:forEach>
    <c:if test="${empty resenas}">
        <p>You haven't written any reviews yet.</p>
    </c:if>
</section>

<jsp:include page="footer.jsp" />
