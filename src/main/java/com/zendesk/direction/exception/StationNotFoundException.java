package com.zendesk.direction.exception;

/**
 * This exception is thrown when mrt station is not present
 * @author swapnil.janorkar
 *
 */
public class StationNotFoundException extends Exception {

	private static final long serialVersionUID = 126987397771094348L;

	private String message;
	
	public StationNotFoundException() {
		super();
	}
	
	public StationNotFoundException(String message) {
		super(message);
		this.message = message;
	}

	public String getMessage() {
		return message + " does not exist. Station names are case sensitive";
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
