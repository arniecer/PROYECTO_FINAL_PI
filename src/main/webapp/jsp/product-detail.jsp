<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<jsp:include page="header.jsp">
    <jsp:param name="title" value="${producto.nombre}" />
</jsp:include>
<!-- product-detail.jsp: Detalle completo de un producto con imagen, info, reseñas y formulario para dejar reseña -->

<section class="container product-detail">
    <div class="product-image">
        <c:choose>
            <c:when test="${producto.imagenDatos != null}">
                <img src="${pageContext.request.contextPath}/images/product?id=${producto.id}" alt="${producto.nombre}">
            </c:when>
            <c:otherwise>
                <img src="${pageContext.request.contextPath}/img/${producto.imagen}" alt="${producto.nombre}">
            </c:otherwise>
        </c:choose>
    </div>
    <div class="product-info">
        <h1>${producto.nombre}</h1>
        <p class="price">${producto.precio}€</p>
        <span class="rating">${producto.valoracion} / 5</span>
        <p>${producto.descripcion}</p>
        <ul>
            <c:if test="${producto.nivelScoville != null}">
                <li><strong>Scoville Level:</strong> ${producto.nivelScoville} SHU</li>
            </c:if>
            <li><strong>Category:</strong> ${producto.categoria}</li>
            <c:if test="${producto.paisOrigen != null}">
                <li><strong>Country of Origin:</strong> ${producto.paisOrigen}</li>
            </c:if>
            <c:if test="${producto.enlaceCompra != null}">
                <li><strong>Purchase:</strong> <a href="${producto.enlaceCompra}">${producto.enlaceCompra}</a></li>
            </c:if>
        </ul>
    </div>
</section>

<section class="container reviews-section">
    <h2>Reviews</h2>

    <c:forEach var="resena" items="${resenas}">
        <div class="review">
            <p><strong>${resena.usuario.nombre}</strong> <span class="rating">${resena.valoracion} / 5</span></p>
            <p>${resena.comentario}</p>
            <small><fmt:formatDate value="${resena.fechaCreacion}" pattern="dd/MM/yyyy" /></small>
        </div>
    </c:forEach>

    <c:if test="${empty resenas}">
        <p>No reviews yet. Be the first one to share your opinion!</p>
    </c:if>

    <c:choose>
        <c:when test="${sessionScope.usuario != null}">
            <div class="review-form">
                <h3>Leave a Review</h3>
                <form action="${pageContext.request.contextPath}/review" method="post">
                    <input type="hidden" name="productId" value="${producto.id}">
                    <label>Rating:
                        <select name="rating" required>
                            <option value="1">1</option>
                            <option value="2">2</option>
                            <option value="3">3</option>
                            <option value="4">4</option>
                            <option value="5">5</option>
                        </select>
                    </label>
                    <label>Comment:
                        <textarea name="comment" rows="4" required></textarea>
                    </label>
                    <button type="submit" class="btn">Submit Review</button>
                </form>
            </div>
        </c:when>
        <c:otherwise>
            <p><a href="${pageContext.request.contextPath}/login">Sign in</a> to leave a review.</p>
        </c:otherwise>
    </c:choose>
</section>

<jsp:include page="footer.jsp" />
