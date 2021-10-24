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
					<select name="st" >
						<option value="title" >제목</option>
						<option value="content">내용</option>
						<option value="writer">글쓴이</option>
					</select>
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
					
					<c:forEach items='${map.printList }' var='vo' varStatus='status'>
						<tr>
							<td>${map.count-(map.limitCount*(map.currentPage-1)) - status.index }</td>
							<c:choose>
								<c:when test="${vo.deleteFlag eq 'N' }">
									<td style="text-align: left; padding-left: ${20*vo.depth}px">
									<c:if test="${vo.depth > 0 }">
										<a href="${pageContext.servletContext.contextPath }/board/delete/${vo.no}">
											<img src="${pageContext.servletContext.contextPath }/assets/images/reply.png">
										</a>
									</c:if>
									<a href="${pageContext.servletContext.contextPath }/board/view/${vo.no}">
										${vo.title }
									</a>
									</td>
									<td>${vo.userName }</td>
									<td>${vo.hit }</td>
									<td>${vo.regDate }</td>
									<td>
										<c:if test="${authUser.no == vo.userNo }" >
											<a href="${pageContext.servletContext.contextPath }/board/delete/${vo.no}" class="del"></a>
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
							<c:when test="${map.currentPage == 1 }">
								<li>◀</li>
							</c:when>
							<c:otherwise>
								<li><a href="${pageContext.servletContext.contextPath }/board?cp=${map.currentPage-1 }&st=${map.st}&kwd=${map.kwd}">◀</a></li>
							</c:otherwise>
						</c:choose>
						
						<%-- 페이징 번호 출력 --%>
						<c:forEach begin="${map.startPage }" end="${map.endPage }" var="pager" step="1">
							<c:choose>
								<c:when test="${map.currentPage == pager }">
									<li class="selected">${pager }</li>
								</c:when>
								<c:when test="${pager > map.lastPage }">
									<li>${pager }</li>
								</c:when>
								<c:otherwise>
									<li><a href="${pageContext.servletContext.contextPath }/board?cp=${pager }&st=${map.st}&kwd=${map.kwd}">${pager }</a></li>
								</c:otherwise>
							</c:choose>
						</c:forEach>
						
						<%-- 다음 페이지 번호로 이동
								만약 현재 페이지가 마지막 페이지(lastPage)라면 다음 페이지 이동 화살표 안 뜨게 --%>
						<c:choose>
							<c:when test="${map.currentPage != map.lastPage }">
								<li><a href="${pageContext.servletContext.contextPath }/board?cp=${map.currentPage+1 }&st=${map.st}&kwd=${map.kwd}">▶</a></li>
							</c:when>
							<c:otherwise>
								<li>▶</li>
							</c:otherwise>
						</c:choose>
					</ul>
				</div>					
				<!-- pager 추가 -->
				
				<div class="bottom">
					<a href="${pageContext.servletContext.contextPath }/board/write" id="new-book">글쓰기</a>
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