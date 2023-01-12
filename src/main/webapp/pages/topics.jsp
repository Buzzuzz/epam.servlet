<%@include file="../components/metadata.jspf" %>
<html lang="${sessionScope.locale}">
<%@include file="../components/head.jspf" %>
<body>
<%@include file="../components/menu.jspf" %>
<div class="container min-vh-100">
    <div class="row">
        <div class="col">
            <div class="card shadow text-center">
                <div class="card-body">
                    <table class="table table-striped table-bordered border-dark m-0">
                        <thead>
                        <tr>
                            <th scope="col">#</th>
                            <th scope="col">
                                <fmt:message key="topic"/>
                            </th>
                            <th scope="col"><fmt:message key="description"/></th>
                            <th scope="col">
                                <fmt:message key="actions"/>
                            </th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach var="topic" items="${requestScope.topics}">
                            <form action="${pageContext.request.contextPath}/controller" method="post">
                                <tr>
                                    <input hidden name="t_id" value="${topic.topicId}" aria-label="topicId"/>
                                    <th scope="row">
                                            ${topic.topicId}
                                    </th>
                                    <td>
                                        <input class="form-control" type="text" name="topicName"
                                               value="${topic.topicName}" required
                                               aria-label="topicName"/>
                                    </td>
                                    <td style="width:50%">
                                        <textarea class="form-control" aria-label="topicDescription"
                                                  style="height: 20px" required
                                                  name="topicDescription">${topic.topicDescription}</textarea>
                                    </td>
                                    <td>
                                        <div class="row">
                                            <div class="col">
                                                <button type="submit" name="command" value="update-topic"
                                                        class="btn btn-info w-100">
                                                    <fmt:message key="save"/>
                                                    <i class="fa-solid fa-floppy-disk"></i>
                                                </button>
                                            </div>
                                            <div class="col">
                                                <button type="submit" name="command" value="delete-topic"
                                                        class="btn btn-danger w-100">
                                                    <fmt:message key="delete"/>
                                                    <i class="fa-solid fa-trash"></i>
                                                </button>
                                            </div>
                                        </div>
                                    </td>
                                </tr>
                            </form>
                        </c:forEach>
                        <form action="${pageContext.request.contextPath}/controller" method="post">
                            <tr>
                                <th scope="row">
                                    #
                                </th>
                                <td>
                                    <input class="form-control" type="text" name="topicName"
                                           aria-label="topicName" required/>
                                </td>
                                <td style="width:50%">
                                        <textarea class="form-control" aria-label="topicDescription"
                                                  style="height: 20px" required
                                                  name="topicDescription"></textarea>
                                </td>
                                <td>
                                    <button class="btn btn-success w-100" type="submit" name="command"
                                            value="add-topic">
                                        <fmt:message key="add_topic"/>
                                        <i class="fa-solid fa-plus"></i>
                                    </button>
                                </td>
                            </tr>
                        </form>
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
