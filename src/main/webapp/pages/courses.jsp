<%@include file="../components/metadata.jspf" %>
<%@taglib prefix="ctm" tagdir="/WEB-INF/tags" %>
<html lang="${sessionScope.locale}">
<%@include file="../components/head.jspf" %>
<body>
<%@include file="../components/menu.jspf" %>
<div class="container min-vh-100">
    <div class="card shadow mb-2">
        <div class="card-body">
            <form action="${pageContext.request.contextPath}/controller">
                <div class="row justify-items-center">
                    <!-- // TODO
                             dropdowns values-->
                    <div class="col">
                        <div class="dropdown-center">
                            <button class="btn btn-secondary dropdown-toggle w-100" type="button" data-bs-toggle="dropdown"
                                    aria-expanded="false">
                                Dropdown button
                            </button>
                            <ul class="dropdown-menu">
                                <li><a class="dropdown-item" href="#">Action</a></li>
                                <li><a class="dropdown-item" href="#">Another action</a></li>
                                <li><a class="dropdown-item" href="#">Something else here</a></li>
                            </ul>
                        </div>
                    </div>
                    <div class="col">
                        <div class="dropdown-center">
                            <button class="btn btn-secondary dropdown-toggle w-100" type="button" data-bs-toggle="dropdown"
                                    aria-expanded="false">
                                Dropdown button
                            </button>
                            <ul class="dropdown-menu">
                                <li><a class="dropdown-item" href="#">Action</a></li>
                                <li><a class="dropdown-item" href="#">Another action</a></li>
                                <li><a class="dropdown-item" href="#">Something else here</a></li>
                            </ul>
                        </div>
                    </div>
                    <div class="col">
                        <div class="dropdown-center">
                            <button class="btn btn-secondary dropdown-toggle w-100" type="button" data-bs-toggle="dropdown"
                                    aria-expanded="false">
                                Dropdown button
                            </button>
                            <ul class="dropdown-menu">
                                <li>
                                    <button class="dropdown-item" type="button">k</button>
                                </li>
                                <li><a class="dropdown-item" href="#">Another action</a></li>
                                <li><a class="dropdown-item" href="#">Something else here</a></li>
                            </ul>
                        </div>
                    </div>
                    <div class="col-1">
                        <input class="form-control" type="number" value="5" aria-label="pagination-counter"/>
                    </div>
                    <div class="col">
                        <button class="btn btn-info w-100">
                            <fmt:message key="search"/>
                            <i class="fa-solid fa-magnifying-glass"></i>
                        </button>
                    </div>
                    <c:if test="${sessionScope.userType eq 'ADMINISTRATOR'}">
                        <div class="col">
                            <button class="btn btn-success w-100">
                                <fmt:message key="new_course"/>
                                <i class="fa-solid fa-plus"></i>
                            </button>
                        </div>
                    </c:if>
                </div>
            </form>
        </div>
    </div>
    <div class="row text-center">
        <div class="col">
            <div class="card shadow">
                <div class="card-body">
                    <table class="table table-striped table-bordered border-dark mb-0">
                        <thead>
                        <tr>
                            <th scope="col">#</th>

                            <th scope="col">
                                <fmt:message key="course_name"/>
                            </th>
                            <th scope="col">
                                <fmt:message key="topic"/>
                            </th>
                            <th>
                                <fmt:message key="teacher"/>
                            </th>
                            <th scope="colgroup" colspan="3">
                                <fmt:message key="actions"/>
                            </th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach items="${requestScope.courses}" var="course">
                            <ctm:Course value="${course}"/>
                        </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
</div>
<%@include file="../components/footer.jspf" %>
</body>
</html>
