package com.taskbuddy.exception;
// checked
@SuppressWarnings("serial")
public class AuthenticationFailedException extends RuntimeException {
	public AuthenticationFailedException(String mesg) {
		super(mesg);
	}
}
