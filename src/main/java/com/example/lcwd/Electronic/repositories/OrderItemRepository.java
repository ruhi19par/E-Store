package com.example.lcwd.Electronic.repositories;

import com.example.lcwd.Electronic.entities.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem,Integer> {
}
