package com.douzone.mysite.mvc.guestbook;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.douzone.mysite.dao.GuestbookDao;
import com.douzone.web.mvc.Action;
import com.douzone.web.util.MvcUtils;

public class DeleteFormAction implements Action {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String no = request.getParameter("no");
		
		boolean isNumeric = no.matches("^\\d+?");
		if(!isNumeric) {
			MvcUtils.redirect(request.getContextPath()+"/guestbook", request, response);
			return;
		}
		
		boolean result = new GuestbookDao().findByPost(Long.parseLong(no));
		if(!result) {
			MvcUtils.redirect(request.getContextPath()+"/guestbook", request, response);
			return;
		}
		request.setAttribute("no", no);
		MvcUtils.forward("guestbook/deleteform", request, response);
	}

}
