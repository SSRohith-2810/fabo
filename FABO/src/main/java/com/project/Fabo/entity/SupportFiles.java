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
public class SupportFiles {
	
		@Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;
	
	 	@Lob
	 	@Column(length = Integer.MAX_VALUE)  // Set the length to the maximum allowed value
	 	private byte[] fileData;
 	  
 		@ManyToOne
 	    @JoinColumn(name = "support_request_id_add_admin")
 	    private AddSupportAdmin addSupportAdmin;

 	    @ManyToOne
 	    @JoinColumn(name = "support_request_id_client_support")
 	    private ClientSupport clientSupport;
    
    public SupportFiles() {
    	
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

	public SupportFiles(Long id, byte[] fileData, AddSupportAdmin addSupportAdmin, ClientSupport clientSupport) {
		super();
		this.id = id;
		this.fileData = fileData;
		this.addSupportAdmin = addSupportAdmin;
		this.clientSupport = clientSupport;
	}

	@Override
	public String toString() {
		return "AdminSupportFile [id=" + id + ", fileData=" + Arrays.toString(fileData) + ", addSupportAdmin="
				+ addSupportAdmin + ", clientSupport=" + clientSupport + "]";
	}

	
}
