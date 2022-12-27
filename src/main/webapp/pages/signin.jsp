<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<html>
<%@include file="../components/metadata.jspf" %>
<body>
<%@include file="../components/menu.jspf" %>
<div class="container min-vh-100">
    <div class="row row-cols-1 justify-content-center h-100">
        <div class="col w-50 align-self-center">
            <div class="card shadow">
                <div class="card-body">
                    <div class="text-center">
                        <h2 class="display-6">Реєстрація</h2>
                    </div>
                    <form>
                        <div class="form-outline">
                            <input type="email" id="form2Example1" class="form-control" name="email"/>
                            <label class="form-label" for="form2Example1">Email address*</label>
                        </div>
                        <div class="row row-cols-2">
                            <div class="col">
                                <div class="form-outline">
                                    <input type="password" id="form2Example2" class="form-control" name="password"/>
                                    <label class="form-label" for="form2Example2">Password*</label>
                                </div>
                            </div>
                            <div class="col">
                                <div class="form-outline">
                                    <input type="password" id="form2Example3" class="form-control" name="password-repeat"/>
                                    <label class="form-label" for="form2Example3">Repeat password*</label>
                                </div>
                            </div>
                        </div>
                        <div class="row row-cols-2">
                            <div class="col">
                                <div class="form-outline">
                                    <input type="text" class="form-control" id="f-name" name="f-name"/>
                                    <label class="form-label" for="f-name">First Name*</label>
                                </div>
                            </div>
                            <div class="col">
                                <div class="form-outline">
                                    <input type="text" class="form-control" id="l-name" name="l-name"/>
                                    <label class="form-label" for="l-name">Last Name*</label>
                                </div>
                            </div>
                        </div>
                        <div>
                            <input type="text" class="form-control" id="phone-number" name="phone-number"/>
                            <label class="form-label" for="phone-number">Phone number*</label>
                        </div>
                        <div class="text-center mb-4">
                            <button type="button" class="btn btn-primary btn-block w-50" name="command" value="signup">Sign Up</button>
                        </div>
                        <div class="row row-cols-2 justify-content-center text-center">
                            <div class="col">
                                <a href="${pageContext.request.contextPath}/pages/login.jsp">Log In</a>
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
