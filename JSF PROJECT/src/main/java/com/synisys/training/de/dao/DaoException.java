package com.synisys.training.de.dao;

public class DaoException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public DaoException(Throwable e) {
		super(e);
	}
	public DaoException(String message, Throwable cause) {
		super(message, cause);
	}
	public DaoException(String message) {
		super(message);
	}
}
