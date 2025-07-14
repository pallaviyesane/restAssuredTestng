package com.pojo.response;

public class UserRes {
	String username;
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getUserID() {
		return userID;
	}
	public void setUserID(String userID) {
		this.userID = userID;
	}
	public String[] getBooks() {
		return books;
	}
	public void setBooks(String[] books) {
		this.books = books;
	}
	String userID;
	String[] books;


}
