package com.douzone.mysite.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.douzone.mysite.repository.GuestbookRepository;
import com.douzone.mysite.vo.GuestbookVo;

@Service
public class GuestbookService {
	@Autowired
	private GuestbookRepository guestbookRepository;

	public List<GuestbookVo> guestbookList() {
		return guestbookRepository.findAll();
	}

	public void write(GuestbookVo vo) {
		guestbookRepository.insert(vo);
	}

	public void delete(Long no, String password) {
		guestbookRepository.delete(no, password);
	}
	
}
