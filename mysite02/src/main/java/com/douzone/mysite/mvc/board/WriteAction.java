package com.douzone.mysite.mvc.board;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.douzone.mysite.dao.BoardDao;
import com.douzone.mysite.vo.BoardVo;
import com.douzone.mysite.vo.UserVo;
import com.douzone.web.mvc.Action;
import com.douzone.web.util.MvcUtils;

public class WriteAction implements Action {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		UserVo authUser = (UserVo)session.getAttribute("authUser");
		if(authUser == null) {
			MvcUtils.redirect(request.getContextPath() + "/board", request, response);
			return;
		}
		
		String title = request.getParameter("title");
		String content = request.getParameter("content");
		Long userNo = authUser.getNo();
		
		if("".equals(title)) {
			MvcUtils.redirect(request.getContextPath()+"/board?a=writeform", request, response);
			return;
		}
		
		BoardVo vo = new BoardVo();
		vo.setTitle(title);
		vo.setContent(content);
		vo.setUserNo(userNo);
		
		Long getNo = new BoardDao().write(vo);
		if(getNo < 1) {
			MvcUtils.redirect(request.getContextPath()+"/board", request, response);
			return;
		}
		
		MvcUtils.redirect(request.getContextPath() + "/board?a=view&no=" + getNo, request, response);
		
	}

}
