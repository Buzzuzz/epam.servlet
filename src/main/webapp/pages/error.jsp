<%@page isErrorPage="true" %>
<%@include file="../components/metadata.jspf" %>
<html lang="${cookie['locale'].value}">
<%@include file="../components/head.jspf" %>
<body>
<%@include file="../components/menu.jspf" %>
<div class="min-vh-100 container text-center">
    <h1 class="display-6 mb-5">
        <fmt:message key="error_message"/>
    </h1>

    <jsp:scriptlet>

            exception.printStackTrace(response.getWriter())

    </jsp:scriptlet>

    <div class="row row-cols-1 w-100 justify-content-center">
        <div class="col w-75">
            <div class="card shadow mb-3">
                <div class="card-body">
                    <table class="table table-striped table-bordered border-dark mb-0">
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
            </div>
        </div>
        <div class="col mb-3">
            <div class="row ms-3">
                <div class="col justify-content-end d-flex">
                    <button class="btn btn-primary w-25">
                        <a class="nav-link" id="home-button" href="${pageContext.request.contextPath}/pages/index.jsp">
                            <fmt:message key="home_page"/>
                        </a>
                    </button>
                </div>
                <div class="col justify-content-start d-flex">
                    <form action="${applicationScope.controller}" class="w-25">
                        <button class="btn btn-danger w-100" name="command" value="logout">
                            <fmt:message key="logout"/>
                        </button>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>
<%@include file="../components/footer.jspf" %>
</body>
</html>