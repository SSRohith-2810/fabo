package com.project.Fabo.entity;

public class CreativeWithImage {
	
	 private Long id;
	 private String base64Image;
	 
	 public CreativeWithImage() {
		 
	 }
	 
	@Override
	public String toString() {
		return "CreativeWithImage [id=" + id + ", base64Image=" + base64Image + "]";
	}
	public CreativeWithImage(Long id, String base64Image) {
		super();
		this.id = id;
		this.base64Image = base64Image;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getBase64Image() {
		return base64Image;
	}
	public void setBase64Image(String base64Image) {
		this.base64Image = base64Image;
	}

}
