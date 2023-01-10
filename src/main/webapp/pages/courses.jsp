<%@include file="../components/metadata.jspf" %>
<%@taglib prefix="ctm" tagdir="/WEB-INF/tags" %>
<html lang="${sessionScope.locale}">
<%@include file="../components/head.jspf" %>
<body>
<%@include file="../components/menu.jspf" %>
<div class="container min-vh-100">
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
                                <fmt:message key="start_date"/>
                            </th>
                            <th>
                                <fmt:message key="end_date"/>
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
