package com.douzone.mysite.controller;

import javax.servlet.ServletContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.douzone.mysite.exception.FileUploadServiceException;
import com.douzone.mysite.security.Auth;
import com.douzone.mysite.service.AdminService;
import com.douzone.mysite.service.FileUploadService;
import com.douzone.mysite.vo.SiteVo;

@Auth(role="ADMIN")
@Controller
@RequestMapping("/admin")
public class AdminController {
	private static final Log LOGGER = LogFactory.getLog(AdminController.class);
	
	@Autowired
	ServletContext servletContext;

	@Autowired
	private AdminService adminService;
	
	@Autowired
	private FileUploadService fileUploadService;
	
	@RequestMapping("")
	public String main(Model model) {
//		SiteVo siteVo = adminService.getSiteInfo();
//		model.addAttribute("siteVo", siteVo);
		return "admin/main";
	}
	
	@RequestMapping(value="/main/update", method=RequestMethod.POST)
	public String update(
			@RequestParam(value="file") MultipartFile multipartFile,
			SiteVo siteVo) {
//		System.out.println(siteVo);
//		adminService.updateSiteInfo(siteVo, multipartFile);
//		SiteVo updateSiteVo = adminService.getSiteInfo();
//		servletContext.setAttribute("siteVo", updateSiteVo);
		// 업로드 이 후에 전역범위에 담아서 뿌려주면 될까요?
//		adminService.updateSiteInfo(siteVo, multipartFile);
		try {
			String profile = fileUploadService.uploadFile(multipartFile);
			siteVo.setProfile(profile);
		} catch(FileUploadServiceException e) {
			LOGGER.info("Admin Main Update:" + e);
		}
//		adminService.updateSite(siteVo, multipartFile);
		adminService.updateSite(siteVo);
		servletContext.setAttribute("siteVo", siteVo);
		
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
