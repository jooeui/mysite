package com.douzone.mysite.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.douzone.mysite.repository.GalleryRepository;
import com.douzone.mysite.vo.GalleryVo;

@Service
public class GalleryService {
	@Autowired
	private GalleryRepository galleryRepository;

	public List<GalleryVo> getImages() {
		return galleryRepository.findAll();
	}

	public void deleteImage(Long no) {
		galleryRepository.delete(no);
	}

	public void uploadImage(GalleryVo galleryVo) {
		galleryRepository.insert(galleryVo);
	}
}
