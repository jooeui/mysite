package com.douzone.mysite.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.douzone.mysite.security.Auth;
import com.douzone.mysite.service.GalleryService;
import com.douzone.mysite.vo.GalleryVo;

@Controller
@RequestMapping("/gallery")
public class GalleryController {
	@Autowired
	private GalleryService galleryService;
	
	@RequestMapping("")
	public String index(Model model) {
		List<GalleryVo> list = galleryService.getImages();
//		System.out.println(list);
		model.addAttribute("list", list);
		return "gallery/index";
	}
	
	@Auth(role="ADMIN")
	@RequestMapping(value="/upload", method=RequestMethod.POST)
	public String upload(
			@RequestParam(value="file") MultipartFile multipartFile,
			@RequestParam(value="comments", required=true, defaultValue="") String comments) {
		galleryService.uploadImage(multipartFile, comments);
		return "redirect:/gallery";
	}
	
	@Auth(role="ADMIN")
	@RequestMapping(value="/delete/{no}", method=RequestMethod.GET)
	public String delete(
			@PathVariable(value="no") Long no) {
		galleryService.deleteImage(no);
		return "redirect:/gallery";
	}
}
