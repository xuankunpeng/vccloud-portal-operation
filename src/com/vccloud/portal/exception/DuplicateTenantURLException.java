package com.vccloud.portal.exception;

public class DuplicateTenantURLException extends ServiceException {

	private static final long serialVersionUID = 3979662916033888186L;

	public DuplicateTenantURLException(Exception e) {
		super(e);
	}

	public DuplicateTenantURLException(String msg) {
		super(msg);
	}

	public DuplicateTenantURLException(String msg, Exception e) {
		super(msg, e);
	}

}
