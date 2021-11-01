package com.douzone.mysite.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.douzone.mysite.service.AdminService;
import com.douzone.mysite.vo.SiteVo;

public class SiteInterceptor extends HandlerInterceptorAdapter {
	@Autowired
	private AdminService adminService;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		SiteVo site = (SiteVo) request.getServletContext().getAttribute("siteVo");
//		System.out.println("********************** SiteInterceptor *******************");
		if(site == null) {
			site = adminService.getSiteInfo();
			request.getServletContext().setAttribute("siteVo", site);
		}
		
//		System.out.println(site);
		
		return true;
	}
	
}
