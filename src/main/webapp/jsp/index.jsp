<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<jsp:include page="header.jsp">
    <jsp:param name="title" value="Spicy Community" />
</jsp:include>
<!-- index.jsp: Pagina principal con hero, productos destacados y reseñas recientes -->

<section class="hero">
    <div class="hero-text">
        <h1>Discover the Spicy World</h1>
        <p>The community where you can explore, review and discover the most amazing spicy products from around the world.</p>
        <a href="${pageContext.request.contextPath}/catalog" class="btn">Explore Catalog</a>
    </div>
</section>

<section class="container">
    <h2>Featured Products</h2>
    <div class="grid">
        <c:forEach var="producto" items="${productos}">
            <div class="card">
                <a href="${pageContext.request.contextPath}/product?id=${producto.id}">
                    <c:choose>
                        <c:when test="${producto.imagenDatos != null}">
                            <img src="${pageContext.request.contextPath}/images/product?id=${producto.id}" alt="${producto.nombre}">
                        </c:when>
                        <c:otherwise>
                            <img src="${pageContext.request.contextPath}/img/${producto.imagen}" alt="${producto.nombre}">
                        </c:otherwise>
                    </c:choose>
                </a>
                <a href="${pageContext.request.contextPath}/product?id=${producto.id}" class="title">${producto.nombre}</a>
                <div class="card-meta">
                    <span class="price">${producto.precio}€</span>
                    <c:if test="${producto.nivelScoville != null}">
                        <span class="scoville-badge">${producto.nivelScoville} SHU</span>
                    </c:if>
                </div>
                <span class="rating">${producto.valoracion} / 5</span>
                <p class="origin">${producto.paisOrigen}</p>
                <a href="${pageContext.request.contextPath}/product?id=${producto.id}" class="btn">See Reviews</a>
            </div>
        </c:forEach>
    </div>
</section>

<section class="container reviews-section">
    <h2>Latest Reviews</h2>
    <c:forEach var="resena" items="${resenasRecientes}">
        <div class="review">
            <p><strong>${resena.usuario.nombre}</strong> on
            <a href="${pageContext.request.contextPath}/product?id=${resena.producto.id}" class="review-product-link">${resena.producto.nombre}</a></p>
            <span class="rating">${resena.valoracion} / 5</span>
            <p>${resena.comentario}</p>
            <small><fmt:formatDate value="${resena.fechaCreacion}" pattern="dd/MM/yyyy" /></small>
        </div>
    </c:forEach>
</section>

<jsp:include page="footer.jsp" />
