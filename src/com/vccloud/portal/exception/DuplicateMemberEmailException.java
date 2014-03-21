package com.vccloud.portal.exception;

public class DuplicateMemberEmailException extends ServiceException {

	private static final long serialVersionUID = -3065401977938180478L;

	public DuplicateMemberEmailException(Exception e) {
		super(e);
	}

	public DuplicateMemberEmailException(String msg) {
		super(msg);
	}

	public DuplicateMemberEmailException(String msg, Exception e) {
		super(msg, e);
	}

}
