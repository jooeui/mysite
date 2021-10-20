<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<% pageContext.setAttribute("newline", "\n");	// jstl에서 \n을 쓸 수 없기 때문에 이렇게 사용 %>
<!DOCTYPE html>
<html>
<head>
<title>mysite</title>
<meta http-equiv="content-type" content="text/html; charset=utf-8">
<link href="${pageContext.request.contextPath }/assets/css/guestbook.css" rel="stylesheet" type="text/css">
</head>
<body>
	<div id="container">
		<c:import url="/WEB-INF/views/includes/header.jsp" />
		<div id="content">
			<div id="guestbook">
				<form action="${pageContext.request.contextPath }/guestbook" method="post">
					<input type="hidden" name="a" value="insert">
					<table>
						<tr>
							<td>이름</td><td><input type="text" name="name"></td>
							<td>비밀번호</td><td><input type="password" name="pass"></td>
						</tr>
						<tr>
							<td colspan=4><textarea name="content" id="content"></textarea></td>
						</tr>
						<tr>
							<td colspan=4 align=right><input type="submit" VALUE=" 확인 "></td>
						</tr>
					</table>
				</form>
				<ul>
					<c:forEach items='${printList }' var='vo' varStatus='status'>
						<li>
							<table>
								<tr>
									<td>[${printNoCal-status.index }]</td>
									<td>${vo.name }</td>
									<td>${vo.getRegDate() }</td>
									<td><a href="${pageContext.request.contextPath }/guestbook?a=deleteform&no=${vo.no }">삭제</a></td>
								</tr>
								<tr>
									<td colspan=4>
										${fn:replace(vo.content, newline, "<br/>") }
									</td>
								</tr>
							</table>
						<br>
						</li>
					</c:forEach>
				</ul>
				
				<div class="pager">
					<ul>
						<%-- 이전 페이지 번호로 이동
								만약 현재 페이지가 1이라면 이전 페이지 이동 화살표 안 뜨게 --%>
						<c:choose>
							<c:when test="${sp == 1 }">
								<li>◀</li>
							</c:when>
							<c:otherwise>
								<li><a href="${pageContext.servletContext.contextPath }/guestbook?sp=${sp-1 }">◀</a></li>
							</c:otherwise>
						</c:choose>
						
						<%-- 페이징 시작, 끝 번호 설정 --%>
						<c:set var="pages" value="${(count/limitCount)%1 > 0 ? (count/limitCount)+1 : (count/limitCount) }" />
						<fmt:parseNumber value="${pages }" var="pages" integerOnly="true" />
						
						<%-- 페이징 번호 출력 --%>
						<c:forEach begin="${startPage }" end="${endPage }" var="pager" step="1">
							<c:choose>
								<c:when test="${sp == pager }">
									<li class="selected">${pager }</li>
								</c:when>
								<c:when test="${pager > pages }">
									<li>${pager }</li>
								</c:when>
								<c:otherwise>
									<li><a href="${pageContext.servletContext.contextPath }/guestbook?sp=${pager }">${pager }</a></li>
								</c:otherwise>
							</c:choose>
						</c:forEach>
						
						<%-- 다음 페이지 번호로 이동
								만약 현재 페이지가 마지막 페이지(lastPage)라면 다음 페이지 이동 화살표 안 뜨게 --%>
						<c:choose>
							<c:when test="${sp != pages }">
								<li><a href="${pageContext.servletContext.contextPath }/guestbook?sp=${sp+1 }">▶</a></li>
							</c:when>
							<c:otherwise>
								<li>▶</li>
							</c:otherwise>
						</c:choose>
					</ul>
				</div>
			</div>
		</div>
		<c:import url="/WEB-INF/views/includes/navigation.jsp" />
		<c:import url="/WEB-INF/views/includes/footer.jsp" />
	</div>
</body>
</html>