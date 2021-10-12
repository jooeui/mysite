package com.douzone.mysite.mvc.user;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.douzone.mysite.dao.UserDao;
import com.douzone.mysite.vo.UserVo;
import com.douzone.web.mvc.Action;
import com.douzone.web.util.MvcUtils;

public class LoginAction implements Action {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String email = request.getParameter("email");
		String password = request.getParameter("password");
		
		UserVo userVo = new UserDao().findByEmailAndPassword(email, password);
		if(userVo == null) {
			/* 로그인 실패 */
			request.setAttribute("result", "fail");
			MvcUtils.forward("user/loginform", request, response);
			return;
		}
		
		/* 인증처리(세션처리) */
		System.out.println("인증처리(세션처리)");
		MvcUtils.redirect("/mysite02", request, response);
	}

}
