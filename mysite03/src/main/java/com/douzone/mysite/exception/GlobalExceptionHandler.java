package com.douzone.mysite.exception;

import java.io.PrintWriter;
import java.io.StringWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
	
	@ExceptionHandler(Exception.class)
	public void HandlerException(
		HttpServletRequest request,
		HttpServletResponse response,
		Exception e) throws Exception {
		// 1. 로깅
		StringWriter errors = new StringWriter();	// 자신의 버퍼에다가 기록
		e.printStackTrace(new PrintWriter(errors));
		// LOGGER.error(errors.toString());
		
		// 2. 요청 구분
		
		// 3. 사과 페이지(정상 종료)
		request.setAttribute("exception", errors.toString());
		request
			.getRequestDispatcher("/WEB-INF/views/error/exception.jsp")
			.forward(request, response);
	}
}