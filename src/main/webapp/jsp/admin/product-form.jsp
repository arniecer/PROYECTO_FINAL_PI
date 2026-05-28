<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:include page="../header.jsp">
    <jsp:param name="title" value="${producto.id != null ? 'Edit Product' : 'New Product'}" />
</jsp:include>
<!-- admin/product-form.jsp: Formulario para crear o editar producto (nombre, precio, categoria, imagen...) -->

<section class="container form-section">
    <h1>${producto.id != null ? 'Edit Product' : 'New Product'}</h1>
    <c:if test="${error != null}">
        <p class="error">${error}</p>
    </c:if>
    <form action="${pageContext.request.contextPath}/admin/product/save" method="post" enctype="multipart/form-data" class="form">
        <c:if test="${producto.id != null}">
            <input type="hidden" name="id" value="${producto.id}">
        </c:if>
        <label>Name:
            <input type="text" name="name" value="${producto.nombre}" required>
        </label>
        <label>Description:
            <textarea name="description" rows="4">${producto.descripcion}</textarea>
        </label>
        <label>Price (€):
            <input type="number" step="0.01" name="price" value="${producto.precio}" required>
        </label>
        <label>Category:
            <select name="category" required>
                <option value="Salsas" ${producto.categoria == 'Salsas' ? 'selected' : ''}>Salsas</option>
                <option value="Especias" ${producto.categoria == 'Especias' ? 'selected' : ''}>Especias</option>
                <option value="Snacks" ${producto.categoria == 'Snacks' ? 'selected' : ''}>Snacks</option>
                <option value="Aceites" ${producto.categoria == 'Aceites' ? 'selected' : ''}>Aceites</option>
            </select>
        </label>
        <label>Scoville Level (SHU):
            <input type="number" name="scovilleLevel" value="${producto.nivelScoville}">
        </label>
        <label>Country of Origin:
            <input type="text" name="countryOfOrigin" value="${producto.paisOrigen}">
        </label>
        <label>Purchase Link:
            <input type="url" name="purchaseLink" value="${producto.enlaceCompra}">
        </label>
        <label>Rating:
            <input type="number" step="0.1" name="rating" value="${producto.valoracion}">
        </label>
        <label>Image:
            <input type="file" name="imageFile" accept="image/*">
        </label>
        <button type="submit" class="btn">Save</button>
    </form>
</section>

<jsp:include page="../footer.jsp" />
