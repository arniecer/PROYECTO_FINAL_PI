<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:include page="../header.jsp">
    <jsp:param name="title" value="Admin Panel" />
</jsp:include>
<!-- admin/dashboard.jsp: Panel de administracion con resumen de productos y usuarios -->

<section class="container">
    <h1>Admin Panel</h1>
    <div class="admin-stats">
        <div class="stat-card">
            <h3>Products</h3>
            <p class="stat-number">${cantidadProductos}</p>
            <a href="${pageContext.request.contextPath}/admin/products" class="btn">Manage</a>
        </div>
        <div class="stat-card">
            <h3>Users</h3>
            <p class="stat-number">${cantidadUsuarios}</p>
            <a href="${pageContext.request.contextPath}/admin/users" class="btn">Manage</a>
        </div>
    </div>
</section>

<jsp:include page="../footer.jsp" />
