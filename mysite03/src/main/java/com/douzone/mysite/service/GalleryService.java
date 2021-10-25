package com.douzone.mysite.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.douzone.mysite.exception.GalleryServiceException;
import com.douzone.mysite.repository.GalleryRepository;
import com.douzone.mysite.vo.GalleryVo;

@Service
public class GalleryService {
	private static String SAVE_PATH = "/upload-mysite";
	private static String URL_BASE = "/gallery/images";
	
	@Autowired
	private GalleryRepository galleryRepository;

	public List<GalleryVo> getImages() {
		return galleryRepository.findAll();
	}

	public void deleteImage(Long no) {
		galleryRepository.delete(no);
	}
	
	public void uploadImage(MultipartFile multipartFile, String comments) {
		try {
			File uploadDirectory = new File(SAVE_PATH);
			if(!uploadDirectory.exists()) {
				uploadDirectory.mkdir();
			}
			
			if(multipartFile.isEmpty()) {
				throw new GalleryServiceException("File Upload Error: image empty");
			}
			
			String originFilename = multipartFile.getOriginalFilename();	// 파일 이름
			String extName = originFilename.substring(originFilename.lastIndexOf('.')+1);	// 파일 확장자
			String saveFilename = generateSaveFilename(extName);	// 저장 파일 이름
			
			byte[] data = multipartFile.getBytes();
			OutputStream os = new FileOutputStream(SAVE_PATH + "/" + saveFilename);
			os.write(data);
			os.close();
			
			GalleryVo galleryVo = new GalleryVo();
			galleryVo.setUrl(URL_BASE + "/" + saveFilename);
			galleryVo.setComments(comments);
//			System.out.println("service: " + galleryVo);
			galleryRepository.insert(galleryVo);
		} catch (IOException e) {
			throw new GalleryServiceException("File Upload Error: " + e);
		}
	}
	
	private static String generateSaveFilename(String extName) {
		String filename = "";
		
		Calendar calendar = Calendar.getInstance();
		
		filename += calendar.get(Calendar.YEAR);
		filename += calendar.get(Calendar.MONTH);
		filename += calendar.get(Calendar.DATE);
		filename += calendar.get(Calendar.HOUR);
		filename += calendar.get(Calendar.MINUTE);
		filename += calendar.get(Calendar.SECOND);
		filename += calendar.get(Calendar.MILLISECOND);
		filename += ("." + extName);
		
		return filename;
	}
}
