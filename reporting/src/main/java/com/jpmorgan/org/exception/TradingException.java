package com.jpmorgan.org.exception;

public class TradingException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6846477819475326883L;

	public TradingException(String message) {
		super(message);
	}

	/**
	 * Instantiates a new transaction execution exception.
	 *
	 * @param message the message
	 * @param throwable the throwable
	 */
	public TradingException(String message, Throwable throwable) {
		super(message, throwable);
	}
}
