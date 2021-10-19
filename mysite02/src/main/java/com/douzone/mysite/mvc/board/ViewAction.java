package com.douzone.mysite.mvc.board;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.douzone.mysite.dao.BoardDao;
import com.douzone.mysite.vo.BoardVo;
import com.douzone.web.mvc.Action;
import com.douzone.web.util.MvcUtils;

public class ViewAction implements Action {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Long no = Long.parseLong(request.getParameter("no"));
		
		new BoardDao().hitUpdate(no);
		BoardVo boardVo = new BoardDao().findByTitleAndContent(no);
		if("Y".equals(boardVo.getDeleteFlag())) {
			MvcUtils.redirect(request.getContextPath() + "/board", request, response);
			return;
		}
			
		request.setAttribute("boardVo", boardVo);
		
		MvcUtils.forward("board/view", request, response);
	}

}
