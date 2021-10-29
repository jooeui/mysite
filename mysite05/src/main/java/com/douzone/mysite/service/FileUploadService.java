package com.douzone.mysite.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Calendar;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.douzone.mysite.exception.FileUploadServiceException;

@Service
public class FileUploadService {
	private static String SAVE_PATH = "/upload-mysite";
	private static String URL_BASE = "/upload/images";
	
	public String uploadFile(MultipartFile multipartFile) {
		try {
			File uploadDirectory = new File(SAVE_PATH);
			if(!uploadDirectory.exists()) {
				uploadDirectory.mkdir();
			}
			
			if(multipartFile.isEmpty()) {
				throw new FileUploadServiceException("File Upload Error: File empty");
			}
			
			String originFilename = multipartFile.getOriginalFilename();	// 파일 이름
			String extName = originFilename.substring(originFilename.lastIndexOf('.')+1);	// 파일 확장자
			String saveFilename = generateSaveFilename(extName);	// 저장 파일 이름
			
			byte[] data = multipartFile.getBytes();
			OutputStream os = new FileOutputStream(SAVE_PATH + "/" + saveFilename);
			os.write(data);
			os.close();
			
			return URL_BASE + "/" + saveFilename;
		} catch (IOException e) {
			throw new FileUploadServiceException("File Upload Error: " + e);
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
