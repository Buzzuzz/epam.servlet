<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<html>
<%@include file="../components/metadata.jspf" %>
<body>
<%@include file="../components/menu.jspf" %>
<div class="container min-vh-100">
    <div class="row row-cols-2 text-center">
        <div class="col">
            <div>
                <div class="card shadow">
                    <div class="card-body">
                        <div class="container">
                            <div class="row mb-2">
                                <div class="col">
                                    <div class="input-group">
                                        <span class="input-group-text" id="basic-addon1">ID</span>
                                        <input type="text" class="form-control" placeholder="Username"
                                               aria-label="Username" value="${sessionScope.loggedUser.u_id}"
                                               disabled>
                                    </div>
                                </div>
                                <div class="col">
                                    <div class="input-group">
                                        <span class="input-group-text">Тип</span>
                                        <input
                                                type="text"
                                                class="form-control"
                                                aria-label="type-user"
                                                value="${sessionScope.loggedUser.user_type}"
                                                disabled>
                                    </div>
                                </div>
                            </div>
                            <div class="row mb-2">
                                <div class="col">
                                    <div class="input-group">
                                        <span class="input-group-text">Ім'я</span>
                                        <input
                                                type="text"
                                                class="form-control"
                                                aria-label="type-user"
                                                value="${sessionScope.loggedUser.first_name}"
                                                required>
                                    </div>
                                </div>
                                <div class="col">
                                    <div class="input-group">
                                        <span class="input-group-text">Прізвище</span>
                                        <input
                                                type="text"
                                                class="form-control"
                                                aria-label="type-user"
                                                value="${sessionScope.loggedUser.last_name}"
                                                required>
                                    </div>
                                </div>
                            </div>
                            <div class="col mb-2">
                                <div class="input-group">
                                    <span class="input-group-text">Email</span>
                                    <input
                                            type="email"
                                            class="form-control"
                                            aria-label="type-user"
                                            value="${sessionScope.loggedUser.email}"
                                            required>
                                </div>
                            </div>
                            <div class="col mb-2">
                                <div class="input-group">
                                    <span class="input-group-text">Password</span>
                                    <input
                                            type="password"
                                            class="form-control"
                                            aria-label="type-user"
                                            value="${sessionScope.loggedUser.password}"
                                            required>
                                </div>
                            </div>
                            <div class="col">
                                <div class="input-group">
                                    <span class="input-group-text">Phone</span>
                                    <input
                                            type="text"
                                            class="form-control"
                                            aria-label="type-user"
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
                    <form action="${pageContext.request.contextPath}/controller" method="get">
                        <button class="btn-primary btn w-100 mb-2" type="submit" name="command" value="user-courses">
                            My courses
                        </button>
                        <button class="btn btn-primary w-100" type="submit" name="command" value="update-user">
                           Save
                        </button>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>
<%@include file="../components/footer.jspf" %>
</body>
</html>
