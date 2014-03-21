package com.vccloud.portal.exception;

public class RoleNameException extends ServiceException{

	
	private static final long serialVersionUID = 3249137814470327977L;
	public RoleNameException(Exception e) {
		super(e);
	}

	public RoleNameException(String msg) {
		super(msg);
	}

	public RoleNameException(String msg, Exception e) {
		super(msg, e);
	}


}
