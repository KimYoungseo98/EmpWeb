<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
<!-- Scriptlet 사용 자바코드를 이용해서 출력, 가장 권장하지 않음 -->
<h1>모든 사원의 수는 <%out.print(request.getAttribute("cnt"));%></h1>

<!-- Expression, 자바스크립트 안에서만 사용 -->
<h2>모든 사원의 수는 <%=request.getAttribute("cnt") %></h2>

<!-- EL(Expression Language)를 사용, 가장 권장함 -->
<h3>모든 사원의 수는 ${cnt}</h3>
</body>
</html>