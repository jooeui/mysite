package com.douzone.mysite.exception;

public class GalleryServiceException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	public GalleryServiceException(String message) {
		super(message);
	}
	
	public GalleryServiceException() {
		super("GalleryService 예외 발생");
	}
}
