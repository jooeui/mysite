package com.douzone.mysite.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.douzone.mysite.exception.GalleryServiceException;
import com.douzone.mysite.repository.AdminRepository;
import com.douzone.mysite.vo.SiteVo;

@Service
public class AdminService {
	private static String SAVE_PATH = "/upload-mysite/profile";
	private static String URL_BASE = "/mysite/images";
	
	@Autowired
	private AdminRepository adminRepository;

	public SiteVo getSiteInfo() {
		return adminRepository.getSiteInfo();
	}

	public void updateSiteInfo(SiteVo siteVo, MultipartFile multipartFile) {
		try {
			File uploadDirectory = new File(SAVE_PATH);
			if(!uploadDirectory.exists()) {
				uploadDirectory.mkdir();
			}
			
			if(!multipartFile.isEmpty()) {
				String originFilename = multipartFile.getOriginalFilename();	// 파일 이름
				String extName = originFilename.substring(originFilename.lastIndexOf('.')+1);	// 파일 확장자
				String saveFilename = generateSaveFilename(extName);	// 저장 파일 이름
				
				byte[] data = multipartFile.getBytes();
				OutputStream os = new FileOutputStream(SAVE_PATH + "/" + saveFilename);
				os.write(data);
				os.close();
				
				siteVo.setProfile(URL_BASE + "/" + saveFilename);
			}
			
//			System.out.println("수정값@@@@@@@@@@@@@@@@@@@@@@@@\n" + siteVo);
			adminRepository.updateSiteInfo(siteVo);
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
