package com.example.lcwd.Electronic.controllers;

import com.example.lcwd.Electronic.dtos.ApiResponseMessage;
import com.example.lcwd.Electronic.dtos.ImageResponse;
import com.example.lcwd.Electronic.dtos.PageableResponse;
import com.example.lcwd.Electronic.dtos.UserDto;
import com.example.lcwd.Electronic.services.FileService;
import com.example.lcwd.Electronic.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StreamUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
private Logger logger= LoggerFactory.getLogger(UserController.class);
    @Autowired
    private UserService userService;

    @Autowired
    private FileService fileService;

            @Value("${user.profile.image.path}")
            private String imageUploadPath;
    @PostMapping
    public ResponseEntity<UserDto> crea(@Valid @RequestBody UserDto userDto){
        UserDto userDto1=userService.creat(userDto);
        return new ResponseEntity<>(userDto1, HttpStatus.CREATED);
    }
    @PutMapping("/{userId}")
    public ResponseEntity<UserDto> upd( @Valid @PathVariable("userId") String userId, @RequestBody UserDto userDto){
        UserDto us=userService.update(userDto, userId);
        return new ResponseEntity<>(us,HttpStatus.OK);
    }
    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponseMessage> Del(@PathVariable("userId") String userId){
        userService.deleteUser(userId);
        ApiResponseMessage mes= ApiResponseMessage.builder().message("User has been deleted successfully!").success(true).status(HttpStatus.OK).build();
        return new ResponseEntity<>(mes,HttpStatus.OK);
    }
    @GetMapping
    public ResponseEntity<PageableResponse<UserDto>> getal(
            @RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "name", required = false) String sortBy,
    @RequestParam(value = "sortDir", defaultValue = "ASC", required = false) String sortDir

    ){
        return new ResponseEntity<>(userService.getAll(pageNumber,pageSize,sortBy, sortDir),HttpStatus.OK);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserDto> get_sin(@PathVariable String userId){
        return new ResponseEntity<>(userService.getUserById(userId),HttpStatus.OK);
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<UserDto> getUserByEmail(@PathVariable String email){
        return new ResponseEntity<>(userService.getUserByEmail(email),HttpStatus.OK);
    }

    @GetMapping("/search/{keywords}")
    public ResponseEntity<List<UserDto>> searchUser(@PathVariable String keywords){
        return new ResponseEntity<>(userService.searchUser(keywords),HttpStatus.OK);
    }
    @PostMapping("/image/{userId}")
    public ResponseEntity<ImageResponse>Uploadfile(@RequestParam("userImage")MultipartFile image, @PathVariable String userId, HttpServletRequest request ){
        logger.info("Content-Type: {}", request.getContentType());
        System.out.println("Controller path: " + imageUploadPath);
       String ImageName= fileService.uploadFile(image, imageUploadPath);

       UserDto user =userService.getUserById(userId);
       user.setImageName(ImageName);
       UserDto user1=userService.update(user,userId);
       ImageResponse imageResponse=ImageResponse.builder().imageName(ImageName).success(true).status(HttpStatus.CREATED).build();
       return new ResponseEntity<>(imageResponse,HttpStatus.CREATED);
    }
    @GetMapping("/image/{userId}")
    public void serveUserImage(@PathVariable String userId, HttpServletResponse response) throws IOException {
        UserDto user=userService.getUserById(userId);
        logger.info("User Image Name :{}",user.getImageName());
        InputStream resource=fileService.getResource(imageUploadPath,user.getImageName());
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(resource,response.getOutputStream());
    }

}
