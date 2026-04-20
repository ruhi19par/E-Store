package com.example.lcwd.Electronic.repositories;

import com.example.lcwd.Electronic.entities.Order;
import com.example.lcwd.Electronic.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order,String> {

    List<Order> findByUser(User user);
}
