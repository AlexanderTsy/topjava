<%--
  Created by IntelliJ IDEA.
  User: shuri
  Date: 16.06.2019
  Time: 17:57
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%--@elvariable id="mealsList" type="java.util.List<ru.javawebinar.topjava.model.MealTo>"--%>
<!doctype html>
<html>
<head>
    <title>Meals</title>
    <style>
        .excess-true {
            color:red;
        }
        .excess-false {
            color:green;
        }
    </style>
</head>
<body>
    <h3><a href="index.html">Home</a></h3>
    <hr>
    <h2>Meals</h2>
    <table class="table">
        <th>Дата и время</th>
        <th>Описание</th>
        <th>Калории</th>
        <c:forEach var="meal" items="${mealsList}">
            <tr class="excess-${meal.excess}">
<%--                <fmt:parseDate value="${ cleanedDateTime }" pattern="yyyy-MM-dd'T'HH:mm" var="parsedDateTime" type="both" />--%>
<%--                <fmt:formatDate pattern="dd.MM.yyyy HH:mm" value="${ parsedDateTime }" />--%>
                <td>${meal.dateTime.format( DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm"))}</td>
                <td>${meal.description}</td>
                <td>${meal.calories}</td>
                <td><a href="meal?id=${meal.id}">edit</a> <a href="meals?action=delete&id=${meal.id}">delete</a></td>
            </tr>
        </c:forEach>
    </table>
</body>
</html>
