package com.vccloud.portal.exception;

public class DuplicateTenantNameException extends ServiceException {

	private static final long serialVersionUID = -6477466769038149745L;

	public DuplicateTenantNameException(Exception e) {
		super(e);
	}

	public DuplicateTenantNameException(String msg) {
		super(msg);
	}

	public DuplicateTenantNameException(String msg, Exception e) {
		super(msg, e);
	}

}
