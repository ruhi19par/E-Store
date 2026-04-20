package com.example.lcwd.Electronic.controllers;

//import com.example.lcwd.Electronic.services.impl;
import com.example.lcwd.Electronic.Security.JwtHelper;
import com.example.lcwd.Electronic.dtos.JwtRequest;
import com.example.lcwd.Electronic.dtos.JwtResponse;
import com.example.lcwd.Electronic.dtos.UserDto;
import com.example.lcwd.Electronic.entities.User;
import com.example.lcwd.Electronic.exceptions.BadApiRequest;
import com.example.lcwd.Electronic.exceptions.ResourceNotFound;
import com.example.lcwd.Electronic.repositories.UserRepository;
import com.example.lcwd.Electronic.services.UserService;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;
import com.google.api.client.json.jackson2.JacksonFactory;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.Principal;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;

import com.example.lcwd.Electronic.dtos.RefreshTokenDto;
import com.example.lcwd.Electronic.dtos.RefreshTokenRequest;
import com.example.lcwd.Electronic.services.RefreshTokenService;

@RestController
@RequestMapping("/auth")
//@CrossOrigin("*")
public class AuthController {

    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private ModelMapper mapper;
    @Autowired
    private AuthenticationManager manager;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RefreshTokenService refreshTokenService;

    @Autowired
    private ModelMapper modelMapper;   // you already have mapper → reuse OR keep one


    @Autowired
    private JwtHelper jwtHelper;

    @Value("${googleClientId}")
    private String googleClientId;
    @Value("${newPassword}")
    private String newPassword;

    private Logger logger= LoggerFactory.getLogger(AuthController.class);
    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody JwtRequest request){

        this.doAuthenticate(request.getEmail(),request.getPassword());

        UserDetails userDetails =
                userDetailsService.loadUserByUsername(request.getEmail());

        // ✅ Generate JWT
        String token = this.jwtHelper.generateToken(userDetails);

        // ✅ Get user
        User user = userRepository
                .findUserByEmail(userDetails.getUsername())
                .orElseThrow(() -> new ResourceNotFound("User not found"));

        UserDto userDto = mapper.map(user, UserDto.class);

        // ✅ Generate Refresh Token
        RefreshTokenDto refreshToken =
                refreshTokenService.createRefreshToken(user.getEmail());

        // ✅ Final Response
        JwtResponse res = JwtResponse.builder()
                .jwtToken(token)
                .refreshToken(refreshToken)   // 🔥 ADD THIS
                .user(userDto)
                .build();

        return ResponseEntity.ok(res);
    }

    private void doAuthenticate(String email, String password) {
        UsernamePasswordAuthenticationToken authentication=new UsernamePasswordAuthenticationToken(email,password);
        try{
            manager.authenticate(authentication);
        } catch (BadCredentialsException e) {
            throw new BadApiRequest("Invalid Username or Password");
        }

    }


    @GetMapping("/current")
    public ResponseEntity<UserDto> getCurrentUser(Principal principal){
        String Name=principal.getName();
        com.example.lcwd.Electronic.entities.User user =
                userRepository.findUserByEmail(Name)
                        .orElseThrow(() -> new ResourceNotFound("User not found"));

//        UserDto userDto = mapper.map(user, UserDto.class);
        UserDto userDto = new UserDto();
        userDto.setUserId(user.getUserId());
        userDto.setName(user.getName());
        userDto.setEmail(user.getEmail());
        userDto.setAbout(user.getAbout());
        userDto.setGender(user.getGender());
        userDto.setImageName(user.getImageName());

        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }

    @PostMapping("/google")
    public ResponseEntity<JwtResponse> loginWithGoogle(@RequestBody Map<String,Object> data) throws IOException, GeneralSecurityException {
        String idToken = data.get("token").toString();  // ✅ FIX

   NetHttpTransport netHttpTransport=new NetHttpTransport();
   JacksonFactory jacksonFactory=JacksonFactory.getDefaultInstance();

        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(netHttpTransport, jacksonFactory)
                .setAudience(Collections.singleton(googleClientId))
                .build();

        GoogleIdToken googleIdToken = verifier.verify(idToken);

        if (googleIdToken == null) {
            throw new RuntimeException("Invalid Google token");
        }

        GoogleIdToken.Payload payload = googleIdToken.getPayload();
        logger.info("Payload {}",payload);
        String email=payload.getEmail();

        User user=null;
        user=userService.findUserByEmailOptional(email).orElse(null);
        if(user==null){
            user = this.saveUser(
                    email,
                    payload.get("name").toString(),
                    payload.get("picture").toString()
            );
        }
        ResponseEntity<JwtResponse> jwtResponseResponseEntity=this.login(JwtRequest.builder().email(user.getEmail()).password(newPassword).build());
        return jwtResponseResponseEntity;

    }

    private User saveUser(String email, String name, String phoneUrl) {
        UserDto newUser=UserDto.builder()
                .name(name)
                .email(email)
                .password(newPassword)
                .imageName(phoneUrl)
                .roles(new HashSet<>())
                .build();
        UserDto user=userService.creat(newUser);
        return this.mapper.map(user,User.class);
    }



    @PostMapping("/regenerate-token")
    public ResponseEntity<JwtResponse> regenerateToken(
            @RequestBody RefreshTokenRequest request) {

        // ✅ Step 1: Find token from DB
        RefreshTokenDto refreshTokenDto =
                refreshTokenService.findByToken(request.getRefreshToken());

        // ✅ Step 2: Verify expiry
        RefreshTokenDto verifiedToken =
                refreshTokenService.verifyRefreshToken(refreshTokenDto);

        // ✅ Step 3: Get user from token
        UserDto user =
                refreshTokenService.getUser(verifiedToken);

        // ✅ Step 4: Generate new JWT
        String jwtToken =
                jwtHelper.generateToken(modelMapper.map(user, User.class));

        // ✅ Step 5: (Optional but better) rotate refresh token
        RefreshTokenDto newRefreshToken =
                refreshTokenService.createRefreshToken(user.getEmail());

        // ✅ Step 6: Build response
        JwtResponse response = JwtResponse.builder()
                .jwtToken(jwtToken)                 // ⚠️ keep consistent with your login API
                .refreshToken(newRefreshToken)      // 🔥 use new token (better security)
                .user(user)
                .build();

        return ResponseEntity.ok(response);
    }
}
