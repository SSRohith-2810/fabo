package com.project.Fabo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.Fabo.entity.CartItems;

public interface CartItemsRepository extends JpaRepository<CartItems, Long>{

}
