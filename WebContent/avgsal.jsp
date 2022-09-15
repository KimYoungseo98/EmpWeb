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
		//String url = application.getInitParameter("OracleURL");
		//String id = application.getInitParameter("OracleId");
		//String pw = application.getInitParameter("OraclePwd");
		//EmpDao dao = new EmpDao(url, id, pw);
		//EmpDao dao = new EmpDao(application);
		EmpDao dao = new EmpDao();
		int result = 0;

		String deptStr = request.getParameter("deptno");
		if (deptStr != null) {
			int deptno = Integer.parseInt(deptStr);
			double avgsal = dao.getAverageSalaryByDepartment(deptno);
			out.print("<h1>" + avgsal + "</h1>");
		}
		//EmpDao dao = new EmpDao();
		//int result = dao.getEmpCount();
	%>
</body>
</html>