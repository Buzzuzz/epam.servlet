<%@include file="../components/metadata.jspf" %>
<html lang="${sessionScope.locale}">
<%@include file="../components/head.jspf" %>
<body>
<%@include file="../components/menu.jspf" %>
<div class="container min-vh-100">
    <form action="${pageContext.request.contextPath}/controller" method="post">

        <input hidden name="c_id" value="${requestScope.c_id}">
        <input hidden name="endDate" value="${requestScope.endDate}">

        <div class="card">
            <div class="card-body">
                <div class="card-title d-grid gap-3">
                    <div class="display-6 text-center">
                        <fmt:message key="course"/> ${requestScope.c_id}
                    </div>
                    <jsp:useBean id="today" class="java.util.Date"/>
                    <c:if test="${requestScope.endDate.time gt today.time}">
                        <h5>
                            <div class="alert bg-danger">
                                    <span class="closebtn"
                                          onclick="this.parentElement.style.display='none';">&times;</span>
                                <fmt:message key="course_not_ended"/>
                            </div>
                        </h5>
                    </c:if>
                    <c:if test="${requestScope.error.value == 'db-error'}">
                        k
                    </c:if>
                </div>
                <div class="d-grid gap-3">
                    <div>
                        <table class="table table-striped table-bordered border-dark mb-0">
                            <thead>
                            <tr>
                                <th scope="col">#</th>
                                <th scope="col">
                                    <fmt:message key="student"/>
                                </th>
                                <th scope="col">
                                    <fmt:message key="email"/>
                                </th>
                                <th scope="col">
                                    <fmt:message key="mark"/>
                                </th>
                            </tr>
                            </thead>
                            <tbody>
                            <c:if test="${requestScope.users != null}">
                                <c:forEach var="dto" items="${requestScope.users}">
                                    <tr>
                                        <th scope="row">
                                                ${dto.user.userId}
                                        </th>
                                        <td>
                                                ${dto.user.firstName} ${dto.user.lastName}
                                        </td>
                                        <td>
                                                ${dto.user.email}
                                        </td>
                                        <td style="width: 15%">
                                            <input class="form-control" type="number" min="1.0" max="100.0"
                                                   aria-label="mark"
                                                   step="0.1"
                                                   oninput="this.value = !!this.value && Math.abs(this.value) >= 1.0 && Math.abs(this.value) <= 100.0 ? Math.abs(this.value) : 1.0"
                                                   name="${dto.user.userId}"
                                                   value="${dto.finalMark eq 0 ? 1.0 : dto.finalMark}"
                                            />
                                        </td>
                                    </tr>
                                </c:forEach>
                            </c:if>
                            </tbody>
                        </table>
                    </div>
                    <div class="text-center">
                        <button class="btn btn-primary w-25" ${requestScope.endDate.time gt today.time ? 'disabled' : ''}
                                name="command" value="update-marks">
                            <fmt:message key="save"/>
                        </button>
                    </div>
                </div>
            </div>
        </div>

    </form>
</div>
<%@include file="../components/footer.jspf" %>
</body>
</html>
