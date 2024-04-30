package com.project.Fabo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "clients")
public class Client {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long Id;
	@Column(unique = true)
	private String storeCode;
	private String email;
	private String storeName;
	private String ownerContact;
	private String storeContact;
	private String state;
	private String city;
	private String fullAddress;
	private String gmbProfileLink;
	private String gstNo;
	private String ownerName;
	private boolean activeStatus = true;
	
	@Override
	public String toString() {
		return "Client [Id=" + Id + ", storeCode=" + storeCode + ", email=" + email + ", storeName=" + storeName
				+ ", ownerContact=" + ownerContact + ", storeContact=" + storeContact + ", state=" + state + ", city="
				+ city + ", fullAddress=" + fullAddress + ", gmbProfileLink=" + gmbProfileLink + ", gstNo=" + gstNo
				+ ", ownerName=" + ownerName + ", activeStatus=" + activeStatus + "]";
	}

	public Client(Long id, String storeCode, String email, String storeName, String ownerContact, String storeContact,
			String state, String city, String fullAddress, String gmbProfileLink, String gstNo, String ownerName,
			boolean activeStatus) {
		super();
		Id = id;
		this.storeCode = storeCode;
		this.email = email;
		this.storeName = storeName;
		this.ownerContact = ownerContact;
		this.storeContact = storeContact;
		this.state = state;
		this.city = city;
		this.fullAddress = fullAddress;
		this.gmbProfileLink = gmbProfileLink;
		this.gstNo = gstNo;
		this.ownerName = ownerName;
		this.activeStatus = activeStatus;
	}

	public Long getId() {
		return Id;
	}

	public void setId(Long id) {
		Id = id;
	}

	public String getStoreCode() {
		return storeCode;
	}

	public void setStoreCode(String storeCode) {
		this.storeCode = storeCode;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getStoreName() {
		return storeName;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}

	public String getOwnerContact() {
		return ownerContact;
	}

	public void setOwnerContact(String ownerContact) {
		this.ownerContact = ownerContact;
	}

	public String getStoreContact() {
		return storeContact;
	}

	public void setStoreContact(String storeContact) {
		this.storeContact = storeContact;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getFullAddress() {
		return fullAddress;
	}

	public void setFullAddress(String fullAddress) {
		this.fullAddress = fullAddress;
	}

	public String getGmbProfileLink() {
		return gmbProfileLink;
	}

	public void setGmbProfileLink(String gmbProfileLink) {
		this.gmbProfileLink = gmbProfileLink;
	}

	public String getGstNo() {
		return gstNo;
	}

	public void setGstNo(String gstNo) {
		this.gstNo = gstNo;
	}

	public String getOwnerName() {
		return ownerName;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}

	public boolean isActiveStatus() {
		return activeStatus;
	}

	public void setActiveStatus(boolean activeStatus) {
		this.activeStatus = activeStatus;
	}

	public Client() {
		
	}
	
	
}	