package com.vccloud.portal.exception;

public class DuplicateExtensionPrefixException extends ServiceException {

	private static final long serialVersionUID = -2585246466681078932L;

	public DuplicateExtensionPrefixException(Exception e) {
		super(e);
	}

	public DuplicateExtensionPrefixException(String msg) {
		super(msg);
	}

	public DuplicateExtensionPrefixException(String msg, Exception e) {
		super(msg, e);
	}

}
