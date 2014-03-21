package com.vccloud.portal.exception;

public class DuplicateMemberExtensionException extends ServiceException {

	private static final long serialVersionUID = -7598889900324481487L;

	public DuplicateMemberExtensionException(Exception e) {
		super(e);
	}

	public DuplicateMemberExtensionException(String msg) {
		super(msg);
	}

	public DuplicateMemberExtensionException(String msg, Exception e) {
		super(msg, e);
	}

}
