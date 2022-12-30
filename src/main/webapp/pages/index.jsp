<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<html>
<%@include file="../components/metadata.jspf" %>
<body>
<%@include file="../components/menu.jspf" %>
<div class="container min-vh-100">
    <div class="row row-cols-1 text-center">
        <div class="col">
            <div>
                <h2 class="display-6 mt-4">
                    <fmt:message key="greeting"/>
                </h2>
                <h2 class="display-6">
                    <fmt:message key="version"/>
                </h2>
            </div>
        </div>
    </div>
</div>
<%@include file="../components/footer.jspf" %>
</body>
</html>
