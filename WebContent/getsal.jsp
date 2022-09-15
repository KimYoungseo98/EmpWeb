<%@page import="kr.kosa.emp.EmpDao"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
<h1>데이터 베이스 연결 테스트 5</h1>
	<%
		EmpDao dao = new EmpDao();
		//int result = 0;

		String empidStr = request.getParameter("empid");
		if (empidStr != null) {
			int empid = Integer.parseInt(empidStr);
			int result = dao.getSalaryByEmployeeid(empid);
			out.print("<h1>" + result + "</h1>");
		}
		//EmpDao dao = new EmpDao();
		//int result = dao.getEmpCount();
	%>
</body>
</html>