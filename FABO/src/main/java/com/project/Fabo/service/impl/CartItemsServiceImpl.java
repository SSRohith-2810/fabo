package com.project.Fabo.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.Fabo.entity.CartItems;
import com.project.Fabo.repository.CartItemsRepository;
import com.project.Fabo.service.CartItemsService;

@Service
public class CartItemsServiceImpl implements CartItemsService{
	
	@Autowired
	private CartItemsRepository cartItemsRepository;

	@Override
	public void saveCartItems(CartItems cartItems) {
		cartItemsRepository.save(cartItems);
	}

}
