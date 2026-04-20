package com.example.lcwd.Electronic.services;

import com.example.lcwd.Electronic.dtos.AddItemToCartRequest;
import com.example.lcwd.Electronic.dtos.CartDto;

public interface CartService {
    CartDto AddItemsToCart(String userId, AddItemToCartRequest request);

    void removeItemfromCart(String userId, int cartItem);

    void clearCart(String userId);

    CartDto getCartByUser(String userId);

}
