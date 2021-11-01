package com.douzone.mysite.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.douzone.mysite.service.GuestbookService;
import com.douzone.mysite.vo.GuestbookVo;

@Controller
@RequestMapping("/guestbook")
public class GuestbookController {
	@Autowired
	private GuestbookService guestbookService;
	
	@RequestMapping("")
	public String index(Model model) {
		List<GuestbookVo> list = guestbookService.guestbookList();
		model.addAttribute("list", list);
		return "/guestbook/index";
	}
	
	@RequestMapping(value="/insert", method=RequestMethod.POST)
	public String add(GuestbookVo vo) {
		guestbookService.write(vo);
		return "redirect:/guestbook";
	}
	
	@RequestMapping(value="/delete/{no}", method=RequestMethod.GET)
	public String deleteForm(@PathVariable("no") Long no, Model model) {
		model.addAttribute("no", no);
		return "/guestbook/delete";
	}
	
	@RequestMapping(value="/delete/{no}", method=RequestMethod.POST)
	public String delete(GuestbookVo vo) {
		guestbookService.delete(vo);
		return "redirect:/guestbook";
	}
}
