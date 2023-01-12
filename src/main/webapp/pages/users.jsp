<%@include file="../components/metadata.jspf" %>
<html lang="${sessionScope.locale}">
<%@include file="../components/head.jspf" %>
<body>
<%@include file="../components/menu.jspf" %>
<div class="container min-vh-100">
    <div class="row">
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
    </div>
</div>

<%@include file="../components/footer.jspf" %>
</body>
</html>