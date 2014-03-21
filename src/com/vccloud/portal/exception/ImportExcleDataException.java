package com.vccloud.portal.exception;

public class ImportExcleDataException extends ServiceException {

	private static final long serialVersionUID = -3401578873974067592L;
	public ImportExcleDataException(Exception e) {
		super(e);
	}
	public ImportExcleDataException(String msg) {
		super(msg);
	}
	public ImportExcleDataException(String msg, Exception e) {
		super(msg, e);
	}
	

}
