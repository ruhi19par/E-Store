package com.example.lcwd.Electronic.controllers;

import com.example.lcwd.Electronic.dtos.UserDto;
import com.example.lcwd.Electronic.entities.Role;
import com.example.lcwd.Electronic.entities.User;
import com.example.lcwd.Electronic.services.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @MockBean
    private UserService userService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private ObjectMapper objectMapper;

    private User user;
    private Role role;

    @BeforeEach
    public void init() {

        role = Role.builder()
                .roleId("abc")
                .rollName("NORMAL")
                .build();

        user = User.builder()
                .name("Durgesh")
                .email("durgesh@gmail.com")
                .about("This is testing create method")
                .gender("Male")
                .imageName("abc.png")
                .password("lcwd")
                .roles(Set.of(role))
                .build();
    }

    @Test
    public void createUserTest() throws Exception {

        UserDto dto = mapper.map(user, UserDto.class);

        Mockito.when(userService.creat(Mockito.any())).thenReturn(dto);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(convertObjectToJson(user))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Durgesh"))
                .andExpect(jsonPath("$.email").value("durgesh@gmail.com"));
    }

    private String convertObjectToJson(Object obj) throws Exception {
        return objectMapper.writeValueAsString(obj);
    }
}