package com.douzone.mysite.controller;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.douzone.mysite.security.Auth;
import com.douzone.mysite.security.AuthUser;
import com.douzone.mysite.service.BoardService;
import com.douzone.mysite.vo.BoardVo;
import com.douzone.mysite.vo.UserVo;

@Controller
@RequestMapping("/board")
public class BoardController {
	@Autowired
	private BoardService boardService;
	
	/* 게시판 리스트 */ 
	@RequestMapping("")
	public String index(
			@RequestParam(value="cp", required=true, defaultValue="1") Long currentPage,
			@RequestParam(value="st", required=true, defaultValue="") String searchType,
			@RequestParam(value="kwd", required=true, defaultValue="") String keyword,
			Model model) {
		Map<String, Object> map= boardService.getBoardList(currentPage, searchType, keyword);
		model.addAttribute("map", map);
		return "board/index";
	}
	
	/* 게시글 보기 */
	@RequestMapping(value="/view/{no}", method=RequestMethod.GET)
	public String view(
			@PathVariable("no") Long no,
			@RequestParam(value="cp", required=true, defaultValue="1") Long currentPage,
			@CookieValue(value="viewedPosts", required=false) Cookie cookie,
			HttpServletResponse response,
			Model model) {
		BoardVo boardVo = boardService.getPost(no);
		
		/* 쿠키 */
		Calendar calendar = Calendar.getInstance();		// 오늘 날짜를 불러오기 위해 calendar 사용
		String date = new SimpleDateFormat("yyMMddHHmm").format(calendar.getTime());	// 날짜 형식을 yyMMddHHmm으로 포맷하고 오늘 날짜를 불러옴
		String cookieValue = "_" + date + ":" + no + "_";	// 쿠키에 추가할 값(ex. _2110232345:30_)
		// System.out.println(cookie);
		
		if(cookie == null) {
			// viewdPosts 이름을 가진 쿠키가 없다면
			boardService.hitUpdate(no);		// 조회수 증가
			cookie = new Cookie("viewedPosts", cookieValue);	// viewedPosts 이름의 쿠키를 생성하고 값을 지정
			// cookie.setPath(request.getContextPath()+"/board/view");	// /mysite03/board/view로 쿠키 PATH 설정(기본)
			cookie.setMaxAge(24 * 60 * 60);		// 쿠키 유지 시간(단위: 초)
			response.addCookie(cookie);		// 생성한 쿠키를 클라이언트에게 전달
		} else {
			// viewdPosts 이름을 가진 쿠키가 있다면
			String value = cookie.getValue();	// 쿠키의 값을 가져옴	
			if(value.indexOf(":" + no + "_") < 0) {
				// 쿠키 값에서 해당 게시글 번호가 없다면(결과 -1)
				boardService.hitUpdate(no);		// 조회수 증가
				value += (cookieValue);		// 기존의 쿠키 값에 쿠키 값(_오늘 날짜와 시간:게시글 번호) 추가
				cookie.setValue(value);		// 새로 설정한 쿠키 값을 쿠키 값에 설정
				response.addCookie(cookie);		// 클라이언트에게 쿠키 전달
			}
		}
		
		model.addAttribute("boardVo", boardVo);
		model.addAttribute("cp", currentPage);
		
		return "board/view";
	}
	
//	@Auth
//	@RequestMapping(value = "/write", method=RequestMethod.GET)
//	public String write(@RequestParam(value="no", required=false) Long no, Model model) {
//		if(no != null) {
//			BoardVo parentsBoardInfo = boardService.getParentBoardInfo(no);
//			if(parentsBoardInfo == null) {
//				return "redirect:/board/view/" + no;
//			}
//			model.addAttribute("no", no);
//		}
//		return "board/write";
//	}
	
	/* 게시글 쓰기 */
	@Auth
	@RequestMapping(value={"/write", "/write/{no}"}, method=RequestMethod.GET)
	public String write(
			@PathVariable(value="no", required=false) Long no, 
			@RequestParam(value="cp", required=true, defaultValue="1") Long currentPage,
			Model model) {
		if(no != null) {
//			BoardVo parentsBoardInfo = boardService.getParentBoardInfo(no);
//			if(parentsBoardInfo == null) {
//				return "redirect:/board";
//			}
			model.addAttribute("no", no);
			model.addAttribute("cp", currentPage);
		}
		return "board/write";
	}
	
	@Auth
	@RequestMapping(value={"/write", "/write/{no}"}, method=RequestMethod.POST)
	public String write(
			@AuthUser UserVo authUser, BoardVo boardVo, 
			@RequestParam(value="cp", required=true, defaultValue="1") Long currentPage,
			Model model) {
		// 제목이나 내용을 입력하지 않을 경우 작성 되지 않는다
		if("".equals(boardVo.getTitle())) {
			model.addAttribute("boardVo", boardVo);
			model.addAttribute("result", "emptyTitle");
			return "/board/write";
		} else if ("".equals(boardVo.getContent())) {
			model.addAttribute("boardVo", boardVo);
			model.addAttribute("result", "emptyContent");
			return "/board/write";
		}
		boardVo.setUserNo(authUser.getNo());	// vo에 유저 번호 set
		
		Long postNo = boardService.addPost(boardVo);	// 게시글 작성 후 작성된 게시글의 번호를 받아옴
		
		return "redirect:/board/view/" + postNo + "?cp=" + currentPage;
	}
	
	/* 게시글 수정 */
	@Auth
	@RequestMapping(value="/modify/{no}", method=RequestMethod.GET)
	public String modify(
			@PathVariable("no") Long no,
			@RequestParam(value="cp", required=true, defaultValue="1") Long currentPage,
			@AuthUser UserVo authUser, Model model) {
		BoardVo editPostInfo = boardService.getEditPostInfo(no, authUser.getNo());
//		if(editPostInfo == null) {
//			return "redirect:/board";
//		}
		model.addAttribute(editPostInfo);
		model.addAttribute("cp", currentPage);
		return "board/modify";
	}
	
	@Auth
	@RequestMapping(value="/modify/{no}", method=RequestMethod.POST)
	public String modify(
			@RequestParam(value="cp", required=true, defaultValue="1") Long currentPage,
			@AuthUser UserVo authUser, BoardVo boardVo, Model model) {
		if("".equals(boardVo.getTitle())) {
			model.addAttribute("boardVo", boardVo);
			model.addAttribute("result", "emptyTitle");
			return "/board/modify";
		} else if ("".equals(boardVo.getContent())) {
			model.addAttribute("boardVo", boardVo);
			model.addAttribute("result", "emptyContent");
			return "/board/modify";
		}
		boardVo.setUserNo(authUser.getNo());
		
		boardService.editPost(boardVo);
		
		return "redirect:/board/view/" + boardVo.getNo() + "?cp=" + currentPage;
	}
	
	/* 글 삭제 */
//	@Auth
//	@RequestMapping(value="/delete/{no}", method=RequestMethod.GET)
//	public String delete(
//			@PathVariable("no") Long no, 
//			@RequestParam(value="cp", required=true, defaultValue="1") Long currentPage,
//			Model model) {
//		model.addAttribute("no", no);
//		return "/board/delete";
//	}
	
	@Auth
//	@RequestMapping(value="/delete/{no}", method=RequestMethod.POST)
	@RequestMapping(value="/delete/{no}", method=RequestMethod.GET)
	public String delete(
			@PathVariable("no") Long no,
			@RequestParam(value="cp", required=true, defaultValue="1") Long currentPage,
//			@RequestParam(value="password", required=true, defaultValue="") String password,
			@AuthUser UserVo authUser) {
//		boardService.deletePost(no, authUser.getNo(), password);
		boardService.deletePost(no, authUser.getNo());
		
		return "redirect:/board?cp=" + currentPage;
	}
}
