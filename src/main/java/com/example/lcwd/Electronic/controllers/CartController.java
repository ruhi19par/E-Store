package com.example.lcwd.Electronic.controllers;

import com.example.lcwd.Electronic.dtos.AddItemToCartRequest;
import com.example.lcwd.Electronic.dtos.ApiResponseMessage;
import com.example.lcwd.Electronic.dtos.CartDto;
import com.example.lcwd.Electronic.services.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/carts")
public class CartController {
    @Autowired
    private CartService cartService;

    @PostMapping("/{userId}")

    public ResponseEntity<CartDto> addItemsCart(@PathVariable String userId,
                                                @RequestBody AddItemToCartRequest request){
        CartDto cardDto=cartService.AddItemsToCart(userId,request);
        return new ResponseEntity<>(cardDto, HttpStatus.CREATED);
    }
    @DeleteMapping("/{userId}/items/{itemId}")
    public ResponseEntity<ApiResponseMessage> removeItemsCart(@PathVariable String userId, @PathVariable int itemId){
        cartService.removeItemfromCart(userId,itemId);
        ApiResponseMessage res= ApiResponseMessage.builder()
                .message("Item has been deleted successfully !!!")
                .status(HttpStatus.OK)
                .success(true)
                .build();
        return new ResponseEntity<>(res,HttpStatus.OK);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponseMessage> clearCart( @PathVariable String userId){
        cartService.clearCart(userId);
        ApiResponseMessage res= ApiResponseMessage.builder()
                .message("Cart is now blank !!!")
                .status(HttpStatus.OK)
                .success(true)
                .build();
        return new ResponseEntity<>(res,HttpStatus.OK);
    }

    @GetMapping("/{userId}")

    public ResponseEntity<CartDto> getCartByUser(@PathVariable String userId){
        CartDto cardDto=cartService.getCartByUser(userId);
        return new ResponseEntity<>(cardDto, HttpStatus.CREATED);
    }
}
