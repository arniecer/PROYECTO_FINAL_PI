<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:include page="../header.jsp">
    <jsp:param name="title" value="Admin - Users" />
</jsp:include>
<!-- admin/users.jsp: Gestion de usuarios (listado y eliminar) -->

<section class="container">
    <h1>User Management</h1>
    <table class="admin-table">
        <thead>
            <tr>
                <th>ID</th>
                <th>Username</th>
                <th>Name</th>
                <th>Email</th>
                <th>Role</th>
                <th>Actions</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach var="u" items="${usuarios}">
                <tr>
                    <td>${u.id}</td>
                    <td>${u.usuario}</td>
                    <td>${u.nombre}</td>
                    <td>${u.email}</td>
                    <td>${u.rol}</td>
                    <td>
                        <a href="${pageContext.request.contextPath}/admin/user/delete?id=${u.id}" class="btn-small btn-danger" onclick="return confirm('Delete user?')">Delete</a>
                    </td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
</section>

<jsp:include page="../footer.jsp" />
