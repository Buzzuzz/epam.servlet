<%@include file="../components/metadata.jspf" %>
<html lang="${sessionScope.locale}">
<%@include file="../components/head.jspf" %>
<body>
<%@include file="../components/menu.jspf" %>
<div class="container min-vh-100">
    <!-- Pagination bar (and sorting, filters) -->
    <form action="${pageContext.request.contextPath}/controller">
        <div class="card shadow mb-3">
            <div class="card-body">
                <div class="row justify-items-center">
                    <!-- // TODO sorting options-->
                    <div class="col">
                        <div class="input-group">
                            <span class="input-group-text">
                                <fmt:message key="sort_by"/>
                            </span>
                            <select class="form-select" name="sorting" aria-label="sorting select">
                                <option value="c_id" ${requestScope.sorting eq 'c_id' ? 'selected="selected"' : ''}>
                                    <fmt:message key="id"/> <fmt:message key="asc"/>
                                </option>
                                <option value="c_id desc" ${requestScope.sorting eq 'c_id desc' ? 'selected="selected"' : ''}>
                                    <fmt:message key="id"/> <fmt:message key="desc"/>
                                </option>
                                <option value="name" ${requestScope.sorting eq 'name' ? 'selected="selected"' : ''}>
                                    <fmt:message key="course_name"/> <fmt:message key="a_z"/>
                                </option>
                                <option value="name desc" ${requestScope.sorting eq 'name desc' ? 'selected="selected"' : ''}>
                                    <fmt:message key="course_name"/> <fmt:message key="z_a"/>
                                </option>
                                <option value="" ${requestScope.sorting eq '' ? 'selected="selected"' : ''}>
                                    <fmt:message key="course_duration"/> <fmt:message key="asc"/>
                                </option>
                                <option value="" ${requestScope.sorting eq '' ? 'selected="selected"' : ''}>
                                    <fmt:message key="course_duration"/> <fmt:message key="desc"/>
                                </option>
                                <option value="" ${requestScope.sorting eq '' ? 'selected="selected"' : ''}>
                                    <fmt:message key="enrolled_students"/> <fmt:message key="asc"/>
                                </option>
                                <option value="" ${requestScope.sorting eq '' ? 'selected="selected"' : ''}>
                                    <fmt:message key="enrolled_students"/> <fmt:message key="desc"/>
                                </option>
                            </select>
                        </div>
                    </div>
                    <div class="col">
                        <div class="input-group">
                            <span class="input-group-text">
                                <fmt:message key="teacher"/>
                            </span>
                            <select class="form-select" name="u_id">
                                <option value="none">
                                    <fmt:message key="filter_all"/>
                                </option>
                                <c:forEach var="teacher" items="${requestScope.courses[0].teachers}">
                                    <option value="${teacher.userId}"
                                    ${requestScope.u_id == 'none' ? '' : requestScope.u_id == teacher.userId ? 'selected="selected"' : ''}>
                                            ${teacher.firstName} ${teacher.lastName}
                                    </option>
                                </c:forEach>
                            </select>
                        </div>
                    </div>
                    <div class="col">
                        <div class="input-group">
                            <span class="input-group-text">
                                <fmt:message key="topic"/>
                            </span>
                            <select class="form-select" name="t_id">
                                <option value="none">
                                    <fmt:message key="filter_all"/>
                                </option>
                                <c:forEach var="topic" items="${requestScope.courses[0].topics}">
                                    <option value="${topic.topicId}"
                                    ${requestScope.t_id eq 'none' ? '' : requestScope.t_id eq topic.topicId ? 'selected="selected"' : ''}>
                                            ${topic.topicName}
                                    </option>
                                </c:forEach>
                            </select>
                        </div>
                    </div>
                    <div class="col-2">
                        <input class="form-control" type="number" name="display" min="1"
                                value="${requestScope.display != null ? requestScope.display : 5}"
                                oninput="this.value = !!this.value && Math.abs(this.value) >= 1 ? Math.abs(this.value) : 1"/>
                    </div>
                </div>
            </div>
        </div>
        <div class="container d-flex justify-content-center">
            <div class="card shadow mb-3 w-50">
                <div class="card-body">
                    <div class="row">
                        <div class="col">
                            <button class="btn btn-info w-100" name="command" value="get-all-courses">
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
                </div>
            </div>
        </div>
    </form>

    <!-- Main table -->
    <div class="row mb-3">
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

    <!-- Pagination -->
    <form action="${pageContext.request.contextPath}/controller">

        <input hidden name="sorting" value="${requestScope.sorting}"/>
        <input hidden name="display" value="${requestScope.display}">
        <input hidden name="command" value="get-all-courses"/>
        <input hidden name="t_id" value="${requestScope.t_id}">
        <input hidden name="u_id" value="${requestScope.u_id}">

        <nav aria-label="Page navigation example">
            <ul class="pagination justify-content-center">
                <li class="page-item">
                    <label for="prev-page" class="page-link">
                        <input id="prev-page" hidden type="submit" name="page" value="1"/>
                        &laquo;
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
