<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<html>
<%@include file="../components/metadata.jspf" %>
<body>
<%@include file="../components/menu.jspf" %>
<div class="container min-vh-100">
    <div class="row row-cols-1 justify-content-center h-100">
        <div class="col w-50 align-self-center">
            <div class="card shadow">
                <div class="card-body">
                    <div class="text-center">
                        <h2 class="display-6">
                            <fmt:message key="enter"/>
                        </h2>
                    </div>
                    <form action="${pageContext.request.contextPath}/controller" method="post">
                        <input type="hidden" name="command" value="login"/>
                        <div class="form-outline">
                            <input type="email" id="email" class="form-control" name="email" required/>
                            <label class="form-label" for="email">
                                <fmt:message key="email"/>*
                            <c:if test="${sessionScope.error == 'email'}">
                                <fmt:message key="wrong_email"/>
                            </c:if>
                            </label>
                        </div>
                        <div class="form-outline">
                            <input type="password" name="password" id="pas" class="form-control" required/>
                            <label class="form-label" for="pas">
                                <fmt:message key="password"/>*
                                <c:if test="${sessionScope.error == 'password'}">
                                    <fmt:message key="wrong_password"/>
                                </c:if>
                            </label>
                        </div>
                        <div class="text-center mb-4">
                            <button type="submit" class="btn btn-primary btn-block w-50">
                                <fmt:message key="login"/>
                            </button>
                        </div>
                        <div class="row row-cols-2 justify-content-center text-center">
                            <div class="col">
                                <a href="${pageContext.request.contextPath}/pages/signin.jsp">
                                    <fmt:message key="signup"/>
                                </a>
                            </div>
                            <div class="col">
                                <a href="${pageContext.request.contextPath}/pages/restore.jsp">
                                    <fmt:message key="forgot_password"/>
                                </a>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>

<%@include file="../components/footer.jspf" %>
</body>
</html>
