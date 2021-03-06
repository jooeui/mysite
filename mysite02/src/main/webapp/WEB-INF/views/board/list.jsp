<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title>mysite</title>
<meta http-equiv="content-type" content="text/html; charset=utf-8">
<link href="${pageContext.servletContext.contextPath }/assets/css/board.css" rel="stylesheet" type="text/css">
</head>
<body>
	<div id="container">
		<c:import url="/WEB-INF/views/includes/header.jsp"/>
		<div id="content">
			<div id="board">
				<form id="search_form" action="${pageContext.servletContext.contextPath }/board" method="post">
					<input type="hidden" name="a" value="search">
					<input type="text" id="kwd" name="kwd" value="">
					<input type="submit" value="찾기">
				</form>
				<table class="tbl-ex">
					<tr>
						<th>번호</th>
						<th>제목</th>
						<th>글쓴이</th>
						<th>조회수</th>
						<th>작성일</th>
						<th>&nbsp;</th>
					</tr>
					
					<c:forEach items='${printList }' var='vo' varStatus='status'>
						<tr>
							<td>${printNoCal - status.index }</td>
							<c:choose>
								<c:when test="${vo.deleteFlag eq 'N' }">
									<td style="text-align: left; padding-left: ${20*vo.depth}px">
									<c:if test="${vo.depth > 0 }">
										<a href="${pageContext.servletContext.contextPath }/board?a=delete&no=${vo.no}">
											<img src="${pageContext.servletContext.contextPath }/assets/images/reply.png">
										</a>
									</c:if>
									<a href="${pageContext.servletContext.contextPath }/board?a=view&no=${vo.no}">
										${vo.title }
									</a>
									</td>
									<td>${vo.writer }</td>
									<td>${vo.hit }</td>
									<td>${vo.regDate }</td>
									<td>
										<c:if test="${authUser.no eq vo.userNo }" >
											<a href="${pageContext.servletContext.contextPath }/board?a=deleteform&no=${vo.no}" class="del">
												<img src="${pageContext.servletContext.contextPath }/assets/images/recycle.png">
											</a>
										</c:if>
									</td>
								</c:when>
								<c:otherwise>
									<td style="text-align: left; padding-left: ${20*vo.depth}px">
										<c:if test="${vo.depth > 0 }">
											<img src="${pageContext.servletContext.contextPath }/assets/images/reply.png">
										</c:if>
										삭제된 게시글입니다.
									</td>
									<td>-</td>
									<td>-</td>
									<td>-</td>
									<td></td>
								</c:otherwise>
							</c:choose>
						</tr>
					</c:forEach>
				</table>
				
				<!-- pager 추가 -->
				<div class="pager">
					<ul>
						<%-- 이전 페이지 번호로 이동
								만약 현재 페이지가 1이라면 이전 페이지 이동 화살표 안 뜨게 --%>
						<c:choose>
							<c:when test="${sp == 1 }">
								<li>◀</li>
							</c:when>
							<c:otherwise>
								<li><a href="${pageContext.servletContext.contextPath }/board?sp=${sp-1 }">◀</a></li>
							</c:otherwise>
						</c:choose>
						
						<%-- 페이징 번호 출력 --%>
						<c:forEach begin="${startPage }" end="${endPage }" var="pager" step="1">
							<c:choose>
								<c:when test="${sp == pager }">
									<li class="selected">${pager }</li>
								</c:when>
								<c:when test="${pager > lastPage }">
									<li>${pager }</li>
								</c:when>
								<c:otherwise>
									<li><a href="${pageContext.servletContext.contextPath }/board?sp=${pager }">${pager }</a></li>
								</c:otherwise>
							</c:choose>
						</c:forEach>
						
						<%-- 다음 페이지 번호로 이동
								만약 현재 페이지가 마지막 페이지(lastPage)라면 다음 페이지 이동 화살표 안 뜨게 --%>
						<c:choose>
							<c:when test="${sp != lastPage }">
								<li><a href="${pageContext.servletContext.contextPath }/board?sp=${sp+1 }">▶</a></li>
							</c:when>
							<c:otherwise>
								<li>▶</li>
							</c:otherwise>
						</c:choose>
					</ul>
				</div>					
				<!-- pager 추가 -->
				
				<div class="bottom">
					<c:choose>
						<c:when test="${empty authUser }">
							<%-- 
								<a style="border:1px solid #aaa; color: #aaa; background-color: #ccc; cursor: default" id="new-book">글쓰기</a>
							--%>
							<a href="${pageContext.servletContext.contextPath }/user?a=loginform" target="_blank" id="new-book">글쓰기</a>
						</c:when>
						<c:otherwise>
							<a href="${pageContext.servletContext.contextPath }/board?a=writeform" id="new-book">글쓰기</a>
						</c:otherwise>
					</c:choose>
				</div>				
			</div>
		</div>
		<c:import url="/WEB-INF/views/includes/navigation.jsp">
			<c:param name="menu" value="board"/>
		</c:import>
		<c:import url="/WEB-INF/views/includes/footer.jsp"/>
	</div>
</body>
</html>