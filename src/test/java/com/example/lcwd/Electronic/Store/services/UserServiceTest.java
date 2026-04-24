package com.example.lcwd.Electronic.Store.services;

import com.example.lcwd.Electronic.dtos.PageableResponse;
import com.example.lcwd.Electronic.dtos.UserDto;
import com.example.lcwd.Electronic.entities.Role;
import com.example.lcwd.Electronic.entities.User;
import com.example.lcwd.Electronic.repositories.RoleRepository;
import com.example.lcwd.Electronic.repositories.UserRepository;
import com.example.lcwd.Electronic.services.impl.UserServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import org.junit.jupiter.api.extension.ExtendWith;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    // ✅ USE REAL MAPPER (IMPORTANT)
    private ModelMapper modelMapper = new ModelMapper();

    User user;
    Role role;

    @BeforeEach
    public void init(){

        role = Role.builder()
                .roleId("abc")
                .rollName("NORMAL")
                .build();

        user = User.builder()
                .name("Durgesh")
                .email("durgesh@gmail.com")
                .about("This is a testing file")
                .gender("Male")
                .imageName("abc.png")
                .password("1234")
                .roles(Set.of(role))
                .build();

        // inject required fields manually
        ReflectionTestUtils.setField(userService, "normalRoleId", "abc");
        ReflectionTestUtils.setField(userService, "modelMapper", modelMapper);
    }

    @Test
    public void creatTest(){

        Mockito.when(roleRepository.findById(Mockito.anyString()))
                .thenReturn(Optional.of(role));

        Mockito.when(userRepository.save(Mockito.any()))
                .thenReturn(user);

        Mockito.when(passwordEncoder.encode(Mockito.anyString()))
                .thenReturn("encoded_password");

        UserDto inputDto = modelMapper.map(user, UserDto.class);

        UserDto user1 = userService.creat(inputDto);

        Assertions.assertNotNull(user1);
        Assertions.assertEquals("Durgesh", user1.getName());
    }

    @Test
    public void updateUserTest() {

        String userId = "abc";

        UserDto userDto = UserDto.builder()
                .name("Durgesh Kumar Tiwari")
                .about("This is updated user about details")
                .gender("Male")
                .imageName("xyz.png")
                .build();

        Mockito.when(userRepository.findById(Mockito.anyString()))
                .thenReturn(Optional.of(user));

        Mockito.when(userRepository.save(Mockito.any()))
                .thenReturn(user);

        UserDto updatedUser = userService.update(userDto, userId);

        Assertions.assertNotNull(updatedUser);

        Assertions.assertEquals(userDto.getName(), updatedUser.getName());
        Assertions.assertEquals(userDto.getAbout(), updatedUser.getAbout());
        Assertions.assertEquals(userDto.getImageName(), updatedUser.getImageName());
    }
    @Test
    public void deleteUserTest() {

        String userId = "abc";

        Mockito.when(userRepository.findById(Mockito.anyString()))
                .thenReturn(Optional.of(user));

        userService.deleteUser(userId);

        Mockito.verify(userRepository, Mockito.times(1))
                .delete(user);
    }
    @Test
    public void getAllUsersTest() {

        User user1 = User.builder()
                .name("Ankit")
                .email("ankit@gmail.com")
                .about("Test user 1")
                .gender("Male")
                .imageName("abc.png")
                .password("1234")
                .roles(Set.of(role))
                .build();

        User user2 = User.builder()
                .name("Uttam")
                .email("uttam@gmail.com")
                .about("Test user 2")
                .gender("Male")
                .imageName("abc.png")
                .password("1234")
                .roles(Set.of(role))
                .build();

        List<User> userList = Arrays.asList(user, user1, user2);

        Page<User> page = new PageImpl<>(userList);

        Mockito.when(userRepository.findAll(Mockito.any(Pageable.class)))
                .thenReturn(page);

        PageableResponse<UserDto> allUsers =
                userService.getAll(0, 5, "name", "asc");

        Assertions.assertNotNull(allUsers);
        Assertions.assertEquals(3, allUsers.getContent().size());
    }
    @Test
    public void getUserByIdTest() {

        String userId = "abc";

        Mockito.when(userRepository.findById(Mockito.anyString()))
                .thenReturn(Optional.of(user));

        UserDto userDto = userService.getUserById(userId);

        Assertions.assertNotNull(userDto);
        Assertions.assertEquals(user.getName(), userDto.getName());
    }
    @Test
    public void getUserByEmailTest() {

        String email = "durgesh@gmail.com";

        Mockito.when(userRepository.findUserByEmail(Mockito.anyString()))
                .thenReturn(Optional.of(user));

        UserDto userDto = userService.getUserByEmail(email);

        Assertions.assertNotNull(userDto);
        Assertions.assertEquals(user.getEmail(), userDto.getEmail());
    }

}