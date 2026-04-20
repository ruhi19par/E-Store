package com.example.lcwd.Electronic.services.impl;

import com.example.lcwd.Electronic.dtos.CreateOrderRequest;
import com.example.lcwd.Electronic.dtos.OrderDto;
import com.example.lcwd.Electronic.dtos.PageableResponse;
import com.example.lcwd.Electronic.dtos.UpdateOrderRequest;
import com.example.lcwd.Electronic.entities.*;
import com.example.lcwd.Electronic.exceptions.BadApiRequest;
import com.example.lcwd.Electronic.exceptions.ResourceNotFound;
import com.example.lcwd.Electronic.helper.Helper;
import com.example.lcwd.Electronic.repositories.CartRepository;
import com.example.lcwd.Electronic.repositories.OrderRepository;
import com.example.lcwd.Electronic.repositories.UserRepository;
import com.example.lcwd.Electronic.services.OrderService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ModelMapper mapper;

    @Override
    public OrderDto createOrder(CreateOrderRequest orderDto) {

        String userId=orderDto.getUserId();
        String cartId=orderDto.getCartId();
       User user=userRepository.findById(userId).orElseThrow(()->new ResourceNotFound("user with given id doesn't exits !!"));

       Cart cart=cartRepository.findById(cartId).orElseThrow(()->new ResourceNotFound("cart with given id is not found"));

       List<CartItem>cartItems=cart.getItems();
       if(cartItems.size()<=0) {
           throw new BadApiRequest("invalid no. of items in this cart");
       }

       Order order= Order.builder()
               .billingName(orderDto.getBillingName())
               .billingPhone(orderDto.getBillingPhone())
               .billingAddress(orderDto.getBillingAddress())
               .orderedDate(new Date())
               .deliveredDate(null)
               .paymentStatus(
                       orderDto.getPaymentStatus() != null ? orderDto.getPaymentStatus() : "NOTPAID"
               )
               .orderStatus(
                       orderDto.getOrderStatus() != null ? orderDto.getOrderStatus() : "PENDING"
               )
               .orderId(UUID.randomUUID().toString())
               .user(user)
               .build();
        AtomicInteger orderAmount= new AtomicInteger();
       List<OrderItem>orderItems=cartItems.stream().map(cartItem -> {


         OrderItem orderItem=  OrderItem.builder()
                   .quantity(cartItem.getQuantity())
                   .product(cartItem.getProduct())
                   .totalPrice(cartItem.getQuantity()* cartItem.getProduct().getDiscountedPrice())
                   .order(order)
                   .build();
        orderAmount.addAndGet(orderItem.getTotalPrice());
         return orderItem;
       }).collect(Collectors.toList());

       order.setOrderItems(orderItems);
       order.setOrderAmount(orderAmount.get());

       cart.getItems().clear();
       cartRepository.save(cart);
      Order savedOrder= orderRepository.save(order);
      return mapper.map(savedOrder, OrderDto.class);
    }

    @Override
    public void removeOrder(String orderId) {
    Order order=orderRepository.findById(orderId).orElseThrow(()->new ResourceNotFound("Order with given id doesn't exist"));

    orderRepository.delete(order);
    }

    @Override
    public List<OrderDto> getOrdersOfUser(String userId) {
        User user=userRepository.findById(userId).orElseThrow(()->new ResourceNotFound("user with given id doesn't exits !!"));

        List<Order>orders=orderRepository.findByUser(user);
        List<OrderDto>orderDtos=orders.stream().map(order -> mapper.map(order, OrderDto.class)).collect(Collectors.toList());

        return orderDtos;
    }

    @Override
    public PageableResponse<OrderDto> getAllOrders(int pageNumber, int pageSize, String sortBy, String sortDir) {
        Sort sort=(sortDir.equalsIgnoreCase("desc"))?(Sort.by(sortBy).descending()):(Sort.by(sortBy).ascending());
        Pageable pageable= PageRequest.of(pageNumber,pageSize,sort);
       Page<Order>page= orderRepository.findAll(pageable);
       return Helper.getPageableResponse(page,OrderDto.class);
    }

    @Override
    public OrderDto updateOrder(UpdateOrderRequest request) {
        String userId=request.getUserId();
        String orderId=request.getOrderId();
        User user=userRepository.findById(userId).orElseThrow(()->new ResourceNotFound("user with given id doesn't exits !!"));
        List<Order>orders=orderRepository.findByUser(user);
        Order ode=null;
        for (Order order : orders) {
            if (order.getOrderId().equals(orderId)) {
               order.setOrderStatus(request.getOrderStatus());
               order.setPaymentStatus(request.getPaymentStatus());
               ode=order;
                break;
            }
        }
        if(ode==null){
            throw new ResourceNotFound("your entered orderId is not valid");
        }
        else{
            Order od1=orderRepository.save(ode);
            return mapper.map(od1,OrderDto.class);
        }
    }
}
