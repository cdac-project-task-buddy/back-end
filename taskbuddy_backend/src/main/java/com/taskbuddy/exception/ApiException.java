package com.taskbuddy.exception;
// checked
public class ApiException extends RuntimeException {
	public ApiException(String mesg) {
		super(mesg);
	}
}
