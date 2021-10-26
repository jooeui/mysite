package com.douzone.mysite.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.douzone.mysite.exception.FileUploadServiceException;
import com.douzone.mysite.repository.AdminRepository;
import com.douzone.mysite.vo.SiteVo;

@Service
public class AdminService {
	@Autowired
	private AdminRepository adminRepository;

	public SiteVo getSiteInfo() {
		return adminRepository.getSiteInfo();
	}

	public void updateSite(SiteVo siteVo) {
		adminRepository.updateSite(siteVo);
	}

}