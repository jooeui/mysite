package com.douzone.mysite.mvc.board;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.douzone.mysite.dao.BoardDao;
import com.douzone.mysite.vo.BoardVo;
import com.douzone.web.mvc.Action;
import com.douzone.web.util.MvcUtils;

public class ListAction implements Action {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String sp = request.getParameter("sp");		// parameter로 넘어온 현재 페이지
		Long startPage = 0L;	// 페이징 시작 번호
		Long endPage = 0L;		// 페이징 마지막 번호
		
		if (sp == null) {
			// 게시판에 처음 들어오면 sp를 parameter로 넘겨주지 않음
			// sp가 null이라면 게시판에 처음 들어왔다는 뜻이므로 현재 페이지를 1로 지정해줌
			sp = "1";
		}
		
		// sp에 정수(1, 2, 3 ..)가 아닌 다른 형(문자형, 실수 등)이 넘어왔는지 검사 
		boolean isNumeric = sp.matches("^\\d+?");
		if (!isNumeric) {
			MvcUtils.redirect(request.getContextPath()+"/board", request, response);
			return;
		}
		request.setAttribute("sp", sp);
		Long selectPage = Long.parseLong(sp);
        
		Long limitCount = 5L;	// 한 페이지 당 출력할 게시글 수
        request.setAttribute("limitCount", limitCount);
        
		BoardDao dao = new BoardDao();
		Long count = dao.countAll();	// 총 게시글 수
		
		Long lastPage = (count-1)/limitCount + 1;	// 마지막 페이지 번호(게시판의 끝)
		if(selectPage > lastPage || selectPage < 1) {	
			// parameter로 넘어온 sp가 게시판 끝 페이지보다 크거나 1보다 작으면
			// 옳지 않은 값이므로 redirect
			MvcUtils.redirect(request.getContextPath()+"/board", request, response);
			return;
		}
		request.setAttribute("count", count);
		request.setAttribute("lastPage", lastPage);
		
		// 페이징 시작 번호, 끝 번호 수정
		if(selectPage < 4 || lastPage <= 5) {
			// 현재 페이지가 4보다 작거나 lastPage가 5 이하라면
			// 기본적으로 1 2 3 4 5 출력
			// 4페이지가 된다면 2 3 4 5 6으로 출력되므로 selectPage < 4 조건을 넣어줌
			startPage = 1L;
			endPage = 5L;
		}
		else if((lastPage-selectPage)> 1) {
			// 현재 페이지가 4 이상인 경우부터 현재 페이지를 페이징 중간 번호로 설정하기 위해
			// (게시판 끝 페이지 - 현재 페이지)가 1보다 크다면 startPage를 sp-2로, endPage를 sp+2로 설정한다.
			// ex) 현재 페이지가 5라면 페이징은 3 4 '5' 6 7 출력
			startPage = selectPage-2;
			endPage = selectPage+2;
		} else {
			// 만약 (게시판 끝 페이지 - 현재 페이지)가 1보다 크지 않다면
			// 페이징 끝 번호를 게시판 끝 페이지로 고정시키고 시작 번호는 -4로 지정
			// ex) 게시판 끝 페이지가 9이고, 현재페이지가 8이면 5 6 7 '8' 9 출력
			//		게시판 끝 페이지보다 큰 번호는 출력하지 않기 위함
			endPage = lastPage;
			startPage = endPage-4;
		}
		request.setAttribute("startPage", startPage);
		request.setAttribute("endPage", endPage);
		
		Long printNoCal = count-(limitCount*(selectPage-1));	// 게시판 번호 출력을 위한 식
		request.setAttribute("printNoCal", printNoCal);
		
		List<BoardVo> printList = dao.findPrintList((selectPage-1)*limitCount, limitCount);		// 출력할 게시글을 불러옴
		request.setAttribute("printList", printList);
		
		MvcUtils.forward("board/list", request, response);
	}

}
