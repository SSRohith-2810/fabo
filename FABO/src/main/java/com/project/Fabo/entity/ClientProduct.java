package com.project.Fabo.entity;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToMany;

@Entity
public class ClientProduct {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String storeName;
	private String storeCode;
	private String productRequestType;
	private String productRequestId;
	private String storeContact;
	private LocalDate Date;
	@OneToMany(mappedBy = "clientProduct", cascade = CascadeType.ALL)
    private List<ProductFiles> adminProductFiles;
	private String issueSubject;
	private String issueDescription;
	private String shippingAddress;
	private String closeRequest;
	private String uploadedBy;
	private String status;
	private boolean activeStatus = true;
	private String externalComments;
	private String commentsToAdmin;
	@OneToMany(mappedBy = "clientProduct", cascade = CascadeType.ALL)
	private List<AdminProductComments> comments;
	
	public ClientProduct() {
		
	}


		@Override
	public String toString() {
		return "ClientProduct [id=" + id + ", storeName=" + storeName + ", storeCode=" + storeCode
				+ ", productRequestType=" + productRequestType + ", productRequestId=" + productRequestId
				+ ", storeContact=" + storeContact + ", Date=" + Date + ", adminProductFiles=" + adminProductFiles
				+ ", issueSubject=" + issueSubject + ", issueDescription=" + issueDescription + ", shippingAddress="
				+ shippingAddress + ", closeRequest=" + closeRequest + ", uploadedBy=" + uploadedBy + ", status="
				+ status + ", activeStatus=" + activeStatus + ", externalComments=" + externalComments
				+ ", commentsToAdmin=" + commentsToAdmin + ", comments=" + comments + "]";
	}


		public ClientProduct(Long id, String storeName, String storeCode, String productRequestType,
			String productRequestId, String storeContact, LocalDate date, List<ProductFiles> adminProductFiles,
			String issueSubject, String issueDescription, String shippingAddress, String closeRequest,
			String uploadedBy, String status, boolean activeStatus, String externalComments, String commentsToAdmin,
			List<AdminProductComments> comments) {
		super();
		this.id = id;
		this.storeName = storeName;
		this.storeCode = storeCode;
		this.productRequestType = productRequestType;
		this.productRequestId = productRequestId;
		this.storeContact = storeContact;
		Date = date;
		this.adminProductFiles = adminProductFiles;
		this.issueSubject = issueSubject;
		this.issueDescription = issueDescription;
		this.shippingAddress = shippingAddress;
		this.closeRequest = closeRequest;
		this.uploadedBy = uploadedBy;
		this.status = status;
		this.activeStatus = activeStatus;
		this.externalComments = externalComments;
		this.commentsToAdmin = commentsToAdmin;
		this.comments = comments;
	}


		public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public String getStoreName() {
		return storeName;
	}


	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}


	public String getStoreCode() {
		return storeCode;
	}


	public void setStoreCode(String storeCode) {
		this.storeCode = storeCode;
	}


	public String getProductRequestType() {
		return productRequestType;
	}


	public void setProductRequestType(String productRequestType) {
		this.productRequestType = productRequestType;
	}


	public String getProductRequestId() {
		return productRequestId;
	}


	public void setProductRequestId(String productRequestId) {
		this.productRequestId = productRequestId;
	}


	public String getStoreContact() {
		return storeContact;
	}


	public void setStoreContact(String storeContact) {
		this.storeContact = storeContact;
	}


	public LocalDate getDate() {
		return Date;
	}


	public void setDate(LocalDate date) {
		Date = date;
	}


	public List<ProductFiles> getAdminProductFiles() {
		return adminProductFiles;
	}


	public void setAdminProductFiles(List<ProductFiles> adminProductFiles) {
		this.adminProductFiles = adminProductFiles;
	}


	public String getIssueSubject() {
		return issueSubject;
	}


	public void setIssueSubject(String issueSubject) {
		this.issueSubject = issueSubject;
	}


	public String getIssueDescription() {
		return issueDescription;
	}


	public void setIssueDescription(String issueDescription) {
		this.issueDescription = issueDescription;
	}


	public String getShippingAddress() {
		return shippingAddress;
	}


	public void setShippingAddress(String shippingAddress) {
		this.shippingAddress = shippingAddress;
	}


	public String getCloseRequest() {
		return closeRequest;
	}


	public void setCloseRequest(String closeRequest) {
		this.closeRequest = closeRequest;
	}


	public String getUploadedBy() {
		return uploadedBy;
	}


	public void setUploadedBy(String uploadedBy) {
		this.uploadedBy = uploadedBy;
	}


	public String getStatus() {
		return status;
	}


	public void setStatus(String status) {
		this.status = status;
	}


	public boolean isActiveStatus() {
		return activeStatus;
	}


	public void setActiveStatus(boolean activeStatus) {
		this.activeStatus = activeStatus;
	}


	public String getExternalComments() {
		return externalComments;
	}


	public void setExternalComments(String externalComments) {
		this.externalComments = externalComments;
	}


	public String getCommentsToAdmin() {
		return commentsToAdmin;
	}


	public void setCommentsToAdmin(String commentsToAdmin) {
		this.commentsToAdmin = commentsToAdmin;
	}


	public List<AdminProductComments> getComments() {
		return comments;
	}


	public void setComments(List<AdminProductComments> comments) {
		this.comments = comments;
	}


		public void setFormattedProductNumber(Long id) {
	        // Use the invoiceId (auto-incremented) to generate the invoice number
	        this.productRequestId = String.format("PR-%s-%d", storeCode, id);
	    }

	
}
