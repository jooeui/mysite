package com.douzone.mysite.mvc.guestbook;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.douzone.mysite.dao.GuestbookDao;
import com.douzone.mysite.vo.GuestbookVo;
import com.douzone.web.mvc.Action;
import com.douzone.web.util.MvcUtils;

public class ListAction implements Action {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String sp = request.getParameter("sp");
		Long startPage = 0L;
		Long endPage = 0L;
		
		if (sp == null) {
			sp = "1";
		}
		
		boolean isNumeric = sp.matches("^\\d+?");
		if (!isNumeric) {
			MvcUtils.redirect(request.getContextPath()+"/board", request, response);
			return;
		}
		request.setAttribute("sp", sp);
		Long selectPage = Long.parseLong(sp);
        
		Long limitCount = 4L;
        request.setAttribute("limitCount", limitCount);
        
        GuestbookDao dao = new GuestbookDao();
		Long count = dao.countAll();
		
		Long lastPage = (count-1)/limitCount + 1;
		if(selectPage > lastPage || selectPage < 1) {	
			MvcUtils.redirect(request.getContextPath()+"/board", request, response);
			return;
		}
		request.setAttribute("count", count);
		request.setAttribute("lastPage", lastPage);
		
		if(selectPage < 4 || lastPage <= 5) {
			startPage = 1L;
			endPage = 5L;
		}
		else if((lastPage-selectPage)> 1) {
			startPage = selectPage-2;
			endPage = selectPage+2;
		} else {
			endPage = lastPage;
			startPage = endPage-4;
		}
		request.setAttribute("startPage", startPage);
		request.setAttribute("endPage", endPage);
		
		Long printNoCal = count-(limitCount*(selectPage-1));
		request.setAttribute("printNoCal", printNoCal);
		
		List<GuestbookVo> printList = dao.printList((selectPage-1)*limitCount, limitCount);
		request.setAttribute("printList", printList);

		MvcUtils.forward("guestbook/list", request, response);
	}

}
