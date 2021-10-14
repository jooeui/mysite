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

public class ReplyAction implements Action {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		UserVo authUser = (UserVo)session.getAttribute("authUser");
		if(authUser == null) {
			MvcUtils.redirect(request.getContextPath() + "/board?a=view&no=" + request.getParameter("no"), request, response);
			return;
		}
		
		String title = request.getParameter("title");
		String content = request.getParameter("content");
		Long userNo = authUser.getNo();
		
		if("".equals(title)) {
			MvcUtils.redirect(request.getContextPath()+"/board?a=writeform&no=" + request.getParameter("no"), request, response);
			return;
		}
		
		// 답글 달 게시글의 group_no, order_no, depth를 불러옴
		Long no = Long.parseLong(request.getParameter("no"));
		BoardVo parentsBoardInfo = new BoardDao().findByParentBoardInfo(no);
		
		Long parentsGroupNo = parentsBoardInfo.getGroupNo();
		Long parentsOrderNo = parentsBoardInfo.getOrderNo();
		Long parentsDepth = parentsBoardInfo.getDepth();
		
		
		// 답글 달 게시글의 정보를 넘겨주어 현재 답글 정보(group_no, order_no, depth)를 결정하고 insert!
		BoardVo boardVo = new BoardVo();
		boardVo.setTitle(title);
		boardVo.setContent(content);
		boardVo.setGroupNo(parentsGroupNo);
		boardVo.setOrderNo(parentsOrderNo);
		boardVo.setDepth(parentsDepth);
		boardVo.setUserNo(userNo);
		
		new BoardDao().orderNoUpdate(boardVo);
		new BoardDao().reply(boardVo);
		
		MvcUtils.redirect(request.getContextPath() + "/board", request, response);
		
		
	}

}
