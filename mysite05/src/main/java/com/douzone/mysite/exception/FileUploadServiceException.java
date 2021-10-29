package com.douzone.mysite.exception;

public class FileUploadServiceException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	public FileUploadServiceException(String message) {
		super(message);
	}
	
	public FileUploadServiceException() {
		super("FileUploadService 예외 발생");
	}
}
