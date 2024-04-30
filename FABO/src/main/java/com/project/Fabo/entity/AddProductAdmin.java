package com.project.Fabo.entity;

import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class AddProductAdmin {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String storeName;
	private String storeCode;
	@Column(name = "product_request_id")
	private String productRequestId;
	private String productRequestType;
	private String storeContact;
	private LocalDate Date;
	private String issueSubject;
	private String issueDescription;
	private String uploadedBy;
	private String status;
	@OneToMany(mappedBy = "addProductAdmin", cascade = CascadeType.ALL)
    private List<ProductFiles> adminProductFiles;
	private String internalComments; 
	private String commentsToClient;
	private String commentsFromClient;
	private String shippingAddress;
	private boolean activeStatus = true;
	@OneToMany(mappedBy = "addProductAdmin", cascade = CascadeType.ALL)
	private List<AdminProductComments> comments;
	
	public AddProductAdmin() {
		
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


	public String getProductRequestId() {
		return productRequestId;
	}


	public void setProductRequestId(String productRequestId) {
		this.productRequestId = productRequestId;
	}


	public String getProductRequestType() {
		return productRequestType;
	}


	public void setProductRequestType(String productRequestType) {
		this.productRequestType = productRequestType;
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


	public List<ProductFiles> getAdminProductFiles() {
		return adminProductFiles;
	}


	public void setAdminProductFiles(List<ProductFiles> adminProductFiles) {
		this.adminProductFiles = adminProductFiles;
	}


	public String getInternalComments() {
		return internalComments;
	}


	public void setInternalComments(String internalComments) {
		this.internalComments = internalComments;
	}


	public String getCommentsToClient() {
		return commentsToClient;
	}


	public void setCommentsToClient(String commentsToClient) {
		this.commentsToClient = commentsToClient;
	}


	public String getCommentsFromClient() {
		return commentsFromClient;
	}


	public void setCommentsFromClient(String commentsFromClient) {
		this.commentsFromClient = commentsFromClient;
	}


	public String getShippingAddress() {
		return shippingAddress;
	}


	public void setShippingAddress(String shippingAddress) {
		this.shippingAddress = shippingAddress;
	}


	public boolean isActiveStatus() {
		return activeStatus;
	}


	public void setActiveStatus(boolean activeStatus) {
		this.activeStatus = activeStatus;
	}


	public List<AdminProductComments> getComments() {
		return comments;
	}


	public void setComments(List<AdminProductComments> comments) {
		this.comments = comments;
	}


	@Override
	public String toString() {
		return "AddProductAdmin [id=" + id + ", storeName=" + storeName + ", storeCode=" + storeCode
				+ ", productRequestId=" + productRequestId + ", productRequestType=" + productRequestType
				+ ", storeContact=" + storeContact + ", Date=" + Date + ", issueSubject=" + issueSubject
				+ ", issueDescription=" + issueDescription + ", uploadedBy=" + uploadedBy + ", status=" + status
				+ ", adminProductFiles=" + adminProductFiles + ", internalComments=" + internalComments
				+ ", commentsToClient=" + commentsToClient + ", commentsFromClient=" + commentsFromClient
				+ ", shippingAddress=" + shippingAddress + ", activeStatus=" + activeStatus + ", comments=" + comments
				+ "]";
	}


	public AddProductAdmin(Long id, String storeName, String storeCode, String productRequestId,
			String productRequestType, String storeContact, LocalDate date, String issueSubject,
			String issueDescription, String uploadedBy, String status, List<ProductFiles> adminProductFiles,
			String internalComments, String commentsToClient, String commentsFromClient, String shippingAddress,
			boolean activeStatus, List<AdminProductComments> comments) {
		super();
		this.id = id;
		this.storeName = storeName;
		this.storeCode = storeCode;
		this.productRequestId = productRequestId;
		this.productRequestType = productRequestType;
		this.storeContact = storeContact;
		Date = date;
		this.issueSubject = issueSubject;
		this.issueDescription = issueDescription;
		this.uploadedBy = uploadedBy;
		this.status = status;
		this.adminProductFiles = adminProductFiles;
		this.internalComments = internalComments;
		this.commentsToClient = commentsToClient;
		this.commentsFromClient = commentsFromClient;
		this.shippingAddress = shippingAddress;
		this.activeStatus = activeStatus;
		this.comments = comments;
	}


	public void setFormattedProductNumber(Long id) {
        // Use the invoiceId (auto-incremented) to generate the invoice number
        this.productRequestId = String.format("PR-%s-%d", storeCode, id);
    }

}
