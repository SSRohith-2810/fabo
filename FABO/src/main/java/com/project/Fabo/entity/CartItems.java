package com.project.Fabo.entity;

import java.util.Arrays;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;

@Entity
public class CartItems {
	
	 	@Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;
	    private String productTitle;
	    private String productRequestType;
	    private Long productPrice;
	    private String description;
	    @Lob
	    @Column(name = "image", columnDefinition = "LONGBLOB")
	    private byte[] image;

	    @Override
		public String toString() {
			return "CartItems [id=" + id + ", productTitle=" + productTitle + ", productRequestType="
					+ productRequestType + ", productPrice=" + productPrice + ", description=" + description
					+ ", image=" + Arrays.toString(image) + "]";
		}

		public CartItems(Long id, String productTitle, String productRequestType, Long productPrice, String description,
				byte[] image) {
			super();
			this.id = id;
			this.productTitle = productTitle;
			this.productRequestType = productRequestType;
			this.productPrice = productPrice;
			this.description = description;
			this.image = image;
		}

		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public String getProductTitle() {
			return productTitle;
		}

		public void setProductTitle(String productTitle) {
			this.productTitle = productTitle;
		}

		public String getProductRequestType() {
			return productRequestType;
		}

		public void setProductRequestType(String productRequestType) {
			this.productRequestType = productRequestType;
		}

		public Long getProductPrice() {
			return productPrice;
		}

		public void setProductPrice(Long productPrice) {
			this.productPrice = productPrice;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

		public byte[] getImage() {
			return image;
		}

		public void setImage(byte[] image) {
			this.image = image;
		}

		public CartItems() {
	    	
	    }
}
