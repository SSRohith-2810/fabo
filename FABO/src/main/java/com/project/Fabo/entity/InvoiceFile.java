package com.project.Fabo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;

@Entity
public class InvoiceFile {
	
	 	@Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;

	 	@Lob
	 	@Column(length = Integer.MAX_VALUE)  // Set the length to the maximum allowed value
	 	private byte[] fileData;

	    @ManyToOne
	    @JoinColumn(name = "invoice_id")
	    private Invoice invoice;
	    
	    public InvoiceFile() {
	    	
	    }

		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public byte[] getFileData() {
			return fileData;
		}

		public void setFileData(byte[] fileData) {
			this.fileData = fileData;
		}

		public Invoice getInvoice() {
			return invoice;
		}

		public void setInvoice(Invoice invoice) {
			this.invoice = invoice;
		}

		public InvoiceFile(Long id, byte[] fileData, Invoice invoice) {
			super();
			this.id = id;
			this.fileData = fileData;
			this.invoice = invoice;
		}



}
