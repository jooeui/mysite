package com.douzone.mysite.service;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.douzone.mysite.repository.BoardRepository;
import com.douzone.mysite.vo.BoardVo;

@Service
public class BoardService {
	private static final int LIMIT_COUNT = 5; 		// 한 페이지 당 출력할 게시글 수
	private static final String COOKIE_NAME = "viewCookie";		// 쿠키 이름
	
	@Autowired
	private BoardRepository boardRepository;

	public Map<String, Object> getBoardList(Long currentPage) {
		Long count = boardRepository.countAll();	// 전체 게시글 수
//		System.out.println(count);
		Long lastPage = (count-1)/LIMIT_COUNT + 1;	// 게시판 끝 번호
		Long startPage = 0L;	// 페이징 시작 번호
		Long endPage = 0L;		// 페이징 끝 번호

		// 페이징 번호 결정
		if((currentPage < 4) || (lastPage <= 5)) {
			startPage = 1L;
			endPage = 5L;
		} else if((lastPage - currentPage) > 1) {
			startPage = currentPage - 2;
			endPage = currentPage + 2;
		} else {
			endPage = lastPage;
			startPage = endPage - 4;
		}
		
		int listLimit = (int)((currentPage-1)*LIMIT_COUNT);
		// 출력할 게시글 리스트
		
		List<BoardVo> printList = boardRepository.findPrintList(listLimit, LIMIT_COUNT);
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("printList", printList);
//		System.out.println("게시글 - " + printList);
		map.put("count", count);
		map.put("limitCount", LIMIT_COUNT);
		map.put("currentPage", currentPage);
		map.put("lastPage", lastPage);
		map.put("startPage", startPage);
		map.put("endPage", endPage);
//		System.out.println("map - " + map);
		return map;
	}

	public BoardVo getPost(Long no) {
		BoardVo boardVo = boardRepository.findByPost(no);
		return boardVo;
	}

	public void hitUpdate(Long no) {
		boardRepository.hitUpdate(no);
	}

	public BoardVo getParentBoardInfo(Long no) {
		return boardRepository.findByParentBoardInfo(no);
	}

	public Long addPost(BoardVo boardVo) {
		if(boardVo.getNo() != null) {
			BoardVo parentsBoardInfo = boardRepository.findByParentBoardInfo(boardVo.getNo());
			boardVo.setGroupNo(parentsBoardInfo.getGroupNo());
			boardVo.setOrderNo(parentsBoardInfo.getOrderNo());
			boardVo.setDepth(parentsBoardInfo.getDepth());
			
			boardRepository.orderNoUpdate(boardVo);
		}
		
		return boardRepository.write(boardVo);
	}
}
