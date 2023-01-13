<%@include file="../components/metadata.jspf" %>
<html lang="${sessionScope.locale}">
<%@include file="../components/head.jspf" %>
<body>
<%@include file="../components/menu.jspf" %>
<div class="container min-vh-100">
    <div class="row g-3">
        <div class="d-flex justify-content-center">
            <div style="width: 75%">
                <form action="${pageContext.request.contextPath}/controller">
                    <div class="card shadow">
                        <div class="card-body">
                            <div class="row">
                                <div class="col-6">
                                    <div class="input-group">
                            <span class="input-group-text">
                                <fmt:message key="sort_by"/>
                            </span>
                                        <select class="form-select" name="sorting" aria-label="Default select example">
                                            <option value="u_id" ${requestScope.sorting eq 'u_id' ? 'selected="selected"' : ''}>
                                                <fmt:message key="id"/>
                                            </option>
                                            <option value="email"
                                            ${requestScope.sorting eq 'email' ? 'selected="selected"' : ''}>
                                                <fmt:message key="email"/>
                                            </option>
                                            <option value="first_name"
                                            ${requestScope.sorting eq 'first_name' ? 'selected="selected"' : ''}>
                                                <fmt:message key="first_name"/>
                                            </option>
                                            <option value="user_type"
                                            ${requestScope.sorting eq 'user_type' ? 'selected="selected"' : ''}>
                                                <fmt:message key="user_type"/>
                                            </option>
                                        </select>
                                    </div>
                                </div>
                                <div class="col">
                                    <input class="form-control" type="number" name="display" min="1"
                                           value="${requestScope.display != null ? requestScope.display : 5}"
                                           oninput="this.value = !!this.value && Math.abs(this.value) >= 1 ? Math.abs(this.value) : 1"/>
                                </div>
                                <div class="col">
                                    <button class="btn btn-primary w-100" type="submit" name="command"
                                            value="get-all-users">
                                        <fmt:message key="search"/>
                                        <i class="fa-solid fa-magnifying-glass"></i>
                                    </button>
                                </div>
                                <div class="col">
                                    <button class="btn btn-success w-100" type="button" data-bs-toggle="modal"
                                            data-bs-target="#exampleModal">
                                        <fmt:message key="add_user"/>
                                        <i class="fa-solid fa-plus"></i>
                                    </button>
                                </div>
                            </div>
                        </div>
                    </div>
                </form>
            </div>
        </div>
        <div class="card shadow">
            <div class="card-body">
                <table class="table table-striped table-bordered border-dark m-0">
                    <thead class="text-center">
                    <tr>
                        <th scope="col">#</th>
                        <th scope="col">
                            <fmt:message key="email"/>
                        </th>
                        <th scope="col">
                            <fmt:message key="first_name"/>
                        </th>
                        <th scope="col">
                            <fmt:message key="user_type"/>
                        </th>
                        <th scope="col">
                            <fmt:message key="actions"/>
                        </th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach items="${requestScope.users}" var="user">
                        <form action="${pageContext.request.contextPath}/controller" method="post">
                            <input hidden name="u_id" value="${user.userId}"/>
                            <input hidden name="user-status" value="${user.isBlocked}"/>

                            <input hidden name="sorting" value="${requestScope.sorting}"/>
                            <input hidden name="page" value="${requestScope.page}">
                            <input hidden name="display" value="${requestScope.display}">

                            <tr>
                                <th scope="row">
                                        ${user.userId}
                                </th>
                                <td>
                                        ${user.email}
                                </td>
                                <td>
                                        ${user.firstName} ${user.lastName}
                                </td>
                                <td>
                                        ${user.userType}
                                </td>
                                <td>
                                    <div class="row">
                                        <div class="col">
                                            <button class="btn ${user.isBlocked eq 'true' ? 'btn-success' : ''} w-100"
                                                    name="command"
                                                    value="change-lock"
                                                ${user.isBlocked eq 'false' ? 'style="background-color: var(--orange)"': ''}>
                                                <c:choose>
                                                    <c:when test="${user.isBlocked eq 'true'}">
                                                        <i class="fa-solid fa-lock-open"></i>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <i class="fa-solid fa-lock"></i>
                                                    </c:otherwise>
                                                </c:choose>
                                            </button>
                                        </div>
                                        <div class="col">
                                            <button class="btn btn-danger w-100" name="command" value="delete-user">
                                                <i class="fa-solid fa-trash"></i>
                                            </button>
                                        </div>
                                    </div>
                                </td>
                            </tr>
                        </form>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
        <!-- Pagination -->
        <form action="${pageContext.request.contextPath}/controller">

            <input hidden name="sorting" value="${requestScope.sorting}"/>
            <input hidden name="display" value="${requestScope.display}">
            <input hidden name="command" value="get-all-users"/>

            <nav aria-label="Page navigation example">
                <ul class="pagination justify-content-center">
                    <li class="page-item">
                        <label for="prev-page" class="page-link">
                            <input id="prev-page" hidden type="submit" name="page" value="1"/>
                            &raquo;
                        </label>
                    </li>
                    <c:forEach var="record" items="${requestScope.records}">
                        <li class="page-item">
                            <label for="page${record}"
                                   class="page-link ${record eq requestScope.page ? 'selected' : ''}">
                                <input id="page${record}" hidden type="submit" name="page" value="${record}"/>
                                    ${record}
                            </label>
                        </li>
                    </c:forEach>
                    <li class="page-item">
                        <label for="next-page" class="page-link">
                            <input id="next-page" hidden type="submit" name="page"
                                   value="${fn:length(requestScope.records)}"/>
                            &raquo;
                        </label>
                    </li>
                </ul>
            </nav>
        </form>
        <!-- Modal create user -->
        <div class="modal fade" id="exampleModal" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">
            <div class="modal-dialog">
                <form action="${pageContext.request.contextPath}/controller" method="post">

                    <input hidden name="sorting" value="${requestScope.sorting}"/>
                    <input hidden name="page" value="${requestScope.page}">
                    <input hidden name="display" value="${requestScope.display}">

                    <div class="modal-content">
                        <div class="modal-header border-0">
                            <h1 class="modal-title fs-5 col" id="exampleModalLabel">
                                <fmt:message key="new_user"/>
                            </h1>
                            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                        </div>
                        <div class="modal-body">
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
                            <div class="input-group mb-2">
                                <span class="input-group-text">
                                    <fmt:message key="user_type"/>
                                </span>
                                <select class="form-select" name="userType" aria-label="userType" required>
                                    <c:forEach var="type" items="${requestScope.types}">
                                        <option value="${type}">${type}</option>
                                    </c:forEach>
                                </select>
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
                        </c:if>
                            </span>
                        </div>
                        <div class="modal-footer border-0">
                            <div class="row w-100 justify-content-center">
                                <div class="col">
                                    <button type="button" class="btn btn-danger w-100" data-bs-dismiss="modal">
                                        <fmt:message key="cancel"/>
                                        <i class="fa-solid fa-ban"></i>
                                    </button>
                                </div>
                                <div class="col">
                                    <button type="submit" class="btn btn-success w-100" name="command" value="add-user">
                                        <fmt:message key="add_user"/>
                                        <i class="fa-solid fa-plus"></i>
                                    </button>
                                </div>
                            </div>
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