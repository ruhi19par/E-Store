package com.example.lcwd.Electronic.services;

import com.example.lcwd.Electronic.dtos.RefreshTokenDto;
import com.example.lcwd.Electronic.dtos.UserDto;
import com.example.lcwd.Electronic.entities.RefreshToken;

public interface RefreshTokenService {

    RefreshTokenDto createRefreshToken(String username);

    RefreshTokenDto findByToken(String token);

    RefreshTokenDto verifyRefreshToken(RefreshTokenDto refreshTokenDto);

    UserDto getUser(RefreshTokenDto dto);
}