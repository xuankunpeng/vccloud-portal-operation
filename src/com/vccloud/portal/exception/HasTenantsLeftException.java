package com.vccloud.portal.exception;

public class HasTenantsLeftException extends ServiceException {

	private static final long serialVersionUID = 4800536512819745529L;

	public HasTenantsLeftException(Exception e) {
		super(e);
	}

	public HasTenantsLeftException(String msg) {
		super(msg);
	}

	public HasTenantsLeftException(String msg, Exception e) {
		super(msg, e);
	}

}
