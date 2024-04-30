package com.project.Fabo.entity;

import java.util.Arrays;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;

@Entity
@Table(name = "images")
public class ImageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
	private String category;
    
    @Lob
    @Column(name = "image_data", columnDefinition = "LONGBLOB")
    private byte[] imageData;

    @Override
	public String toString() {
		return "ImageEntity [id=" + id + ", category=" + category + ", imageData=" + Arrays.toString(imageData) + "]";
	}

	public ImageEntity(Long id, String category, byte[] imageData) {
		this.id = id;
		this.category = category;
		this.imageData = imageData;
	}
	
	   public ImageEntity(String category, byte[] imageData) {
			this.category = category;
			this.imageData = imageData;
		}


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public byte[] getImageData() {
		return imageData;
	}

	public void setImageData(byte[] imageData) {
		this.imageData = imageData;
	}
    
    public ImageEntity() {
    	
    }

	
}