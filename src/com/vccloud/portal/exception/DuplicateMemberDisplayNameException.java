package com.vccloud.portal.exception;

public class DuplicateMemberDisplayNameException extends ServiceException {

	private static final long serialVersionUID = 876490788152466120L;

	public DuplicateMemberDisplayNameException(Exception e) {
		super(e);
	}

	public DuplicateMemberDisplayNameException(String msg) {
		super(msg);
	}

	public DuplicateMemberDisplayNameException(String msg, Exception e) {
		super(msg, e);
	}

}
