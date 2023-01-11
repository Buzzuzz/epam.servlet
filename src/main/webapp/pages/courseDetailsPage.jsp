<%@ taglib prefix="d" uri="https://epam.servlet/tag_handlers" %>
<%@include file="../components/metadata.jspf" %>
<html>
<%@include file="../components/head.jspf" %>
<body>
<%@include file="../components/menu.jspf" %>
<!-- // TODO add topic description,
         topic selection - dropdown,
         teacher selection - dropdown -->
<div class="container min-vh-100">
    <div class="row justify-content-center">
        <div class="col-8">
            <div class="card shadow mb-4">
                <div class="card-body">
                    <div class="text-center">
                        <h5 class="card-title">
                            <fmt:message key="course"/> #${requestScope.course.courseId}
                        </h5>
                    </div>
                    <form action="${pageContext.request.contextPath}/controller" class="d-grid gap-2">
                        <div class="row">
                            <div class="col-7">
                                <div class="input-group">
                            <span class="input-group-text">
                                <fmt:message key="course_name"/>
                            </span>
                                    <input class="form-control" name="courseName"
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
                                    <input class="form-control" name="topicName"
                                           value="${requestScope.course.topicName}"
                                    ${sessionScope.userType eq 'ADMINISTRATOR' ? '' : 'disabled'}
                                           aria-label="topicName"
                                    />
                                </div>
                            </div>
                        </div>
                        <div class="input-group">
                            <span class="input-group-text">
                                <fmt:message key="description"/>
                            </span>
                            <textarea class="form-control"
                                      name="courseDescription"
                            ${sessionScope.userType eq 'ADMINISTRATOR' ? '' : 'disabled'} aria-label="courseDescription"
                            >${requestScope.course.courseDescription}</textarea>
                        </div>
                        <div class="row">
                            <div class="col">
                                <div class="input-group">
                            <span class="input-group-text">
                                <fmt:message key="teacher"/>
                            </span>
                                    <input class="form-control" name="firstName"
                                           value="${requestScope.course.firstName}"
                                    ${sessionScope.userType eq 'ADMINISTRATOR' ? '' : 'disabled'}
                                           aria-label="teacherName"
                                    />
                                </div>
                            </div>
                            <div class="col">
                                <input class="form-control" name="lastName"
                                       value="${requestScope.course.lastName}"
                                ${sessionScope.userType eq 'ADMINISTRATOR' ? '' : 'disabled'}
                                       aria-label="lastName"
                                >
                            </div>
                        </div>

                        <div class="row">
                            <div class="col">
                                <div class="input-group">
                                    <span class="input-group-text">
                                        <fmt:message key="start_date"/>
                                    </span>
                                    <input class="form-control" aria-label="startDate" type="date"
                                           name="startDate"
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
                                    <input class="form-control" aria-label="endDate" type="date"
                                           name="endDate" value="<d:dateOnly date="${requestScope.course.endDate}"/>"
                                    ${sessionScope.userType eq 'ADMINISTRATOR' ? '' : 'disabled'}
                                    />
                                </div>
                            </div>
                        </div>
                        <div class="row">
                            <c:if test="${sessionScope.userType eq 'STUDENT'}">
                                <div class="col">
                                    <button class="btn btn-info w-100" name="command" value="enroll">
                                        <fmt:message key="enroll"/>
                                    </button>
                                </div>
                            </c:if>
                            <c:if test="${sessionScope.userType eq 'ADMINISTRATOR'}">
                                <div class="col">
                                    <button class="btn btn-info w-100" name="command" value="update-course">
                                        <fmt:message key="save"/>
                                    </button>
                                </div>
                                <div class="col">
                                    <button class="btn btn-danger w-100" name="command" value="delete-course">
                                        <fmt:message key="delete"/>
                                    </button>
                                </div>
                            </c:if>
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
