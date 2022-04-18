package com.porkerspicks.ggs.exceptions;

public class HorseSelectionException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;

	private String message = null; 
	
	public HorseSelectionException(String message) {
		super();
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
