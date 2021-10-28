package com.douzone.mysite.security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebArgumentResolver;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.douzone.mysite.vo.UserVo;

public class AuthUserHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {
	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
			NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
		if(!supportsParameter(parameter)) {
			return WebArgumentResolver.UNRESOLVED;
		}
		
		HttpServletRequest request = (HttpServletRequest)webRequest.getNativeRequest();
		HttpSession session = request.getSession();
		if(session == null) {
			return null;		// 위 조건의 return WebArgumentResolver.UNRESOLVED와 값은 같지만 의미적으로는 차이가 있다!!
		}
		
		return webRequest.getNativeRequest(HttpServletRequest.class).getSession().getAttribute("authUser");
	}

	@Override
	public boolean supportsParameter(MethodParameter parameter) {	// 이 메서드로 파라미터 타입 체크
		AuthUser authUser = parameter.getParameterAnnotation(AuthUser.class);	// Method(AuthUser)의 parameter의 annotation을 가져옴
		
		// @AuthUser가 안 붙어 이씅ㅁ
		if(authUser == null) {
			return false;
		}
		
		if(parameter.getParameterType().equals(UserVo.class) == false) {
			return false;
		}
		
		return true;
	}
}
