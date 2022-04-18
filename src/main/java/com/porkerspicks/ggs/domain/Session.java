package com.porkerspicks.ggs.domain;

public class Session {

	private String sessionToken;
	private String loginStatus;

	public String getSessionToken() {
		return sessionToken;
	}
	public void setSessionToken(String id) {
		this.sessionToken = id;
	}
	public String getLoginStatus() {
		return loginStatus;
	}
	public void setLoginStatus(String status) {
		this.loginStatus = status;
	}
}
