package com.zendesk.direction.exception;

/**
 * This exception is thrown when mrt station is closed
 * @author swapnil.janorkar
 *
 */
public class StationClosedException extends Exception {

	private static final long serialVersionUID = 126987397771094348L;

	private String message;
	
	public StationClosedException() {
		super();
	}
	
	public StationClosedException(String message) {
		super(message);
		this.message = message;
	}

	public String getMessage() {
		return message + " is closed now";
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
