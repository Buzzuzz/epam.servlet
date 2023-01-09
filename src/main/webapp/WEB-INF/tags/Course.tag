<%@taglib prefix="d" uri="../../WEB-INF/tlds/dateOnly.tld" %>
<%@attribute name="value" type="model.entities.Course" required="true" %>

<tr>
    <th scope="row">${value.c_id}</th>
    <td>${value.name}</td>
    <td>${value.description}</td>
    <td>
        <d:dateOnly date="${value.start_date}"/>
    </td>
    <td>
        <d:dateOnly date="${value.end_date}"/>
    </td>
</tr>