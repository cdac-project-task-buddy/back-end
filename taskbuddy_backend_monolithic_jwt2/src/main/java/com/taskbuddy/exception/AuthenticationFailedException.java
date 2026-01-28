package com.taskbuddy.exception;

@SuppressWarnings("serial")
public class AuthenticationFailedException extends RuntimeException {
	public AuthenticationFailedException(String mesg) {
		super(mesg);
	}
}
