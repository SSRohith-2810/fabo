package com.project.Fabo.entity;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
public class AdminComments {
	
	    @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    @Column(name = "comment_id")
	    private Long commentId;

	    @ManyToOne
	    @JoinColumn(name = "add_support_admin_id") // Corrected the join column name
	    private AddSupportAdmin addSupportAdmin;

	    @ManyToOne
	    @JoinColumn(name = "client_support_id") // Corrected the join column name
	    private ClientSupport clientSupport;


	    @Column(name = "admin_comments")
	    private String adminComments;
	    
	    private String Reason;
	    
	    private String addedBy;
	    
	    private String notification;
	    
	    private String storeCode;
	    
	    @Column(name = "support_request_id")
	    private String supportRequestId;
	    
	    private Boolean requestStatus;

	    @Temporal(TemporalType.DATE)
	    @Column(name = "date_added")
	    private Date dateAdded;

	    @Temporal(TemporalType.TIME)
	    @Column(name = "time_added")
	    private Date timeAdded;
	    

		public Long getCommentId() {
			return commentId;
		}


		public void setCommentId(Long commentId) {
			this.commentId = commentId;
		}


		public AddSupportAdmin getAddSupportAdmin() {
			return addSupportAdmin;
		}


		public void setAddSupportAdmin(AddSupportAdmin addSupportAdmin) {
			this.addSupportAdmin = addSupportAdmin;
		}


		public ClientSupport getClientSupport() {
			return clientSupport;
		}


		public void setClientSupport(ClientSupport clientSupport) {
			this.clientSupport = clientSupport;
		}


		public String getAdminComments() {
			return adminComments;
		}


		public void setAdminComments(String adminComments) {
			this.adminComments = adminComments;
		}


		public String getReason() {
			return Reason;
		}


		public void setReason(String reason) {
			Reason = reason;
		}


		public String getAddedBy() {
			return addedBy;
		}


		public void setAddedBy(String addedBy) {
			this.addedBy = addedBy;
		}


		 public String getNotification() {
		        return notification;
		    }


		public void setNotification(String notification) {
			this.notification = notification;
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


		public Boolean getRequestStatus() {
			return requestStatus;
		}


		public void setRequestStatus(Boolean requestStatus) {
			this.requestStatus = requestStatus;
		}


		public Date getDateAdded() {
			return dateAdded;
		}


		public void setDateAdded(Date dateAdded) {
			this.dateAdded = dateAdded;
		}


		public Date getTimeAdded() {
			return timeAdded;
		}


		public void setTimeAdded(Date timeAdded) {
			this.timeAdded = timeAdded;
		}


		@Override
		public String toString() {
			return "AdminComments [commentId=" + commentId + ", addSupportAdmin=" + addSupportAdmin + ", clientSupport="
					+ clientSupport + ", adminComments=" + adminComments + ", Reason=" + Reason + ", addedBy=" + addedBy
					+ ", notification=" + notification + ", storeCode=" + storeCode + ", supportRequestId="
					+ supportRequestId + ", requestStatus=" + requestStatus + ", dateAdded=" + dateAdded
					+ ", timeAdded=" + timeAdded + "]";
		}


		public AdminComments(Long commentId, AddSupportAdmin addSupportAdmin, ClientSupport clientSupport,
				String adminComments, String reason, String addedBy, String notification, String storeCode,
				String supportRequestId, Boolean requestStatus, Date dateAdded, Date timeAdded) {
			super();
			this.commentId = commentId;
			this.addSupportAdmin = addSupportAdmin;
			this.clientSupport = clientSupport;
			this.adminComments = adminComments;
			Reason = reason;
			this.addedBy = addedBy;
			this.notification = notification;
			this.storeCode = storeCode;
			this.supportRequestId = supportRequestId;
			this.requestStatus = requestStatus;
			this.dateAdded = dateAdded;
			this.timeAdded = timeAdded;
		}


		public AdminComments() {
	    	
	    }

		
}
