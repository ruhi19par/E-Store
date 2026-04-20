package com.example.lcwd.Electronic.dtos;

import com.example.lcwd.Electronic.entities.Role;
import com.example.lcwd.Electronic.validate.ImageNameValidate;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {
    private String userId;
    @Size(min=3, max=30, message = "Invalid name")
    private String name;
//    @Email(message = "Invalid Email")
@Pattern(
        regexp = "^[a-z0-9]+[-._a-z0-9]*@([a-z0-9]+\\.)+[a-z]{2,5}$",
        message = "Invalid email format"
)
    @NotBlank(message = "email is required")
    private String email;
    @NotBlank(message = "password is required")
    private String password;
    @Size(min =4, max = 6, message = "Invalid Gender")
    private String gender;
@NotBlank(message = "write something about yourself")
    private String about;
    @ImageNameValidate
    private String imageName;

    private Set<RoleDto> roles=new HashSet<>();
}
