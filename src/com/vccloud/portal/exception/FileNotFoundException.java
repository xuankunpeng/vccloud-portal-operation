package com.vccloud.portal.exception;

public class FileNotFoundException extends ServiceException{

	private static final long serialVersionUID = -4087655833448907924L;
	public FileNotFoundException(Exception e) {
		super(e);
	}

	public FileNotFoundException(String msg) {
		super(msg);
	}

	public FileNotFoundException(String msg, Exception e) {
		super(msg, e);
	}
}
