package com.douzone.mysite.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.douzone.mysite.security.Auth;

@Controller
public class MainController {
//	@Autowired
//	private AdminService adminService;
	
	@Auth
	@RequestMapping({"", "/main"})
	public String index(Model model) {
		// servlet context에 담아서 뿌려주자
		// 메인 페이지 처음 갔을 때 불러옴
		// 이것도 Interceptor 만들어서...?
//		SiteVo site = servletContext.getAttribute("site");
//		if(site == null) {
//			SiteVo siteVo = adminService.getSiteInfo();
//			servletContext.setAttribute("siteVo", siteVo);
//		}
//		SiteVo siteVo = adminService.getSiteInfo();
//		model.addAttribute("siteVo", siteVo);
		return "main/index";
	}
	
	@ResponseBody
	@RequestMapping("/msg01")
	public String message01() {
		return "안녕";
	}
	
//	@RequestMapping("/msg02")
//	public void message02(HttpServletResponse resp) throws Exception {
//		resp.setContentType("application/json; charset=UTF-8");
//		resp.getWriter().print("{\"message\":\"Hello World\"}");
//	}
	@ResponseBody
	@RequestMapping("/msg02")
//	public Object message02() throws Exception {
	public Map<String, Object> message02() throws Exception {
		// object를 json으로 바꿔주는 애가 없기 때문에 지금은 안 돌아감!!
		Map<String, Object> map = new HashMap<>();
		map.put("message", "HelloWord");
		return map;	
	}
}
