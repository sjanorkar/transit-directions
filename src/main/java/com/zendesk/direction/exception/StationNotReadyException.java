package com.zendesk.direction.exception;

/**
 * This exception is thrown when mrt station is not ready for travel
 * @author swapnil.janorkar
 *
 */
public class StationNotReadyException extends Exception {

	private static final long serialVersionUID = 126987397771094348L;

	private String message;
	
	public StationNotReadyException() {
		super();
	}
	
	public StationNotReadyException(String message) {
		super(message);
		this.message = message;
	}

	public String getMessage() {
		return message + " is not ready yet";
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
