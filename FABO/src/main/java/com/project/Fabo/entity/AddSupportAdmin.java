package com.project.Fabo.entity;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToMany;

@Entity
public class AddSupportAdmin {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String storeName;
	private String storeCode;
	@Column(name = "support_request_id")
	private String supportRequestId;
	private String supportRequestType;
	private String storeContact;
	private LocalDate Date;
	private String uploadedBy;
	private String issueSubject;
	private String issueDescription;
	private String status;
	@OneToMany(mappedBy = "addSupportAdmin", cascade = CascadeType.ALL)
    private List<SupportFiles> adminSupportFiles;
	private String internalComments; 
	private String commentsToClient;
	private String commentsFromClient;
	private boolean activeStatus = true;
	@OneToMany(mappedBy = "addSupportAdmin", cascade = CascadeType.ALL)
	private List<AdminComments> comments;
	
	
	
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



	public String getSupportRequestId() {
		return supportRequestId;
	}



	public void setSupportRequestId(String supportRequestId) {
		this.supportRequestId = supportRequestId;
	}



	public String getSupportRequestType() {
		return supportRequestType;
	}



	public void setSupportRequestType(String supportRequestType) {
		this.supportRequestType = supportRequestType;
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



	public String getUploadedBy() {
		return uploadedBy;
	}



	public void setUploadedBy(String uploadedBy) {
		this.uploadedBy = uploadedBy;
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



	public String getStatus() {
		return status;
	}



	public void setStatus(String status) {
		this.status = status;
	}



	public List<SupportFiles> getAdminSupportFiles() {
		return adminSupportFiles;
	}



	public void setAdminSupportFiles(List<SupportFiles> adminSupportFiles) {
		this.adminSupportFiles = adminSupportFiles;
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



	public boolean isActiveStatus() {
		return activeStatus;
	}



	public void setActiveStatus(boolean activeStatus) {
		this.activeStatus = activeStatus;
	}



	public List<AdminComments> getComments() {
		return comments;
	}



	public void setComments(List<AdminComments> comments) {
		this.comments = comments;
	}



	@Override
	public String toString() {
		return "AddSupportAdmin [id=" + id + ", storeName=" + storeName + ", storeCode=" + storeCode
				+ ", supportRequestId=" + supportRequestId + ", supportRequestType=" + supportRequestType
				+ ", storeContact=" + storeContact + ", Date=" + Date + ", uploadedBy=" + uploadedBy + ", issueSubject="
				+ issueSubject + ", issueDescription=" + issueDescription + ", status=" + status
				+ ", adminSupportFiles=" + adminSupportFiles + ", internalComments=" + internalComments
				+ ", commentsToClient=" + commentsToClient + ", commentsFromClient=" + commentsFromClient
				+ ", activeStatus=" + activeStatus + ", comments=" + comments + "]";
	}



	public AddSupportAdmin(Long id, String storeName, String storeCode, String supportRequestId,
			String supportRequestType, String storeContact, LocalDate date, String uploadedBy, String issueSubject,
			String issueDescription, String status, List<SupportFiles> adminSupportFiles, String internalComments,
			String commentsToClient, String commentsFromClient, boolean activeStatus, List<AdminComments> comments) {
		super();
		this.id = id;
		this.storeName = storeName;
		this.storeCode = storeCode;
		this.supportRequestId = supportRequestId;
		this.supportRequestType = supportRequestType;
		this.storeContact = storeContact;
		Date = date;
		this.uploadedBy = uploadedBy;
		this.issueSubject = issueSubject;
		this.issueDescription = issueDescription;
		this.status = status;
		this.adminSupportFiles = adminSupportFiles;
		this.internalComments = internalComments;
		this.commentsToClient = commentsToClient;
		this.commentsFromClient = commentsFromClient;
		this.activeStatus = activeStatus;
		this.comments = comments;
	}

	public AddSupportAdmin() {
		
	}


	public void setFormattedSupportNumber(Long id) {
        // Use the invoiceId (auto-incremented) to generate the invoice number
        this.supportRequestId = String.format("SR-%s-%d", storeCode, id);
    }


}
