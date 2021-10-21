package com.douzone.mysite.mvc.board;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.douzone.mysite.dao.BoardDao;
import com.douzone.mysite.vo.BoardVo;
import com.douzone.web.mvc.Action;
import com.douzone.web.util.MvcUtils;

public class ViewAction implements Action {
	private static final String COOKIE_NAME = "viewCookie";
	
	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String no = request.getParameter("no");
		
		boolean isNumeric = no.matches("^\\d+?");
		if (!isNumeric) {
			MvcUtils.redirect(request.getContextPath()+"/board", request, response);
			return;
		}
		
		BoardVo boardVo = new BoardDao().findByPost(Long.parseLong(no));
		if(boardVo == null) {
			MvcUtils.redirect(request.getContextPath() + "/board", request, response);
			return;
		}
		
		Cookie viewCookie = null;
		String value = "";
		// 쿠키 읽기
		Cookie[] cookies = request.getCookies();
		if(cookies != null && cookies.length > 0) {
			for(Cookie cookie : cookies) {
				if(COOKIE_NAME.equals(cookie.getName())) {
					viewCookie = cookie;
					value = viewCookie.getValue();
				}
			}
		}
		
		Calendar calender = Calendar.getInstance();
		
		String date = new SimpleDateFormat("yyMMddHHmm").format(calender.getTime());
		
		if(viewCookie == null) {
			new BoardDao().hitUpdate(Long.parseLong(no));
			Cookie cookie = new Cookie(COOKIE_NAME, "_" + date + ":" + no + "_");
			cookie.setPath(request.getContextPath());
			cookie.setMaxAge(24 * 60 * 60);
			response.addCookie(cookie);
		} else {
			if(value.indexOf(":" + no + "_") < 0) {
				new BoardDao().hitUpdate(Long.parseLong(no));
				value += ("_" + date + ":" + no + "_");
				viewCookie.setValue(value);
				response.addCookie(viewCookie);
			}
		}
		request.setAttribute("boardVo", boardVo);
		
		MvcUtils.forward("board/view", request, response);
	}

}
