package com.zendesk.direction.exception;

/**
 * This exception is thrown datetime is less than current datetime
 * @author swapnil.janorkar
 *
 */
public class PastDateTimeException extends Exception {

	private static final long serialVersionUID = 126987397771094348L;

	private String message = "Cannot generate trasit directions for past date & time";
	
	public PastDateTimeException() {
		super();
	}
	
	public PastDateTimeException(String message) {
		super(message);
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
