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

public class WriteFormAction implements Action {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		UserVo authUser = (UserVo)session.getAttribute("authUser");
		String no = request.getParameter("no");
		if(authUser == null) {
			if(no == null) {
				MvcUtils.redirect(request.getContextPath() + "/board", request, response);
			} else {
				MvcUtils.redirect(request.getContextPath() + "/board?a=view&no=" + request.getParameter("no"), request, response);
			}
			return;
		}
		
		if(no != null) {
			boolean isNumeric = no.matches("^\\d+?");
			if (!isNumeric) {
				MvcUtils.redirect(request.getContextPath()+"/board", request, response);
				return;
			}
			BoardVo parentsBoardInfo = new BoardDao().findByParentBoardInfo(Long.parseLong(no));
			if(parentsBoardInfo == null) {
				MvcUtils.redirect(request.getContextPath() + "/board", request, response);
				return;
			}
		}
		
		MvcUtils.forward("board/write", request, response);
	}

}
