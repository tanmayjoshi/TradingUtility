package com.jpmorgan.org.exception;

public class ReadingTradeDataException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5392363897333652813L;

	
	public ReadingTradeDataException(Exception e) {
		super(e);
	}

	/**
	 *  Instantiates a new Reading Trade Data Exception.
	 *
	 * @param message the message
	 * @param throwable the throwable
	 */
	public ReadingTradeDataException(String message, Throwable throwable) {
		super(message, throwable);
	}

}
