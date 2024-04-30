package com.project.Fabo.entity;

public class ProductDTO {
    private String productName;
    private String productDescription;
    private String price;
    private String totalPrice;
    private int quantity;
    private String imageUrl;
    
	@Override
	public String toString() {
		return "ProductDTO [productName=" + productName + ", productDescription=" + productDescription + ", price="
				+ price + ", totalPrice=" + totalPrice + ", quantity=" + quantity + ", imageUrl=" + imageUrl + "]";
	}
	public ProductDTO(String productName, String productDescription, String price, String totalPrice, int quantity,
			String imageUrl) {
		super();
		this.productName = productName;
		this.productDescription = productDescription;
		this.price = price;
		this.totalPrice = totalPrice;
		this.quantity = quantity;
		this.imageUrl = imageUrl;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getProductDescription() {
		return productDescription;
	}
	public void setProductDescription(String productDescription) {
		this.productDescription = productDescription;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getTotalPrice() {
		return totalPrice;
	}
	public void setTotalPrice(String totalPrice) {
		this.totalPrice = totalPrice;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

    // Constructor, getters, and setters
}

