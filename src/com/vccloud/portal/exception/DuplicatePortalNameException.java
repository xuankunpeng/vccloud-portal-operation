package com.vccloud.portal.exception;

public class DuplicatePortalNameException extends ServiceException {

	private static final long serialVersionUID = -6459995235664271051L;

	public DuplicatePortalNameException(Exception e) {
		super(e);
	}

	public DuplicatePortalNameException(String msg) {
		super(msg);
	}

	public DuplicatePortalNameException(String msg, Exception e) {
		super(msg, e);
	}

}
