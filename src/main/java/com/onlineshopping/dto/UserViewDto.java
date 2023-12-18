package com.onlineshopping.dto;

import com.onlineshopping.entity.Address;

public class UserViewDto {
	
	private String userName;
	private String userEmail;
	private long phonenumber;
	private String password;
	private char userType;
	private Address address;
	public UserViewDto() {
		super();
	}
	public UserViewDto(String userName, String userEmail, long phonenumber, String password, char userType,
			Address address) {
		super();
		this.userName = userName;
		this.userEmail = userEmail;
		this.phonenumber = phonenumber;
		this.password = password;
		this.userType = userType;
		this.address = address;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserEmail() {
		return userEmail;
	}
	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}
	public long getPhonenumber() {
		return phonenumber;
	}
	public void setPhonenumber(long phonenumber) {
		this.phonenumber = phonenumber;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public char getUserType() {
		return userType;
	}
	public void setUserType(char userType) {
		this.userType = userType;
	}
	public Address getAddress() {
		return address;
	}
	public void setAddress(Address address) {
		this.address = address;
	}
	@Override
	public String toString() {
		return "UserViewDto [userName=" + userName + ", userEmail=" + userEmail + ", phonenumber=" + phonenumber
				+ ", password=" + password + ", userType=" + userType + ", address=" + address + "]";
	}

}
