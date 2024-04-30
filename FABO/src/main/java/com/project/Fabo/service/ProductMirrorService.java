package com.project.Fabo.service;

import com.project.Fabo.entity.AddProductAdmin;
import com.project.Fabo.entity.ClientProduct;


public interface ProductMirrorService {

	void addProductRecordToBothSides(AddProductAdmin addProductAdmin);
	
	void addProductRecordToBothSide(ClientProduct clientProduct);

}
