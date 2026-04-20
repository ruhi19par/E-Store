package com.example.lcwd.Electronic.services;

import com.example.lcwd.Electronic.dtos.PageableResponse;
import com.example.lcwd.Electronic.dtos.UserDto;
import com.example.lcwd.Electronic.entities.User;

import java.util.List;
import java.util.Optional;

public interface UserService  {
    UserDto creat(UserDto userdto);

    UserDto update(UserDto userDto, String userId);

    void deleteUser(String userId);

   PageableResponse<UserDto> getAll(int pageNumber, int pageSize, String sortBy, String sortDir);

    UserDto getUserById(String userId);

    UserDto getUserByEmail(String email);

    List<UserDto> searchUser(String keyword);

    Optional<User> findUserByEmailOptional(String email);

}
