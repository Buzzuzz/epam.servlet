<%@include file="../components/metadata.jspf" %>
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
                            <button class="btn btn-secondary dropdown-toggle w-100" type="button"
                                    data-bs-toggle="dropdown"
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
                            <button class="btn btn-secondary dropdown-toggle w-100" type="button"
                                    data-bs-toggle="dropdown"
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
                            <button class="btn btn-secondary dropdown-toggle w-100" type="button"
                                    data-bs-toggle="dropdown"
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
                        <!-- Button trigger modal -->
                        <div class="col">
                            <button type="button" class="btn btn-success w-100" data-bs-toggle="modal"
                                    data-bs-target="#exampleModal">
                                <fmt:message key="new_course"/>
                                <i class="fa-solid fa-plus"></i>
                            </button>
                        </div>
                    </c:if>
                </div>
            </form>
        </div>
    </div>
    <div class="row">
        <div class="col">
            <div class="card shadow">
                <div class="card-body">
                    <table class="table table-striped table-bordered border-dark mb-0">
                        <thead class="text-center">
                        <tr>
                            <th scope="col">#</th>

                            <th scope="col">
                                <fmt:message key="course_name"/>
                            </th>
                            <th scope="col" style="width: 25%">
                                <fmt:message key="topic"/>
                            </th>
                            <th>
                                <fmt:message key="teacher"/>
                            </th>
                            <th scope="colgroup" colspan="3" style="width:17%">
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

    <!-- Modal -->
    <form action="${pageContext.request.contextPath}/controller" method="post">
        <div class="modal fade" id="exampleModal" tabindex="-1" aria-labelledby="exampleModalLabel"
             aria-hidden="true">
            <div class="modal-dialog modal-lg">
                <div class="modal-content">
                    <div class="modal-header border-0">
                        <h1 class="modal-title fs-5" id="exampleModalLabel">
                            <fmt:message key="new_course"/>
                        </h1>
                        <button type="button" class="btn-close" data-bs-dismiss="modal"
                                aria-label="Close"></button>
                    </div>

                    <div class="modal-body">
                        <c:if test="${sessionScope.error.value == 'endDate'}">
                            <h5>
                                <div class="alert bg-danger">
                                                    <span class="closebtn"
                                                          onclick="this.parentElement.style.display='none';">&times;</span>
                                    <fmt:message key="end_date_error"/>
                                </div>
                            </h5>
                        </c:if>
                        <div class="row g-3">
                            <div class="input-group">
                        <span class="input-group-text">
                            <fmt:message key="course_name"/>
                        </span>
                                <input class="form-control" name="courseName" required
                                       aria-label="courseName"/>
                            </div>
                            <div>
                                <div class="row">
                                    <div class="col">
                                        <div class="input-group">
                                        <span class="input-group-text">
                                            <fmt:message key="topic"/>
                                        </span>
                                            <select class="form-select" name="t_id" required>
                                                <c:forEach var="topic" items="${requestScope.courses[0].topics}">
                                                    <option ${topic.topicId eq requestScope.course.currentTopicId ? 'selected' : ''}
                                                            value="${topic.topicId}">
                                                            ${topic.topicName}
                                                    </option>
                                                </c:forEach>
                                            </select>
                                        </div>
                                    </div>
                                    <div class="col">
                                        <div class="input-group">
                                        <span class="input-group-text">
                                            <fmt:message key="teacher"/>
                                        </span>
                                            <select class="form-select" name="u_id" required>
                                                <c:forEach var="teacher" items="${requestScope.courses[0].teachers}">
                                                    <option value="${teacher.userId}">
                                                            ${teacher.firstName} ${teacher.lastName}
                                                    </option>
                                                </c:forEach>
                                            </select>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="input-group">
                            <span class="input-group-text">
                                <fmt:message key="description"/>
                            </span>
                                <textarea class="form-control" name="courseDescription" required
                                          aria-label="courseDescription"></textarea>
                            </div>
                            <div>
                                <div class="row">
                                    <div class="col">
                                        <div class="input-group">
                                    <span class="input-group-text">
                                        <fmt:message key="start_date"/>
                                    </span>
                                            <input class="form-control" aria-label="startDate" type="date"
                                                   name="startDate"
                                                   value="<fmt:formatDate value="${now}" pattern="yyyy-MM-dd" />"
                                                   required/>
                                        </div>
                                    </div>
                                    <div class="col">
                                        <div class="input-group">
                                    <span class="input-group-text">
                                        <fmt:message key="end_date"/>
                                    </span>
                                            <input class="form-control" aria-label="endDate" type="date" name="endDate"
                                                   value="<fmt:formatDate value="${now}" pattern="yyyy-MM-dd" />"
                                                   required/>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="modal-footer border-0">
                        <div class="row w-100 justify-content-center">
                            <div class="col">
                                <button type="button" class="btn btn-danger w-100"
                                        data-bs-dismiss="modal">
                                    <fmt:message key="cancel"/>
                                    <i class="fa-solid fa-ban"></i>
                                </button>
                            </div>
                            <div class="col">
                                <button type="submit" class="btn btn-success w-100" name="command"
                                        value="add-course">
                                    <fmt:message key="add_course"/>
                                    <i class="fa-solid fa-plus"></i>
                                </button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </form>
</div>
${sessionScope.remove("error")}
<%@include file="../components/footer.jspf" %>
</body>
</html>
