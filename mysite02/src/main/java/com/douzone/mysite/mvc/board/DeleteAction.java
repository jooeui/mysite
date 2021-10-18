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

public class DeleteAction implements Action {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		UserVo authUser = (UserVo)session.getAttribute("authUser");
		if(authUser == null) {
			MvcUtils.redirect(request.getContextPath() + "/board", request, response);
			return;
		}
		
		Long no = Long.parseLong(request.getParameter("no"));
		Long userNo = authUser.getNo();
		String pw = request.getParameter("password");
		
		BoardVo vo = new BoardVo();
		
		vo.setNo(no);
		vo.setUserNo(userNo);
		
		new BoardDao().delete(vo, pw);
		
		MvcUtils.redirect(request.getContextPath() + "/board", request, response);
	}

}
