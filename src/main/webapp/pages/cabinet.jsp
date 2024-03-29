<%@include file="../components/metadata.jspf" %>
<%@page import="com.servlet.ejournal.model.entities.UserType" %>
<html lang="${sessionScope.locale}">
<%@include file="../components/head.jspf" %>
<body>
<%@include file="../components/menu.jspf" %>
<form action="${applicationScope.controller}" method="post">
    <div class="container min-vh-100">
        <div class="row row-cols-2 text-center">
            <div class="col">
                <div>
                    <div class="card shadow">
                        <div class="card-body">
                            <h5 class="card-title">
                                <c:if test="${sessionScope.error.value == 'none'}">
                                    <div class="alert bg-success">
                                    <span class="closebtn"
                                          onclick="this.parentElement.style.display='none';">&times;</span>
                                        <fmt:message key="success"/>
                                    </div>
                                </c:if>
                                <c:if test="${sessionScope.error.value == 'password'}">
                                    <div class="alert bg-danger">
                                    <span class="closebtn"
                                          onclick="this.parentElement.style.display='none';">&times;</span>
                                        <fmt:message key="wrong_password"/>
                                    </div>
                                </c:if>
                                <c:if test="${sessionScope.error.value == 'password-repeat'}">
                                    <div class="alert bg-danger">
                                    <span class="closebtn"
                                          onclick="this.parentElement.style.display='none';">&times;</span>
                                        <fmt:message key="password_not_match"/>
                                    </div>
                                </c:if>
                                <c:if test="${sessionScope.error.value == 'phone-number'}">
                                    <div class="alert bg-danger">
                                    <span class="closebtn"
                                          onclick="this.parentElement.style.display='none';">&times;</span>
                                        <fmt:message key="wrong_phone"/>
                                    </div>
                                </c:if>
                            </h5>
                            <div class="container">
                                <div class="row mb-2">
                                    <div class="col">
                                        <div class="input-group">
                                            <span class="input-group-text" id="basic-addon1">
                                                <fmt:message key="id"/>
                                            </span>
                                            <input type="text" class="form-control" placeholder="Username"
                                                   aria-label="Username" value="${sessionScope.loggedUser.u_id}"
                                                   disabled>
                                        </div>
                                    </div>
                                    <div class="col">
                                        <div class="input-group">
                                            <span class="input-group-text">
                                                <fmt:message key="user_type"/>
                                            </span>
                                            <input
                                                    type="text"
                                                    class="form-control"
                                                    aria-label="type-user"
                                                    value="${sessionScope.loggedUser.user_type}"
                                                    disabled>
                                        </div>
                                    </div>
                                </div>
                                <div class="col mb-2">
                                    <div class="input-group">
                                        <span class="input-group-text">
                                            <fmt:message key="email"/>
                                        </span>
                                        <input
                                                type="email"
                                                class="form-control"
                                                aria-label="type-user"
                                                name="email"
                                                value="${sessionScope.loggedUser.email}"
                                                required
                                                disabled>
                                    </div>
                                </div>
                                <div class="row mb-2">
                                    <div class="col">
                                        <div class="input-group">
                                            <span class="input-group-text">
                                                <fmt:message key="first_name"/>
                                            </span>
                                            <input
                                                    type="text"
                                                    class="form-control"
                                                    aria-label="type-user"
                                                    name="f-name"
                                                    value="${sessionScope.loggedUser.first_name}"
                                                    required>
                                        </div>
                                    </div>
                                    <div class="col">
                                        <div class="input-group">
                                            <span class="input-group-text">
                                                <fmt:message key="last_name"/>
                                            </span>
                                            <input
                                                    type="text"
                                                    class="form-control"
                                                    aria-label="type-user"
                                                    name="l-name"
                                                    value="${sessionScope.loggedUser.last_name}"
                                                    required>
                                        </div>
                                    </div>
                                </div>
                                <div class="col mb-2">
                                    <div class="input-group">
                                        <span class="input-group-text">
                                            <fmt:message key="password"/>
                                        </span>
                                        <input
                                                type="password"
                                                class="form-control"
                                                value=""
                                                aria-label="type-user"
                                                name="password"
                                        >
                                    </div>
                                </div>
                                <div class="col mb-2">
                                    <div class="input-group">
                                        <span class="input-group-text">
                                            <fmt:message key="repeat_password"/>
                                        </span>
                                        <input
                                                type="password"
                                                class="form-control"
                                                value=""
                                                aria-label="type-user"
                                                name="password-repeat"
                                        >
                                    </div>
                                </div>
                                <div class="col">
                                    <div class="input-group">
                                        <span class="input-group-text">
                                            <fmt:message key="phone"/>
                                        </span>
                                        <input
                                                type="number"
                                                class="form-control"
                                                aria-label="type-user"
                                                name="phone-number"
                                                value="${sessionScope.loggedUser.phone}"
                                                required>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="col">
                <div class="card shadow">
                    <div class="card-body d-grid gap-2">
                        <button class="btn btn-info w-100" type="submit" name="command" value="update-user">
                            <fmt:message key="save"/>
                        </button>
                        <button class="btn btn-danger w-100" type="submit" name="command" value="logout">
                            <fmt:message key="logout"/>
                        </button>
                    </div>
                </div>
            </div>
        </div>
    </div>
</form>
${sessionScope.remove("error")}
<%@include file="../components/footer.jspf" %>
</body>
</html>
