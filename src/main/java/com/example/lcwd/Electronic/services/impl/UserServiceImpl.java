package com.example.lcwd.Electronic.services.impl;

import com.example.lcwd.Electronic.dtos.PageableResponse;
import com.example.lcwd.Electronic.dtos.UserDto;
import com.example.lcwd.Electronic.entities.Role;
import com.example.lcwd.Electronic.entities.User;
import com.example.lcwd.Electronic.exceptions.ResourceNotFound;
import com.example.lcwd.Electronic.helper.Helper;
import com.example.lcwd.Electronic.repositories.RoleRepository;
import com.example.lcwd.Electronic.repositories.UserRepository;
import com.example.lcwd.Electronic.services.UserService;
import org.modelmapper.ModelMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.PageRequest;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import java.util.stream.Collectors;
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${user.profile.image.path}")
    private String imagePath;

    @Value("${normal.role.id}")
    private String normalRoleId;
    @Value("${admin.role.id}")
    private String role_admin_id;
    @Autowired
    private RoleRepository roleRepository;


    private Logger logger= LoggerFactory.getLogger(UserServiceImpl.class);
    @Override
    public UserDto creat(UserDto userdto) {
        String userId= UUID.randomUUID().toString();
        userdto.setUserId(userId);
        userdto.setPassword(passwordEncoder.encode(userdto.getPassword()));
       User user=DtoToObj(userdto);
        Role adminRole = roleRepository.findById(normalRoleId).get();
        user.setRoles(Set.of(adminRole));
       User savedUser=userRepository.save(user);
       UserDto us1=ObjToDto(savedUser);
       return us1;
    }


    @Override
    public UserDto update(UserDto userDto, String userId) {
       User user=userRepository.findById(userId).orElseThrow(()->new ResourceNotFound("User with given id doesn;t exist"));
       user.setName(userDto.getName());
       user.setEmail(userDto.getEmail());
       user.setPassword(userDto.getPassword());
       user.setAbout(userDto.getAbout());
       user.setGender(userDto.getGender());
       user.setImageName(userDto.getImageName());
      User up= userRepository.save(user);
       UserDto us=ObjToDto(up);
       return us;
    }

    @Override
    public void deleteUser(String userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFound("User with given id doesn't exist")
                );

        String fullPath = System.getProperty("user.dir")
                + File.separator
                + imagePath
                + File.separator
                + user.getImageName();

        try {
            Path path = Paths.get(fullPath);

            if (Files.exists(path)) {
                Files.delete(path);
                logger.info("File deleted successfully: {}", fullPath);
            } else {
                logger.warn("File not found: {}", fullPath);
            }

        } catch (IOException ex) {
            logger.error("Error deleting file: {}", fullPath, ex);
        }

        userRepository.delete(user);
    }

    @Override
    public PageableResponse<UserDto> getAll(int pageNumber, int pageSize, String sortBy, String sortDir) {

        Sort sort=(sortDir.equalsIgnoreCase("desc"))?(Sort.by(sortBy).descending()):(Sort.by(sortBy).ascending());

        Pageable pageable = PageRequest.of(pageNumber, pageSize,sort);

        Page<User> page = userRepository.findAll(pageable);
        PageableResponse<UserDto>data=Helper.getPageableResponse(page, UserDto.class);
//        List<User> user = page.getContent();
//
//       List<UserDto>ls1=user.stream().map(use -> ObjToDto(use)).collect(Collectors.toUnmodifiableList());
//
//       PageableResponse<UserDto>data=new PageableResponse<>();
//       data.setContent(ls1);
//       data.setPageNumber(page.getNumber());
//       data.setPageSize(page.getSize());
//       data.setTotalElements(page.getTotalElements());
//       data.setTotalPages(page.getTotalPages());
//       data.setLastPage(page.isLast());
       return data;
    }

    @Override
    public UserDto getUserById(String userId) {
        User user=userRepository.findById(userId).orElseThrow(()->new ResourceNotFound("User with given id doesn;t exist"));
        return ObjToDto(user);
    }

    @Override
    public UserDto getUserByEmail(String email) {
        User user = userRepository.findUserByEmail(email).orElseThrow(()->new ResourceNotFound("User with given id doesn;t exist"));
        return ObjToDto(user);
    }

    @Override
    public List<UserDto> searchUser(String keyword) {
        List<User>us=userRepository.findByNameContaining(keyword);
        List<UserDto>us1=us.stream().map(user -> ObjToDto(user)).collect(Collectors.toUnmodifiableList());
        return us1;
    }

    @Override
    public Optional<User> findUserByEmailOptional(String email) {
        return userRepository.findUserByEmail(email);
    }

    private UserDto ObjToDto(User savedUser) {

        return modelMapper.map(savedUser,UserDto.class);
    }

    private User DtoToObj(UserDto userdto) {
//        User user=User.builder()
//                .userId(userdto.getUserId())
//                .name(userdto.getName())
//                .email(userdto.getEmail())
//                .password(userdto.getPassword())
//                .gender(userdto.getGender())
//                .about(userdto.getAbout())
//                .imageName(userdto.getImageName())
//                .build();
        return modelMapper.map(userdto,User.class);

    }

}
