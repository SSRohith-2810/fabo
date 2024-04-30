package com.project.Fabo.entity;

import java.util.Collections;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Admin {
	
	@Id
	private String userName;
	private String email;
	private String adminName;
	private String phoneNumber;
	private String displayName;
	private String token;
	private String concatenatedRoleNames;
	private List<Long> roleIds;
	private boolean activeStatus=true;
	
	
	@Override
	public String toString() {
		return "Admin [userName=" + userName + ", email=" + email + ", adminName=" + adminName + ", phoneNumber="
				+ phoneNumber + ", displayName=" + displayName + ", token=" + token + ", concatenatedRoleNames="
				+ concatenatedRoleNames + ", roleIds=" + roleIds + ", activeStatus=" + activeStatus + "]";
	}


	public Admin(String userName, String email, String adminName, String phoneNumber, String displayName, String token,
			String concatenatedRoleNames, List<Long> roleIds, boolean activeStatus) {
		super();
		this.userName = userName;
		this.email = email;
		this.adminName = adminName;
		this.phoneNumber = phoneNumber;
		this.displayName = displayName;
		this.token = token;
		this.concatenatedRoleNames = concatenatedRoleNames;
		this.roleIds = roleIds;
		this.activeStatus = activeStatus;
	}


	public String getUserName() {
		return userName;
	}


	public void setUserName(String userName) {
		this.userName = userName;
	}


	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}


	public String getAdminName() {
		return adminName;
	}


	public void setAdminName(String adminName) {
		this.adminName = adminName;
	}


	public String getPhoneNumber() {
		return phoneNumber;
	}


	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}


	public String getDisplayName() {
		return displayName;
	}


	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}


	public String getToken() {
		return token;
	}


	public void setToken(String token) {
		this.token = token;
	}


	public String getConcatenatedRoleNames() {
		return concatenatedRoleNames;
	}


	public void setConcatenatedRoleNames(String concatenatedRoleNames) {
		this.concatenatedRoleNames = concatenatedRoleNames;
	}


	public List<Long> getRoleIds() {
		return roleIds;
	}


	public void setRoleIds(List<Long> roleIds) {
		this.roleIds = roleIds;
	}


	public boolean isActiveStatus() {
		return activeStatus;
	}


	public void setActiveStatus(boolean activeStatus) {
		this.activeStatus = activeStatus;
	}


	public Admin() {
		
	}


	
	
}
