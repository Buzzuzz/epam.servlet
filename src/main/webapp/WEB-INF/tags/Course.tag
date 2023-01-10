<%@ taglib prefix="d" uri="../../WEB-INF/tlds/dateOnly.tld" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ attribute name="value" type="model.entities.Course" required="true" %>
<fmt:setLocale value="${sessionScope.locale}"/>
<fmt:setBundle basename="locale"/>

<form action="${pageContext.request.contextPath}/controller" method="get">
    <tr>
        <input type="hidden" name="c_id" value="${value.c_id}">

        <th scope="row">${value.c_id}</th>
        <td>${value.name}</td>
        <td>
            <d:dateOnly date="${value.start_date}"/>
        </td>
        <td>
            <d:dateOnly date="${value.end_date}"/>
        </td>
        <td>
            <button class="btn btn-primary w-100" name="command" type="submit" value="course-details">
                <fmt:message key="details"/>
            </button>
        </td>
    </tr>
</form>