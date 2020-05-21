
<%@ page language="java" contentType="text/html; charset=UTF-8"   pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
    <title>all photos</title>
</head>
<body>
<div align="center">
<h1 align="center" >  Your Photos</h1>
<form action="/deletes/" method="post" >

<table border="2">
<tr>
    <td>Delete</td>
    <td>Photo id</td>
    <td>Photo</td>
</tr>

<c:forEach items="${photos}" var="photos"
    varStatus="ststus">

    <tr>
        <td>
            <input type="checkbox" name="photo"  value="${photos.key}"/>
        </td>
        <td>${photos.key}</td>
        <td>
            <img height="70" width="100" src="/photo/${photos.key}" />
        </td>
    </tr>
    <br>
</c:forEach>
</table>
    <input type="submit" value="Delete photo">
</form>
</div>
</body>
</html>
