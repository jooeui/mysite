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

public class SearchAction implements Action {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String sp = request.getParameter("sp");
		String kwd = request.getParameter("kwd");
		
		if (sp == null) {
			sp = "1";
		}
		if (kwd == null) {
			kwd = "";
		}
        Long selectPage = Long.parseLong(sp);
        Long limitCount = 5L;
        
		BoardDao dao = new BoardDao();
		Long count = dao.searchCount(kwd);
		request.setAttribute("count", count);
		
		List<BoardVo> printList = dao.searchPrintList((selectPage-1)*limitCount, limitCount, kwd);
		request.setAttribute("printList", printList);
		
		MvcUtils.forward("board/list", request, response);
	}

}
