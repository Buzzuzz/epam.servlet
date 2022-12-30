<%@page isErrorPage="true" %>
<html>
<%@include file="../components/metadata.jspf" %>
<body>
<%@include file="../components/menu.jspf" %>
<div class="min-vh-100 container text-center">
    <h1 class="display-6 mb-5">
        <fmt:message key="error_message"/>
    </h1>
    <div class="row row-cols-1 w-100 justify-content-center">
        <div class="col w-75">
            <table class="table table-striped table-bordered border-dark">
                <tbody>
                <tr>
                    <th scope="row">
                        <fmt:message key="exception"/>
                    </th>
                    <td>${pageContext.exception}</td>
                </tr>
                <tr>
                    <th scope="row">
                        <fmt:message key="requested_uri"/>
                    </th>
                    <td>${pageContext.errorData.requestURI}</td>
                </tr>
                <tr>
                    <th scope="row">
                        <fmt:message key="error_code"/>
                    </th>
                    <td>
                        ${pageContext.errorData.statusCode}
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
        <div class="col">
            <button class="btn btn-primary">
                <a class="nav-link" id="home-button" href="${pageContext.request.contextPath}/pages/index.jsp">
                    <fmt:message key="home_page"/>
                </a></button>
        </div>
    </div>
</div>
<%@include file="../components/footer.jspf" %>
</body>
</html>