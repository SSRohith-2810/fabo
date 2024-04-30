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
public class ClientSupport {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String storeName;
	private String storeCode;
	private String supportRequestType;
	private String supportRequestId;
	private String storeContact;
	private LocalDate Date;
	@OneToMany(mappedBy = "clientSupport", cascade = CascadeType.ALL)
    private List<SupportFiles> adminSupportFiles;
	private String issueSubject;
	private String issueDescription;
	private String closeRequest;
	private String uploadedBy;
	private String status;
	private boolean activeStatus = true;
	private String externalComments;
	private String commentsToAdmin;
	@OneToMany(mappedBy = "clientSupport", cascade = CascadeType.ALL)
	private List<AdminComments> comments;

	

		@Override
	public String toString() {
		return "ClientSupport [id=" + id + ", storeName=" + storeName + ", storeCode=" + storeCode
				+ ", supportRequestType=" + supportRequestType + ", supportRequestId=" + supportRequestId
				+ ", storeContact=" + storeContact + ", Date=" + Date + ", adminSupportFiles=" + adminSupportFiles
				+ ", issueSubject=" + issueSubject + ", issueDescription=" + issueDescription + ", closeRequest="
				+ closeRequest + ", uploadedBy=" + uploadedBy + ", status=" + status + ", activeStatus=" + activeStatus
				+ ", externalComments=" + externalComments + ", commentsToAdmin=" + commentsToAdmin + ", comments="
				+ comments + "]";
	}

		public ClientSupport(Long id, String storeName, String storeCode, String supportRequestType,
			String supportRequestId, String storeContact, LocalDate date, List<SupportFiles> adminSupportFiles,
			String issueSubject, String issueDescription, String closeRequest, String uploadedBy, String status,
			boolean activeStatus, String externalComments, String commentsToAdmin, List<AdminComments> comments) {
		super();
		this.id = id;
		this.storeName = storeName;
		this.storeCode = storeCode;
		this.supportRequestType = supportRequestType;
		this.supportRequestId = supportRequestId;
		this.storeContact = storeContact;
		Date = date;
		this.adminSupportFiles = adminSupportFiles;
		this.issueSubject = issueSubject;
		this.issueDescription = issueDescription;
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

	public String getSupportRequestType() {
		return supportRequestType;
	}

	public void setSupportRequestType(String supportRequestType) {
		this.supportRequestType = supportRequestType;
	}

	public String getSupportRequestId() {
		return supportRequestId;
	}

	public void setSupportRequestId(String supportRequestId) {
		this.supportRequestId = supportRequestId;
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

	public List<SupportFiles> getAdminSupportFiles() {
		return adminSupportFiles;
	}

	public void setAdminSupportFiles(List<SupportFiles> adminSupportFiles) {
		this.adminSupportFiles = adminSupportFiles;
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

	public List<AdminComments> getComments() {
		return comments;
	}
 
	public void setComments(List<AdminComments> comments) {
		this.comments = comments;
	}

		public ClientSupport() {
		
	}
		
		public void setFormattedSupportNumber(Long id) {
	        // Use the invoiceId (auto-incremented) to generate the invoice number
	        this.supportRequestId = String.format("SR-%s-%d", storeCode, id);
	    }

	
}
