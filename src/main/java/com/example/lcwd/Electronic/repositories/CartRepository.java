package com.example.lcwd.Electronic.repositories;

import com.example.lcwd.Electronic.entities.Cart;
import com.example.lcwd.Electronic.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart,String> {

    Optional<Cart> findByUser(User user);
}
