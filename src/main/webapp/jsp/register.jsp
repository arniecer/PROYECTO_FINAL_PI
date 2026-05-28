<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:include page="header.jsp">
    <jsp:param name="title" value="Create Account" />
</jsp:include>
<!-- register.jsp: Formulario de registro de nuevo usuario -->

<section class="container form-section">
    <h1>Create Account</h1>
    <c:if test="${error != null}">
        <p class="error">${error}</p>
    </c:if>
    <form action="${pageContext.request.contextPath}/register" method="post" class="form">
        <label>Name:
            <input type="text" name="name" required>
        </label>
        <label>Username:
            <input type="text" name="username" required>
        </label>
        <label>Email:
            <input type="email" name="email" required>
        </label>
        <label>Password:
            <input type="password" name="password" required>
        </label>
        <button type="submit" class="btn">Register</button>
    </form>
    <p>Already have an account? <a href="${pageContext.request.contextPath}/login">Sign In</a></p>
</section>

<jsp:include page="footer.jsp" />
