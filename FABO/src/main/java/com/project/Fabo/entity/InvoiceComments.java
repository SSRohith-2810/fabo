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
public class InvoiceComments {

	 @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    @Column(name = "incmt_id")
	    private Long incmtId;
	 
	 @ManyToOne
	    @JoinColumn(name = "invoice_id") // Corrected the join column name
	    private Invoice invoice;
	 
	 @Column(name = "admin_comments")
	    private String adminComments;
	    
	    private String Reason;
	    
	    private String addedBy;
	    
	    private String notification;
	    
	    private Boolean requestStatus;

	    @Temporal(TemporalType.DATE)
	    @Column(name = "date_added")
	    private Date dateAdded;

	    @Temporal(TemporalType.TIME)
	    @Column(name = "time_added")
	    private Date timeAdded;
	    
	   

		@Override
		public String toString() {
			return "InvoiceComments [incmtId=" + incmtId + ", invoice=" + invoice + ", adminComments=" + adminComments
					+ ", Reason=" + Reason + ", addedBy=" + addedBy + ", notification=" + notification
					+ ", requestStatus=" + requestStatus + ", dateAdded=" + dateAdded + ", timeAdded=" + timeAdded
					+ "]";
		}



		public InvoiceComments(Long incmtId, Invoice invoice, String adminComments, String reason, String addedBy,
				String notification, Boolean requestStatus, Date dateAdded, Date timeAdded) {
			super();
			this.incmtId = incmtId;
			this.invoice = invoice;
			this.adminComments = adminComments;
			Reason = reason;
			this.addedBy = addedBy;
			this.notification = notification;
			this.requestStatus = requestStatus;
			this.dateAdded = dateAdded;
			this.timeAdded = timeAdded;
		}



		public Long getIncmtId() {
			return incmtId;
		}



		public void setIncmtId(Long incmtId) {
			this.incmtId = incmtId;
		}



		public Invoice getInvoice() {
			return invoice;
		}



		public void setInvoice(Invoice invoice) {
			this.invoice = invoice;
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



		public InvoiceComments() {
	    	
	    }
	    
	    
	    

}
