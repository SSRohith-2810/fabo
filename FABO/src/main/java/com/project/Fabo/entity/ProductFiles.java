package com.project.Fabo.entity;

import java.util.Arrays;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;

@Entity
public class ProductFiles {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

 	@Lob
 	@Column(length = Integer.MAX_VALUE)  // Set the length to the maximum allowed value
 	private byte[] fileData;
 	  
 		@ManyToOne
 	    @JoinColumn(name = "product_request_id_add_admin")
 	    private AddProductAdmin addProductAdmin;

 	    @ManyToOne
 	    @JoinColumn(name = "product_request_id_client_product")
 	    private ClientProduct clientProduct;
    
    public ProductFiles() {
    	
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

	public ProductFiles(Long id, byte[] fileData, AddProductAdmin addProductAdmin, ClientProduct clientProduct) {
		super();
		this.id = id;
		this.fileData = fileData;
		this.addProductAdmin = addProductAdmin;
		this.clientProduct = clientProduct;
	}

	@Override
	public String toString() {
		return "AdminProductFile [id=" + id + ", fileData=" + Arrays.toString(fileData) + ", addProductAdmin="
				+ addProductAdmin + ", clientProduct=" + clientProduct + "]";
	}

	
}
