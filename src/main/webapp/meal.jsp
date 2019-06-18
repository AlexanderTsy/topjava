<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<%--@elvariable id="meal" type="ru.javawebinar.topjava.model.Meal"--%>
<%--
  Created by IntelliJ IDEA.
  User: shuri
  Date: 17.06.2019
  Time: 21:25
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Add meal</title>
</head>
<body>
    <form action="meals.jsp" method="post" name="formMeal">
        Meal ID : <input type="text" readonly="readonly" name="id"
                         value="<c:out value="${meal.id}" />" /> <br />
<%--        <fmt:parseDate value="${ meal.dateTime }" pattern="yyyy-MM-dd'T'HH:mm" var="parsedDateTime" type="both" />--%>
<%--        Date Time : <input--%>
<%--            type="text" name="dateTime"--%>
<%--            value="<fmt:formatDate pattern="dd.MM.yyyy HH:mm" value="${parsedDateTime}" />" /> <br />--%>
        Description : <input
            type="text" name="description"
            value="<c:out value="${meal.description}" />" /> <br />
        Description : <input
            type="text" name="calories"
            value="<c:out value="${meal.calories}" />" /> <br />
         <input
            type="submit" value="Submit" />
    </form>
</body>
</html>
