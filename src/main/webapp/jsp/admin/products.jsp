<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:include page="../header.jsp">
    <jsp:param name="title" value="Admin - Products" />
</jsp:include>
<!-- admin/products.jsp: Gestion de productos (listado, editar, eliminar, añadir) -->

<section class="container">
    <h1>Product Management</h1>
    <a href="${pageContext.request.contextPath}/admin/product/new" class="btn">Add Product</a>
    <table class="admin-table">
        <thead>
            <tr>
                <th>ID</th>
                <th>Name</th>
                <th>Price</th>
                <th>Category</th>
                <th>Rating</th>
                <th>Actions</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach var="producto" items="${productos}">
                <tr>
                    <td>${producto.id}</td>
                    <td>${producto.nombre}</td>
                    <td>${producto.precio}€</td>
                    <td>${producto.categoria}</td>
                    <td>${producto.valoracion}</td>
                    <td>
                        <a href="${pageContext.request.contextPath}/admin/product/edit?id=${producto.id}" class="btn-small">Edit</a>
                        <form action="${pageContext.request.contextPath}/admin/product/delete" method="post" style="display:inline">
                            <input type="hidden" name="id" value="${producto.id}">
                            <button type="submit" class="btn-small btn-danger" onclick="return confirm('Are you sure?')">Delete</button>
                        </form>
                    </td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
</section>

<jsp:include page="../footer.jsp" />
