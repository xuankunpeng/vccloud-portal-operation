package com.vccloud.portal.exception;

public class DuplicateGroupNameException extends ServiceException {

	private static final long serialVersionUID = -421995984251622139L;

	public DuplicateGroupNameException(Exception e) {
		super(e);
	}

	public DuplicateGroupNameException(String msg) {
		super(msg);
	}

	public DuplicateGroupNameException(String msg, Exception e) {
		super(msg, e);
	}

}
