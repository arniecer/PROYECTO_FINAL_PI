<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<jsp:include page="header.jsp">
    <jsp:param name="title" value="Error" />
</jsp:include>
<!-- error.jsp: Pagina de error generica con enlace para volver al inicio -->

<section class="container">
    <h1>Something went wrong</h1>
    <p>Please try again or contact the administrator.</p>
    <a href="${pageContext.request.contextPath}/home" class="btn">Go to Home</a>
</section>

<jsp:include page="footer.jsp" />
