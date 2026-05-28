<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:include page="header.jsp">
    <jsp:param name="title" value="Sign In" />
</jsp:include>
<!-- login.jsp: Formulario de inicio de sesion -->

<section class="container form-section">
    <h1>Sign In</h1>
    <c:if test="${error != null}">
        <p class="error">${error}</p>
    </c:if>
    <form action="${pageContext.request.contextPath}/login" method="post" class="form">
        <label>Username:
            <input type="text" name="username" required>
        </label>
        <label>Password:
            <input type="password" name="password" required>
        </label>
        <button type="submit" class="btn">Sign In</button>
    </form>
    <p>Don't have an account? <a href="${pageContext.request.contextPath}/register">Register here</a></p>
</section>

<jsp:include page="footer.jsp" />
