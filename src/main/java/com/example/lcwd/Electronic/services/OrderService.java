package com.example.lcwd.Electronic.services;


import com.example.lcwd.Electronic.dtos.CreateOrderRequest;
import com.example.lcwd.Electronic.dtos.OrderDto;
import com.example.lcwd.Electronic.dtos.PageableResponse;
import com.example.lcwd.Electronic.dtos.UpdateOrderRequest;

import java.util.List;

public interface OrderService {
    OrderDto createOrder(CreateOrderRequest orderDto);

    void removeOrder(String orderId);

    List<OrderDto> getOrdersOfUser(String userId);

    PageableResponse<OrderDto> getAllOrders(int pageNumber, int pageSize, String sortBy, String sortDir);

    OrderDto updateOrder(UpdateOrderRequest request);

}
