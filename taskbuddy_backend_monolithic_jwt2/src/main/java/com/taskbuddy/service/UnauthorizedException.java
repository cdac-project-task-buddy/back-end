package com.taskbuddy.service;

public class UnauthorizedException extends RuntimeException {
	UnauthorizedException(String msg){
		super(msg);
	}
}
