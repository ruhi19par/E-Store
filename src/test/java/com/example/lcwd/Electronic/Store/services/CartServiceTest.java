package com.example.lcwd.Electronic.Store.services;

import com.example.lcwd.Electronic.dtos.AddItemToCartRequest;
import com.example.lcwd.Electronic.dtos.CartDto;
import com.example.lcwd.Electronic.entities.*;
import com.example.lcwd.Electronic.repositories.*;
import com.example.lcwd.Electronic.services.impl.CartServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import org.junit.jupiter.api.extension.ExtendWith;

import org.modelmapper.ModelMapper;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class CartServiceTest {

    @Mock
    private CartRepository cartRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private CartServiceImpl cartService;

    private ModelMapper mapper = new ModelMapper();

    User user;
    Product product;
    Cart cart;

    @BeforeEach
    public void init(){

        user = User.builder()
                .userId("u1")
                .name("Durgesh")
                .email("durgesh@gmail.com")
                .password("1234")
                .build();

        product = Product.builder()
                .productId("p1")
                .title("Laptop")
                .price(50000)
                .discountedPrice(45000)
                .build();

        cart = Cart.builder()
                .cartId("c1")
                .user(user)
                .items(new ArrayList<>())   // IMPORTANT (mutable)
                .build();

        ReflectionTestUtils.setField(cartService, "mapper", mapper);
    }

    // ================= ADD ITEM =================

    @Test
    public void addItemsToCartTest(){

        AddItemToCartRequest request = AddItemToCartRequest.builder()
                .productId("p1")
                .quantity(2)
                .build();

        Mockito.when(userRepository.findById("u1"))
                .thenReturn(Optional.of(user));

        Mockito.when(productRepository.findById("p1"))
                .thenReturn(Optional.of(product));

        Mockito.when(cartRepository.findByUser(user))
                .thenReturn(Optional.of(cart));

        Mockito.when(cartRepository.save(Mockito.any()))
                .thenReturn(cart);

        CartDto result = cartService.AddItemsToCart("u1", request);

        Assertions.assertNotNull(result);
    }

    // ================= GET CART =================

    @Test
    public void getCartByUserTest(){

        Mockito.when(userRepository.findById("u1"))
                .thenReturn(Optional.of(user));

        Mockito.when(cartRepository.findByUser(user))
                .thenReturn(Optional.of(cart));

        CartDto result = cartService.getCartByUser("u1");

        Assertions.assertNotNull(result);
    }
}