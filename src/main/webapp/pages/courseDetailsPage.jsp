<%@include file="../components/metadata.jspf" %>
<html>
<%@include file="../components/head.jspf" %>
<body>
<%@include file="../components/menu.jspf" %>
<div class="container min-vh-100">
    <div class="row justify-content-center">
        <div class="col-2"></div>
        <div class="col-8">
            <div class="card shadow mb-4">
                <div class="card-body">
                    <div class="text-center">
                        <h5 class="card-title">
                            <c:if test="${sessionScope.error.value == 'endDate'}">
                                <div class="alert bg-danger">
                                    <span class="closebtn"
                                          onclick="this.parentElement.style.display='none';">&times;</span>
                                    <fmt:message key="end_date_error"/>
                                </div>
                            </c:if>
                            <c:if test="${sessionScope.error.value eq 'none'}">
                                <div class="alert bg-success">
                                    <span class="closebtn"
                                          onclick="this.parentElement.style.display='none';">&times;</span>
                                    <fmt:message key="success"/>
                                </div>
                            </c:if>
                            <c:if test="${sessionScope.error.value eq 'db-error'}">
                                <div class="alert bg-danger">
                                    <span class="closebtn"
                                          onclick="this.parentElement.style.display='none';">&times;</span>
                                    <fmt:message key="db_error"/>
                                </div>
                            </c:if>
                            <div class="row mb-3">
                                <div class="col">
                                    <div class="input-group">
                                        <span class="input-group-text">
                                            <fmt:message key="course"/>
                                        </span>
                                        <input disabled class="form-control" value="#${requestScope.course.courseId}">
                                    </div>
                                </div>
                                <div class="col">
                                    <div class="input-group">
                                        <span class="input-group-text">
                                            <fmt:message key="enrolled_students"/>:
                                        </span>
                                        <input disabled class="form-control"
                                               value="${requestScope.course.enrolled eq 0 ? 0 : requestScope.course.enrolled - 1}">
                                    </div>
                                </div>
                            </div>
                        </h5>
                    </div>
                    <form action="${pageContext.request.contextPath}/controller" class="d-grid gap-3" method="post">
                        <input hidden name="c_id" value="${requestScope.course.courseId}"/>
                        <div class="row">
                            <div class="col-7">
                                <div class="input-group">
                            <span class="input-group-text">
                                <fmt:message key="course_name"/>
                            </span>
                                    <input class="form-control" name="courseName" required
                                           value="${requestScope.course.courseName}"
                                    ${sessionScope.userType eq 'ADMINISTRATOR' ? '' : 'disabled'}
                                           aria-label="courseName"/>
                                </div>
                            </div>
                            <div class="col">
                                <div class="input-group">
                                    <span class="input-group-text">
                                        <fmt:message key="topic"/>
                                    </span>
                                    <select class="form-select" required
                                            name="t_id" ${sessionScope.userType eq 'ADMINISTRATOR' ? '' : 'disabled'}
                                    >
                                        <c:forEach var="topic" items="${requestScope.course.topics}">
                                            <option ${topic.topicId eq requestScope.course.currentTopicId ? 'selected' : ''}
                                                    value="${topic.topicId}">
                                                    ${topic.topicName}
                                            </option>
                                        </c:forEach>
                                    </select>
                                </div>
                            </div>
                        </div>
                        <div class="input-group">
                            <span class="input-group-text">
                                <fmt:message key="description"/>
                            </span>
                            <textarea class="form-control"
                                      name="courseDescription" required
                            ${sessionScope.userType eq 'ADMINISTRATOR' ? '' : 'disabled'} aria-label="courseDescription"
                            >${requestScope.course.courseDescription}</textarea>
                        </div>
                        <div class="input-group">
                            <span class="input-group-text">
                                <fmt:message key="about_topic"/>
                            </span>
                            <textarea class="form-control" disabled aria-label="topicDescription"
                            >${requestScope.course.currentTopicDescription}</textarea>
                        </div>
                        <div class="col">
                            <div class="row">
                                <div class="col">
                                    <div class="input-group">
                                    <span class="input-group-text">
                                        <fmt:message key="teacher"/>
                                    </span>
                                        <select class="form-select" required
                                                name="u_id" ${sessionScope.userType eq 'ADMINISTRATOR' ? '' : 'disabled'}>
                                            <c:forEach var="teacher" items="${requestScope.course.teachers}">
                                                <option ${teacher.userId eq requestScope.course.currentTeacherId ? 'selected="selected"' : ''}
                                                        value="${teacher.userId}">
                                                        ${teacher.firstName} ${teacher.lastName}
                                                </option>
                                            </c:forEach>
                                        </select>
                                    </div>
                                </div>
                                <div class="col">
                                    <div class="input-group">
                                        <span class="input-group-text">
                                            <fmt:message key="course_duration"/>
                                        </span>
                                        <input class="form-control d-inline" disabled
                                               value="${requestScope.course.duration}">
                                        <input class="form-control d-inline" disabled value="<fmt:message key="days"/>">
                                    </div>
                                </div>
                            </div>
                        </div>

                        <div class="row">
                            <div class="col">
                                <div class="input-group">
                                    <span class="input-group-text">
                                        <fmt:message key="start_date"/>
                                    </span>
                                    <input class="form-control" aria-label="startDate" type="date"
                                           name="startDate" required
                                           value="<d:dateOnly date="${requestScope.course.startDate}"/>"
                                    ${sessionScope.userType eq 'ADMINISTRATOR' ? '' : 'disabled'}
                                    />
                                </div>
                            </div>
                            <div class="col">
                                <div class="input-group">
                                    <span class="input-group-text">
                                        <fmt:message key="end_date"/>
                                    </span>
                                    <input class="form-control" aria-label="endDate" type="date" required
                                           name="endDate" value="<d:dateOnly date="${requestScope.course.endDate}"/>"
                                    ${sessionScope.userType eq 'ADMINISTRATOR' ? '' : 'disabled'}
                                    />
                                </div>
                            </div>
                        </div>
                        <div class="row justify-content-center">
                            <c:if test="${sessionScope.userType eq 'STUDENT'}">
                                <jsp:useBean id="today" class="java.util.Date"/>
                                <button class="btn btn-info w-50" name="command" value="enroll"
                                        <c:if test="${today.time gt requestScope.course.startDate.time}">
                                            disabled
                                        </c:if>>
                                    <fmt:message key="enroll"/>
                                </button>

                            </c:if>
                            <c:if test="${sessionScope.userType eq 'ADMINISTRATOR'}">
                                <div class="col">
                                    <button class="btn btn-info w-100" name="command" value="update-course">
                                        <fmt:message key="save"/>
                                        <i class="fa-solid fa-floppy-disk"></i>
                                    </button>
                                </div>
                                <div class="col">
                                    <button class="btn btn-danger w-100" name="command" value="delete-course">
                                        <fmt:message key="delete"/>
                                        <i class="fa-solid fa-trash"></i>
                                    </button>
                                </div>
                            </c:if>
                        </div>
                    </form>
                </div>
            </div>
        </div>
        <div class="col-2">
            <c:if test="${requestScope.final_mark > 0}">
                <div class="card shadow">
                    <div class="card-body">
                        <div class="card-title text-center">
                            <h5 class="display-6">
                                <fmt:message key="mark"/>
                            </h5>
                            <h2>
                                    ${requestScope.final_mark}
                            </h2>
                        </div>
                    </div>
                </div>
            </c:if>
        </div>
    </div>
</div>
${sessionScope.remove("error")}
<%@include file="../components/footer.jspf" %>
</body>
</html>
