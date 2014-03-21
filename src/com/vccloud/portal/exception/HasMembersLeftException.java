package com.vccloud.portal.exception;

public class HasMembersLeftException extends ServiceException {

	private static final long serialVersionUID = -6772772676385904447L;

	public HasMembersLeftException(Exception e) {
		super(e);
	}

	public HasMembersLeftException(String msg) {
		super(msg);
	}

	public HasMembersLeftException(String msg, Exception e) {
		super(msg, e);
	}

}
