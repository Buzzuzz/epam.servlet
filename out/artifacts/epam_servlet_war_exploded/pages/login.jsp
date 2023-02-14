<%@include file="../components/metadata.jspf" %>
<html lang="${sessionScope.locale}">
<%@include file="../components/head.jspf" %>
<body>
<%@include file="../components/menu.jspf" %>
<div class="container min-vh-100">
    <div class="row row-cols-1 justify-content-center h-100">
        <div class="col w-50 align-self-center">
            <div class="card shadow">
                <div class="card-body">
                    <div class="card-title">
                        <h5>
                            <c:if test="${sessionScope.error.value == 'none'}">
                                <div class="alert bg-success">
                                    <span class="closebtn"
                                          onclick="this.parentElement.style.display='none';">&times;</span>
                                    <fmt:message key="registration_successful"/>
                                </div>
                            </c:if>
                            <c:if test="${sessionScope.error.value != 'none'}">
                                <c:if test="${sessionScope.error.value == 'email'}">
                                    <div class="alert bg-danger">
                                    <span class="closebtn"
                                          onclick="this.parentElement.style.display='none';">&times;</span>
                                        <fmt:message key="wrong_email"/>
                                    </div>
                                </c:if>
                                <c:if test="${sessionScope.error.value == 'password'}">
                                    <div class="alert bg-danger">
                                    <span class="closebtn"
                                          onclick="this.parentElement.style.display='none';">&times;</span>
                                        <fmt:message key="wrong_password"/>
                                    </div>
                                </c:if>
                            </c:if>
                        </h5>
                    </div>
                    <div class="text-center">
                        <h2 class="display-6">
                            <fmt:message key="enter"/>
                        </h2>
                    </div>
                    <form action="${applicationScope.controller}" method="post">
                        <input type="hidden" name="command" value="login"/>
                        <div class="form-outline">
                            <input type="email" id="email" class="form-control" name="email" required/>
                            <label class="form-label" for="email">
                                <fmt:message key="email"/>*
                            </label>
                        </div>
                        <div class="form-outline">
                            <input type="password" name="password" id="pas" class="form-control" required/>
                            <label class="form-label" for="pas">
                                <fmt:message key="password"/>*
                            </label>
                        </div>
                        <div class="text-center mb-4">
                            <button type="submit" class="btn btn-primary btn-block w-50">
                                <fmt:message key="login"/>
                            </button>
                        </div>
                        <div class="row row-cols-2 justify-content-center text-center">
                            <div class="col">
                                <a href="${pageContext.request.contextPath}/pages/signup.jsp">
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
${sessionScope.remove("error")}
<%@include file="../components/footer.jspf" %>
</body>
</html>
