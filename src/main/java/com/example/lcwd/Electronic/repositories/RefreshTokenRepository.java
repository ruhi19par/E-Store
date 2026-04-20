package com.example.lcwd.Electronic.repositories;

import com.example.lcwd.Electronic.entities.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.lcwd.Electronic.entities.User;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken,Integer> {

    Optional<RefreshToken> findByToken(String token);


    Optional<RefreshToken> findByUser(User user);
}
