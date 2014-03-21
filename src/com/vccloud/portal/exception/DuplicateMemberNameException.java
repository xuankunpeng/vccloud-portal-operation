package com.vccloud.portal.exception;

public class DuplicateMemberNameException extends ServiceException {

	private static final long serialVersionUID = 151781917602587269L;

	public DuplicateMemberNameException(Exception e) {
		super(e);
	}

	public DuplicateMemberNameException(String msg) {
		super(msg);
	}

	public DuplicateMemberNameException(String msg, Exception e) {
		super(msg, e);
	}

}
