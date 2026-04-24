package com.example.lcwd.Electronic.Store.services;

import com.example.lcwd.Electronic.dtos.CreateOrderRequest;
import com.example.lcwd.Electronic.dtos.OrderDto;
import com.example.lcwd.Electronic.entities.*;
import com.example.lcwd.Electronic.repositories.*;
import com.example.lcwd.Electronic.services.impl.OrderServiceImpl;

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
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CartRepository cartRepository;

    @InjectMocks
    private OrderServiceImpl orderService;

    private ModelMapper mapper = new ModelMapper();

    User user;
    Order order;

    @BeforeEach
    public void init(){

        user = User.builder()
                .userId("u1")
                .name("Durgesh")
                .email("durgesh@gmail.com")
                .password("1234")
                .build();

        order = Order.builder()
                .orderId("o1")
                .billingName("Durgesh")
                .billingPhone("9999999999")
                .billingAddress("India")
                .user(user)
                .build();

        ReflectionTestUtils.setField(orderService, "mapper", mapper);
    }

    // ================= CREATE ORDER =================

    @Test
    public void createOrderTest(){

        // request
        CreateOrderRequest request = CreateOrderRequest.builder()
                .userId("u1")
                .cartId("c1")
                .billingName("Durgesh")
                .billingPhone("9999999999")
                .billingAddress("India")
                .build();

        // product
        Product product = Product.builder()
                .title("Laptop")
                .price(50000)
                .discountedPrice(45000)
                .build();

        // cart item
        CartItem item = CartItem.builder()
                .product(product)
                .quantity(2)
                .totalPrice(90000)
                .build();

        // cart (IMPORTANT: items must NOT be null)
        Cart cart = Cart.builder()
                .cartId("c1")
                .user(user)
                .items(new ArrayList<>(List.of(item)))
                .build();

        // mocks
        Mockito.when(userRepository.findById("u1"))
                .thenReturn(Optional.of(user));

        Mockito.when(cartRepository.findById("c1"))
                .thenReturn(Optional.of(cart));

        Mockito.when(orderRepository.save(Mockito.any()))
                .thenReturn(order);

        // call
        OrderDto result = orderService.createOrder(request);

        // assertions
        Assertions.assertNotNull(result);
        Assertions.assertEquals("Durgesh", result.getBillingName());
    }

    // ================= REMOVE ORDER =================

    @Test
    public void removeOrderTest(){

        String orderId = "o1";

        Mockito.when(orderRepository.findById(orderId))
                .thenReturn(Optional.of(order));

        orderService.removeOrder(orderId);

        Mockito.verify(orderRepository, Mockito.times(1))
                .delete(order);
    }
}