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

public class ModifyFormAction implements Action {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		UserVo authUser = (UserVo)session.getAttribute("authUser");
		if(authUser == null) {
			MvcUtils.redirect(request.getContextPath() + "/board", request, response);
			return;
		}
		
		Long userNo = authUser.getNo();
		Long no = Long.parseLong(request.getParameter("no"));
		BoardVo boardVo = new BoardDao().findByPost(userNo, no);
		
		if(userNo != boardVo.getUserNo() || "Y".equals(boardVo.getDeleteFlag())) {
			MvcUtils.redirect(request.getContextPath() + "/board", request, response);
			return;
		}
		
		request.setAttribute("boardVo", boardVo);
		MvcUtils.forward("/board/modify", request, response);
	}

}
