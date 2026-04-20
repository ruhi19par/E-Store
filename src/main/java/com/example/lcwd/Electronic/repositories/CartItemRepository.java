package com.example.lcwd.Electronic.repositories;

import com.example.lcwd.Electronic.entities.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem,Integer> {
}
