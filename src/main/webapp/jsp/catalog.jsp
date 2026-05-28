<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:include page="header.jsp">
    <jsp:param name="title" value="Catalog" />
</jsp:include>
<!-- catalog.jsp: Pagina de catalogo con todos los productos en cuadricula -->

<section class="container">
    <h1>Product Catalog</h1>
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

<jsp:include page="footer.jsp" />
