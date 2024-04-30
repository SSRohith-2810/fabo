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
import jakarta.persistence.Table;

@Entity
@Table(name = "invoices")
public class Invoice {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(name = "invoice_number")
	private String invoiceNumber;
	private String storeName;
	@Column(name = "store_code")
	private String storeCode;
	private LocalDate invoiceDate;
	private String invoiceAmount; 
	@OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL)
    private List<InvoiceFile> invoiceFiles;
	private String invoiceType;
	private String invoiceComments;
	private String invoiceStatus;
	private boolean activeStatus = true;
	@OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL)
	private List<InvoiceComments> comments;
	private String uploadedBy;
	
	
	@Override
	public String toString() {
		return "Invoice [id=" + id + ", invoiceNumber=" + invoiceNumber + ", storeName=" + storeName + ", storeCode="
				+ storeCode + ", invoiceDate=" + invoiceDate + ", invoiceAmount=" + invoiceAmount + ", invoiceFiles="
				+ invoiceFiles + ", invoiceType=" + invoiceType + ", invoiceComments=" + invoiceComments
				+ ", invoiceStatus=" + invoiceStatus + ", activeStatus=" + activeStatus + ", comments=" + comments
				+ ", uploadedBy=" + uploadedBy + "]";
	}





	public Invoice(Long id, String invoiceNumber, String storeName, String storeCode, LocalDate invoiceDate,
			String invoiceAmount, List<InvoiceFile> invoiceFiles, String invoiceType, String invoiceComments,
			String invoiceStatus, boolean activeStatus, List<InvoiceComments> comments, String uploadedBy) {
		super();
		this.id = id;
		this.invoiceNumber = invoiceNumber;
		this.storeName = storeName;
		this.storeCode = storeCode;
		this.invoiceDate = invoiceDate;
		this.invoiceAmount = invoiceAmount;
		this.invoiceFiles = invoiceFiles;
		this.invoiceType = invoiceType;
		this.invoiceComments = invoiceComments;
		this.invoiceStatus = invoiceStatus;
		this.activeStatus = activeStatus;
		this.comments = comments;
		this.uploadedBy = uploadedBy;
	}


	public Invoice() {
		
	}


	public Long getId() {
		return id;
	}





	public void setId(Long id) {
		this.id = id;
	}





	public String getInvoiceNumber() {
		return invoiceNumber;
	}





	public void setInvoiceNumber(String invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
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





	public LocalDate getInvoiceDate() {
		return invoiceDate;
	}





	public void setInvoiceDate(LocalDate invoiceDate) {
		this.invoiceDate = invoiceDate;
	}





	public String getInvoiceAmount() {
		return invoiceAmount;
	}





	public void setInvoiceAmount(String invoiceAmount) {
		this.invoiceAmount = invoiceAmount;
	}





	public List<InvoiceFile> getInvoiceFiles() {
		return invoiceFiles;
	}





	public void setInvoiceFiles(List<InvoiceFile> invoiceFiles) {
		this.invoiceFiles = invoiceFiles;
	}





	public String getInvoiceType() {
		return invoiceType;
	}





	public void setInvoiceType(String invoiceType) {
		this.invoiceType = invoiceType;
	}





	public String getInvoiceComments() {
		return invoiceComments;
	}





	public void setInvoiceComments(String invoiceComments) {
		this.invoiceComments = invoiceComments;
	}





	public String getInvoiceStatus() {
		return invoiceStatus;
	}





	public void setInvoiceStatus(String invoiceStatus) {
		this.invoiceStatus = invoiceStatus;
	}





	public boolean isActiveStatus() {
		return activeStatus;
	}





	public void setActiveStatus(boolean activeStatus) {
		this.activeStatus = activeStatus;
	}





	public List<InvoiceComments> getComments() {
		return comments;
	}





	public void setComments(List<InvoiceComments> comments) {
		this.comments = comments;
	}





	public String getUploadedBy() {
		return uploadedBy;
	}





	public void setUploadedBy(String uploadedBy) {
		this.uploadedBy = uploadedBy;
	}





	/*public void setFormattedInvoiceNumber(String storeCode, Long id) {
	    // Use the store code and invoiceId (auto-incremented) to generate the invoice number
	    this.invoiceNumber = String.format("INV-%s-%05d", storeCode, id);
	}*/
	
	public void setFormattedInvoiceNumber(String storeCode, Long id) {
	    this.invoiceNumber = String.format("INV-%s-%d", storeCode, id);
	}



	
}
