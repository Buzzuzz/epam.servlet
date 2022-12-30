<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>

<html>
<%@include file="../components/metadata.jspf" %>
<body>
<%@include file="../components/menu.jspf" %>
<div class="container">
    <div class="row row-cols-1 min-vh-100 justify-content-center align-items-center">
        <div class="col w-50">
            <div class="card">
                <div class="card-body">
                    <form>
                        <div class="text-center">
                            <h2 class="display-6">
                                <fmt:message key="restore_password"/>
                            </h2>
                        </div>
                        <div class="form-outline">
                            <input class="form-control" type="email" id="rest-email"/>
                            <label class="form-label" for="rest-email">
                                <fmt:message key="email"/>*</label>
                        </div>
                        <div class="row text-center">
                            <div class="col">
                                <button class="btn btn-primary w-50">
                                    <fmt:message key="restore"/>
                                </button>
                            </div>
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
