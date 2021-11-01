package com.douzone.mysite.controller.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.douzone.mysite.dto.JsonResult;
import com.douzone.mysite.service.UserService;
import com.douzone.mysite.vo.UserVo;

// RestController를 사용하면 핸들러에 ResponseBody를 쓰지 않아도 됨!!
@RestController("userApiController")
@RequestMapping("/user/api")
public class UserController {
	@Autowired
	private UserService userService;
	
//	@ResponseBody
//	@RequestMapping("/checkemail")
	@GetMapping("/checkemail")
	public JsonResult checkemail(
			@RequestParam(value="email", required=true, defaultValue="") String email) {
//		Map<String, Object> map = new HashMap<>();
//		map.put("result", userVo != null);
//		map.put("data", userVo != null);
//		map.put("message", null);
//		return map;
		
		
		UserVo userVo = userService.getUser(email);
		return JsonResult.success(userVo != null);
		
		
		
	}
}
