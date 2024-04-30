package com.project.Fabo.entity;

import java.sql.Timestamp;
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
public class AdminProductComments {
	
	  @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    @Column(name = "product_comment_id")
	    private Long productCommentId;

	    @ManyToOne
	    @JoinColumn(name = "add_product_id")
	    private AddProductAdmin addProductAdmin;
	    @ManyToOne
	    @JoinColumn(name = "client_product_id")
	    private ClientProduct clientProduct;

	    private String addedBy;
	    
	    private String notification;
	    
	    @Column(name = "admin_product_comments")
	    private String adminProductComments;
	    
	    private String Reason;
	    
	    @Column(name = "product_request_id")
	    private String productRequestId;
	    
	    private boolean requestStatus;
	    
	    @Temporal(TemporalType.DATE)
	    @Column(name = "date_added")
	    private Date dateAdded;

	    @Temporal(TemporalType.TIME)
	    @Column(name = "time_added")
	    private Date timeAdded;

		
		
		public Long getProductCommentId() {
			return productCommentId;
		}



		public void setProductCommentId(Long productCommentId) {
			this.productCommentId = productCommentId;
		}



		public AddProductAdmin getAddProductAdmin() {
			return addProductAdmin;
		}



		public void setAddProductAdmin(AddProductAdmin addProductAdmin) {
			this.addProductAdmin = addProductAdmin;
		}



		public ClientProduct getClientProduct() {
			return clientProduct;
		}



		public void setClientProduct(ClientProduct clientProduct) {
			this.clientProduct = clientProduct;
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



		public String getAdminProductComments() {
			return adminProductComments;
		}



		public void setAdminProductComments(String adminProductComments) {
			this.adminProductComments = adminProductComments;
		}



		public String getReason() {
			return Reason;
		}



		public void setReason(String reason) {
			Reason = reason;
		}



		public String getProductRequestId() {
			return productRequestId;
		}



		public void setProductRequestId(String productRequestId) {
			this.productRequestId = productRequestId;
		}



		public boolean getRequestStatus() {
			return requestStatus;
		}



		public void setRequestStatus(boolean requestStatus) {
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
			return "AdminProductComments [productCommentId=" + productCommentId + ", addProductAdmin=" + addProductAdmin
					+ ", clientProduct=" + clientProduct + ", addedBy=" + addedBy + ", notification=" + notification
					+ ", adminProductComments=" + adminProductComments + ", Reason=" + Reason + ", productRequestId="
					+ productRequestId + ", requestStatus=" + requestStatus + ", dateAdded=" + dateAdded
					+ ", timeAdded=" + timeAdded + "]";
		}



		public AdminProductComments(Long productCommentId, AddProductAdmin addProductAdmin, ClientProduct clientProduct,
				String addedBy, String notification, String adminProductComments, String reason,
				String productRequestId, boolean requestStatus, Date dateAdded, Date timeAdded) {
			super();
			this.productCommentId = productCommentId;
			this.addProductAdmin = addProductAdmin;
			this.clientProduct = clientProduct;
			this.addedBy = addedBy;
			this.notification = notification;
			this.adminProductComments = adminProductComments;
			Reason = reason;
			this.productRequestId = productRequestId;
			this.requestStatus = requestStatus;
			this.dateAdded = dateAdded;
			this.timeAdded = timeAdded;
		}



		public AdminProductComments()
		{
			
		}
	    
}