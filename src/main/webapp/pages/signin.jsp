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
                            <fmt:message key="signup_title"/>
                        </h2>
                    </div>
                    <form action="${pageContext.request.contextPath}/controller" method="post">
                        <div class="form-outline">
                            <input type="email" class="form-control" name="email" required/>
                            <label class="form-label">
                                <fmt:message key="email"/>*
                            <c:if test="${sessionScope.error == 'email'}">
                                <fmt:message key="email_in_use"/>
                            </c:if></label>
                        </div>
                        <div class="row row-cols-2">
                            <div class="col">
                                <div class="form-outline">
                                    <input type="password" id="password" class="form-control" name="password"
                                           required/>
                                    <label class="form-label" for="password">
                                        <fmt:message key="password"/>*
                                    <c:if test="${sessionScope.error == 'password'}">
                                        <fmt:message key="wrong_password"/>
                                    </c:if>
                                    </label>
                                </div>
                            </div>
                            <div class="col">
                                <div class="form-outline">
                                    <input type="password" id="password-repeat" class="form-control"
                                           name="password-repeat" required/>
                                    <label class="form-label" for="password-repeat">
                                        <fmt:message key="repeat_password"/>*
                                    <c:if test="${sessionScope.error == 'password-repeat'}">
                                        <fmt:message key="password_not_match"/>
                                    </c:if>
                                    </label>
                                </div>
                            </div>
                        </div>
                        <div class="row row-cols-2">
                            <div class="col">
                                <div class="form-outline">
                                    <input type="text" class="form-control" id="f-name" name="f-name" required/>
                                    <label class="form-label" for="f-name">
                                        <fmt:message key="first_name"/>*</label>
                                </div>
                            </div>
                            <div class="col">
                                <div class="form-outline">
                                    <input type="text" class="form-control" id="l-name" name="l-name" required/>
                                    <label class="form-label" for="l-name">
                                        <fmt:message key="last_name"/>*</label>
                                </div>
                            </div>
                        </div>
                        <div class="input-group">
                            <span class="input-group-text" id="basic-addon1">
                                <fmt:message key="phone"/>
                            </span>
                            <input type="number" class="form-control" name="phone-number" required/>
                        </div>
                        <span>
                            <fmt:message key="phone_title"/>*
                        <c:if test="${sessionScope.error == 'phone-number'}">
                            <fmt:message key="wrong_phone"/>
                        </c:if> </span>
                        <div class="text-center mb-3 mt-2">
                            <button type="submit" class="btn btn-primary btn-block w-50" name="command" value="signup">
                                <fmt:message key="signup"/>
                            </button>
                        </div>
                        <div class="row row-cols-2 justify-content-center text-center">
                            <div class="col">
                                <a href="${pageContext.request.contextPath}/pages/login.jsp">
                                    <fmt:message key="enter"/>
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
