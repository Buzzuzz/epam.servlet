<%@include file="../components/metadata.jspf" %>
<html lang="${sessionScope.locale}">
<%@include file="../components/head.jspf" %>
<body>
<%@include file="../components/menu.jspf" %>
<div class="container min-vh-100">
    <div class="row">
        <div class="col">
            <div class="d-flex justify-content-center">
                <div class="card shadow mb-3" style="width: 75%">
                    <div class="card-body">
                        <form action="${pageContext.request.contextPath}/controller">
                            <input hidden name="page" value="${requestScope.page}">

                            <div class="row">
                                <div class="col-6">
                                    <div class="input-group">
                                    <span class="input-group-text">
                                        <fmt:message key="sort_by"/>
                                    </span>
                                        <select class="form-select" aria-label="sorting" name="sorting">
                                            <option value="t_id" ${requestScope.sorting eq 't_id' ? 'selected="selected"' : ''}>
                                                <fmt:message key="id"/>
                                            </option>
                                            <option value="name" ${requestScope.sorting eq 'name' ? 'selected="selected"' : ''}>
                                                <fmt:message key="topic_name"/>
                                            </option>
                                        </select>
                                    </div>
                                </div>
                                <div class="col">
                                    <input class="form-control" type="number" name="display" min="1"
                                           value="${requestScope.display != null ? requestScope.display : 5}"
                                           oninput="this.value = !!this.value && Math.abs(this.value) >= 1 ? Math.abs(this.value) : 1"/>
                                </div>
                                <div class="col">
                                    <button type="submit" name="command" value="get-all-topics"
                                            class="btn btn-primary w-100">
                                        <fmt:message key="search"/>
                                        <i class="fa-solid fa-magnifying-glass"></i>
                                    </button>
                                </div>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
            <div class="card shadow text-center mb-3">
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

                                <input hidden name="sorting" value="${requestScope.sorting}"/>
                                <input hidden name="page" value="${requestScope.page}">
                                <input hidden name="display" value="${requestScope.display}">

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

                            <input hidden name="sorting" value="${requestScope.sorting}"/>
                            <input hidden name="page" value="${requestScope.page}">
                            <input hidden name="display" value="${requestScope.display}">

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

            <!-- Pagination -->
            <form action="${pageContext.request.contextPath}/controller">

                <input hidden name="sorting" value="${requestScope.sorting}"/>
                <input hidden name="display" value="${requestScope.display}">
                <input hidden name="command" value="get-all-topics"/>

                <nav aria-label="Page navigation example">
                    <ul class="pagination justify-content-center">
                        <li class="page-item">
                            <label for="prev-page" class="page-link">
                                <input id="prev-page" hidden type="submit" name="page" value="1"/>
                                &laquo;
                            </label>
                        </li>
                        <c:forEach var="record" items="${requestScope.records}">
                            <li class="page-item">
                                <label for="page${record}" class="page-link ${record eq requestScope.page ? 'selected' : ''}">
                                    <input id="page${record}" hidden type="submit" name="page" value="${record}"/>
                                        ${record}
                                </label>
                            </li>
                        </c:forEach>
                        <li class="page-item">
                            <label for="next-page" class="page-link">
                                <input id="next-page" hidden type="submit" name="page" value="${fn:length(requestScope.records)}"/>
                                &raquo;
                            </label>
                        </li>
                    </ul>
                </nav>
            </form>
        </div>
    </div>
</div>

<%@include file="../components/footer.jspf" %>
</body>
</html>
