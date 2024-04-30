package com.project.Fabo.entity;

import java.util.Arrays;
import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;

@Entity
public class Creatives {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long creativeId;
	private String creativeName;
	private String templateStyle;
	private String templateType;
	private String serviceType;
	private String discount;
	private String textStyle1;
	private String textStyle2;
	private String textStyle3;
	private String color1;
	private String color2;
	private String color3;
	private String phoneNumber;
	private String address;
	@Lob
	@Column(length = Integer.MAX_VALUE)
	private byte[] baseImage;
	@Lob
	@Column(length = Integer.MAX_VALUE)
	private byte[] canvasImage;

	@Override
	public String toString() {
		return "Creatives [creativeId=" + creativeId + ", creativeName=" + creativeName + ", templateStyle="
				+ templateStyle + ", templateType=" + templateType + ", serviceType=" + serviceType + ", discount="
				+ discount + ", textStyle1=" + textStyle1 + ", textStyle2=" + textStyle2 + ", textStyle3=" + textStyle3
				+ ", color1=" + color1 + ", color2=" + color2 + ", color3=" + color3 + ", phoneNumber=" + phoneNumber
				+ ", address=" + address + ", baseImage=" + Arrays.toString(baseImage) + ", canvasImage="
				+ Arrays.toString(canvasImage) + "]";
	}

	public Creatives(Long creativeId, String creativeName, String templateStyle, String templateType,
			String serviceType, String discount, String textStyle1, String textStyle2, String textStyle3, String color1,
			String color2, String color3, String phoneNumber, String address, byte[] baseImage, byte[] canvasImage) {
		super();
		this.creativeId = creativeId;
		this.creativeName = creativeName;
		this.templateStyle = templateStyle;
		this.templateType = templateType;
		this.serviceType = serviceType;
		this.discount = discount;
		this.textStyle1 = textStyle1;
		this.textStyle2 = textStyle2;
		this.textStyle3 = textStyle3;
		this.color1 = color1;
		this.color2 = color2;
		this.color3 = color3;
		this.phoneNumber = phoneNumber;
		this.address = address;
		this.baseImage = baseImage;
		this.canvasImage = canvasImage;
	}

	public Long getCreativeId() {
		return creativeId;
	}

	public void setCreativeId(Long creativeId) {
		this.creativeId = creativeId;
	}

	public String getCreativeName() {
		return creativeName;
	}

	public void setCreativeName(String creativeName) {
		this.creativeName = creativeName;
	}

	public String getTemplateStyle() {
		return templateStyle;
	}

	public void setTemplateStyle(String templateStyle) {
		this.templateStyle = templateStyle;
	}

	public String getTemplateType() {
		return templateType;
	}

	public void setTemplateType(String templateType) {
		this.templateType = templateType;
	}

	public String getServiceType() {
		return serviceType;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

	public String getDiscount() {
		return discount;
	}

	public void setDiscount(String discount) {
		this.discount = discount;
	}

	public String getTextStyle1() {
		return textStyle1;
	}

	public void setTextStyle1(String textStyle1) {
		this.textStyle1 = textStyle1;
	}

	public String getTextStyle2() {
		return textStyle2;
	}

	public void setTextStyle2(String textStyle2) {
		this.textStyle2 = textStyle2;
	}

	public String getTextStyle3() {
		return textStyle3;
	}

	public void setTextStyle3(String textStyle3) {
		this.textStyle3 = textStyle3;
	}

	public String getColor1() {
		return color1;
	}

	public void setColor1(String color1) {
		this.color1 = color1;
	}

	public String getColor2() {
		return color2;
	}

	public void setColor2(String color2) {
		this.color2 = color2;
	}

	public String getColor3() {
		return color3;
	}

	public void setColor3(String color3) {
		this.color3 = color3;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public byte[] getBaseImage() {
		return baseImage;
	}

	public void setBaseImage(byte[] baseImage) {
		this.baseImage = baseImage;
	}

	public byte[] getCanvasImage() {
		return canvasImage;
	}

	public void setCanvasImage(byte[] canvasImage) {
		this.canvasImage = canvasImage;
	}

	public Creatives() {
		
	}
	

}
