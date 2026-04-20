package com.example.lcwd.Electronic.dtos;

import com.example.lcwd.Electronic.entities.User;
import jakarta.persistence.OneToOne;
import lombok.*;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RefreshTokenDto {
    private int id;
    private String token;
    private Instant expiryDate;
}
