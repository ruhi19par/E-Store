package com.example.lcwd.Electronic.repositories;

import com.example.lcwd.Electronic.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User,String> {
    Optional<User> findUserByEmail(String email);
    Optional<User> findUserByEmailAndPassword(String email, String password);
    List<User> findByNameContaining(String keyword);
}
