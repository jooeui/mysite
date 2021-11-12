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
	
	public List<GuestbookVo> findAll(Long no) {
		return guestbookRepository.findAllLimit(no);
	}

	public void write(GuestbookVo vo) {
		guestbookRepository.insert(vo);
	}
	
	public boolean delete(Long no, String password) {
		if(guestbookRepository.findByPassword(no, password) == null) {
			return false; 
		}
		
		return guestbookRepository.delete(no, password);
	}
}
