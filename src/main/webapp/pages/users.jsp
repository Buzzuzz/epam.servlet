<%@include file="../components/metadata.jspf" %>
<html lang="${sessionScope.locale}">
<%@include file="../components/head.jspf" %>
<body>
<%@include file="../components/menu.jspf" %>
<div class="container min-vh-100">
    ${requestScope.users}
</div>

<%@include file="../components/footer.jspf" %>
</body>
</html>