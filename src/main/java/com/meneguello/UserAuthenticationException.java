package com.meneguello;

public class UserAuthenticationException extends Exception {
	
	private static final long serialVersionUID = 8171590360708692156L;

	public UserAuthenticationException(Exception e) {
		super(e);
	}

}
