package com.project.Fabo.entity;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;


@Entity
public class ClientUser {
	
  @Id
  private String userName;
  private String email;
  private String token;
  private String storeCode;
  private String storeName;
  private String displayName;
  private String phoneNumber;
  @Column(name = "concatenated_role_names")
  private String concatenatedRoleNames;
  private List<Long> roleIds;
  private boolean activeStatus=true;

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

public String getToken() {
	return token;
}

public void setToken(String token) {
	this.token = token;
}

public String getStoreCode() {
	return storeCode;
}

public void setStoreCode(String storeCode) {
	this.storeCode = storeCode;
}

public String getStoreName() {
	return storeName;
}

public void setStoreName(String storeName) {
	this.storeName = storeName;
}

public String getDisplayName() {
	return displayName;
}

public void setDisplayName(String displayName) {
	this.displayName = displayName;
}

public String getPhoneNumber() {
	return phoneNumber;
}

public void setPhoneNumber(String phoneNumber) {
	this.phoneNumber = phoneNumber;
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

@Override
public String toString() {
	return "ClientUser [userName=" + userName + ", email=" + email + ", token=" + token + ", storeCode=" + storeCode
			+ ", storeName=" + storeName + ", displayName=" + displayName + ", phoneNumber=" + phoneNumber
			+ ", concatenatedRoleNames=" + concatenatedRoleNames + ", roleIds=" + roleIds + ", activeStatus="
			+ activeStatus + "]";
}

public ClientUser(String userName, String email, String token, String storeCode, String storeName, String displayName,
		String phoneNumber, String concatenatedRoleNames, List<Long> roleIds, boolean activeStatus) {
	super();
	this.userName = userName;
	this.email = email;
	this.token = token;
	this.storeCode = storeCode;
	this.storeName = storeName;
	this.displayName = displayName;
	this.phoneNumber = phoneNumber;
	this.concatenatedRoleNames = concatenatedRoleNames;
	this.roleIds = roleIds;
	this.activeStatus = activeStatus;
}

public ClientUser() {
	  
  }


}

