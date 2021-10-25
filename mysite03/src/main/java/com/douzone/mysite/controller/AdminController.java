package com.douzone.mysite.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.douzone.mysite.security.Auth;
import com.douzone.mysite.service.AdminService;
import com.douzone.mysite.vo.SiteVo;

@Auth(role="ADMIN")
@Controller
@RequestMapping("/admin")
public class AdminController {
	@Autowired
	private AdminService adminService;
	
	@RequestMapping("")
	public String main(Model model){
		SiteVo siteVo = adminService.getSiteInfo();
		model.addAttribute("siteVo", siteVo);
		return "admin/main";
	}
	
	@RequestMapping(value="/update", method=RequestMethod.POST)
	public String update(
			@RequestParam(value="file") MultipartFile multipartFile,
			SiteVo siteVo) {
//		System.out.println(siteVo);
		adminService.updateSiteInfo(siteVo, multipartFile);
		return "redirect:/admin";
	}
	
	@RequestMapping("/guestbook")
	public String guestbook(){
		return "admin/guestbook";
	}
	
	@RequestMapping("/board")
	public String board(){
		return "admin/board";
	}
	
	@RequestMapping("/user")
	public String user(){
		return "admin/user";
	}
}
