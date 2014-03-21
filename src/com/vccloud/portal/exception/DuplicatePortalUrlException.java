package com.vccloud.portal.exception;

public class DuplicatePortalUrlException extends ServiceException {

	private static final long serialVersionUID = -4932062964149961374L;

	public DuplicatePortalUrlException(Exception e) {
		super(e);
	}

	public DuplicatePortalUrlException(String msg) {
		super(msg);
	}

	public DuplicatePortalUrlException(String msg, Exception e) {
		super(msg, e);
	}

}
