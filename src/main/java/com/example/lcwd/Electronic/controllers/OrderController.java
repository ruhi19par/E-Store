package com.example.lcwd.Electronic.controllers;

import com.example.lcwd.Electronic.dtos.*;
import com.example.lcwd.Electronic.services.OrderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
//@EnableMethodSecurity
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderDto> create_order(@Valid @RequestBody CreateOrderRequest request){
    OrderDto ord=orderService.createOrder(request);
    return new ResponseEntity<>(ord, HttpStatus.CREATED);
    }
//    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{orderId}")
    public ResponseEntity<ApiResponseMessage> remove_Order(@PathVariable String orderId){
        orderService.removeOrder(orderId);
        ApiResponseMessage mes= ApiResponseMessage.builder()
                .message("Your Order with provided orderId has been deleted successfully !!!")
                .success(true)
                .status(HttpStatus.OK)
                .build();
        return new ResponseEntity<>(mes,HttpStatus.OK);
    }
//    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/users/{userId}")
    public ResponseEntity<List<OrderDto>> getOrdersUser(@PathVariable String userId){
            List<OrderDto>orderDtos=orderService.getOrdersOfUser(userId);
            return new ResponseEntity<>(orderDtos,HttpStatus.OK);
    }

//    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<PageableResponse<OrderDto>> getOrders( @RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber, @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize, @RequestParam(value = "sortBy", defaultValue = "orderedDate", required = false) String sortBy, @RequestParam(value = "sortDir", defaultValue = "desc", required = false) String sortDir
    ){
        PageableResponse<OrderDto>pageableResponse=orderService.getAllOrders(pageNumber,pageSize,sortBy,sortDir);
        return new ResponseEntity<>(pageableResponse,HttpStatus.OK);
    }
    @PutMapping
    public ResponseEntity<OrderDto> updateOrder(@RequestBody UpdateOrderRequest request){
        OrderDto orderDto=orderService.updateOrder(request);
        return new ResponseEntity<>(orderDto,HttpStatus.OK);
    }

}
