<%@page isErrorPage="true" %>
<html>
<%@include file="../components/metadata.jspf" %>
<body>
<%@include file="../components/menu.jspf" %>
<div class="min-vh-100 container text-center">
    <h1 class="display-6 mb-5">Oops... Something went wrong
        <br>Here is some info about error (pass it to admin)
    </h1>
    <div class="row row-cols-1 w-100 justify-content-center">
        <div class="col w-75">
            <table class="table table-striped table-bordered border-dark">
                <tbody>
                <tr>
                    <th scope="row">Exception</th>
                    <td>${pageContext.exception}</td>
                </tr>
                <tr>
                    <th scope="row">Requested URI</th>
                    <td>${pageContext.errorData.requestURI}</td>
                </tr>
                <tr>
                    <th scope="row">Error Code</th>
                    <td>
                        ${pageContext.errorData.statusCode}
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
    </div>
</div>
<%@include file="../components/footer.jspf" %>
</body>
</html>