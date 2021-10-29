<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>mysite</title>
<meta http-equiv="content-type" content="text/html; charset=utf-8">
<link href="${pageContext.request.contextPath }/assets/css/board.css" rel="stylesheet" type="text/css">
</head>
<body>
	<div id="container">
		<c:import url="/WEB-INF/views/includes/header.jsp" />
		<div id="content">
			<div id="board">
				<c:choose>
					<c:when test="${not empty no }">
						<c:set var="writePath" value="/${no }?cp=${cp }" />
					</c:when>
					<c:otherwise>
						<c:set var="writePath" value="" />
					</c:otherwise>
				</c:choose>
				<form class="board-form" method="post" action="${pageContext.servletContext.contextPath }/board/write${writePath }">
					<table class="tbl-ex">
						<tr>
							<th colspan="2">
							<c:choose>
								<c:when test="${empty no }">
									글쓰기
								</c:when>
								<c:otherwise>
									답글쓰기
								</c:otherwise>
							</c:choose>
							</th>
						</tr>
						<tr>
							<td class="label">제목</td>
							<td><input type="text" name="title" value="${boardVo.title }"></td>
						</tr>
						<tr>
							<td class="label">내용</td>
							<td>
								<textarea id="content" name="content">${boardVo.content }</textarea>
							</td>
						</tr>
					</table>
					<c:choose>
						<c:when test='${result == "emptyTitle" }'>
							<p>
								제목을 입력해주세요.
							</p>
						</c:when>
						<c:when test='${result == "emptyContent" }'>
							<p>
								내용을 입력해주세요.
							</p>
						</c:when>
					</c:choose>
					<div class="bottom">
						<c:choose>
							<c:when test="${empty no }">
								<a href="${pageContext.servletContext.contextPath }/board">취소</a>
							</c:when>
							<c:otherwise>
								<a href="${pageContext.servletContext.contextPath }/board/view/${no }?cp=${cp }">취소</a>
							</c:otherwise>
						</c:choose>
						<input type="submit" value="등록">
					</div>
				</form>				
			</div>
		</div>
		<c:import url="/WEB-INF/views/includes/navigation.jsp" />
		<c:import url="/WEB-INF/views/includes/footer.jsp" />
	</div>
</body>
</html>