<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	<c:set var="booklist">
		<booklist> 
			<book> 
				<name>사피엔스</name> 
				<author>유발하라리</author> 
				<price>19800</price> 
			</book> 
			<book> 
				<name>총, 균, 쇠</name> 
				<author>제러드 다이아몬드</author> 
				<price>25200</price> 
			</book> 
		</booklist>
	</c:set>
	<x:parse xml="${booklist}" var="blist" />
	
	<h2>파싱1</h2>
	제록: <x:out select="$blist/booklist/book[1]/name" escapeXml="true" />
	저자: <x:out select="$blist/booklist/book[1]/author" escapeXml="true" />
	가격: <x:out select="$blist/booklist/book[1]/price" escapeXml="true" />
	
	
	<h2>파싱2</h2>
	<table border="1">
		<tr>
			<th>제목</th>
			<th>저자</th>
			<th>가격</th>
		</tr>
		<x:forEach select="$blist/booklist/book" var="item">
			<tr>
				<td><x:out select="$item/name" /></td>
				<td><x:out select="$item/author" /></td>
				<td><x:choose>
						<x:when select="$item/price>=20000">
							2만 원 이상<br />
						</x:when>
						<x:otherwise>
							2만 원 미만<br />
						</x:otherwise>
					</x:choose></td>
			</tr>
		</x:forEach>
	</table>
	<h2>파싱3</h2>
	<table border="1">
		<x:forEach select="$blist/booklist/book" var="item">
		<tr>
			<td><x:out select="$item/name" /></td>
			<td><x:out select="$item/author" /></td>
			<td><x:out select="$item/price" /></td>
			<td><x:if select="$item/name='총, 균, 쇠'">구매함</x:if></td>
		</tr>
		</x:forEach>
	</table>
</body>
</html>