<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ attribute name="value" type="com.servlet.ejournal.services.dto.FullCourseDTO" required="true" %>
<fmt:setLocale value="${sessionScope.locale}"/>
<fmt:setBundle basename="locale"/>

<form action="${pageContext.request.contextPath}/controller" method="get">
    <tr>
        <input type="hidden" name="c_id" value="${value.courseId}">

        <th scope="row">${value.courseId}</th>
        <td>${value.courseName}</td>
        <td>
            ${value.currentTopicName}
        </td>
        <td>
            ${value.currentTeacherName}
        </td>
        <td>
            ${value.duration}
        </td>
        <td>
            ${value.enrolled - 1}
        </td>
        <td>
            <button class="btn btn-primary w-100" name="command" type="submit" value="course-details">
                <fmt:message key="details"/>
                <i class="fa-solid fa-square-info"></i>
            </button>
        </td>
        <c:if test="${sessionScope.userType eq 'STUDENT' and requestScope.get('switch') eq 'on'}">
            <td>
                <button class="btn bg-danger w-100" name="command" value="withdraw">
                    <fmt:message key="quit"/>
                </button>
            </td>
        </c:if>
        <c:if test="${requestScope.get('switch') eq 'on' and value.currentTeacherId eq sessionScope.loggedUser.u_id}">
            <td>
                <button class="btn btn-info w-100" name="command" value="marks">
                    <fmt:message key="marks"/>
                </button>
            </td>
        </c:if>
    </tr>
</form>