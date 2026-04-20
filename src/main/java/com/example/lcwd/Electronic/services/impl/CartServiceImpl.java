package com.example.lcwd.Electronic.services.impl;

import com.example.lcwd.Electronic.dtos.AddItemToCartRequest;
import com.example.lcwd.Electronic.dtos.CartDto;
import com.example.lcwd.Electronic.entities.Cart;
import com.example.lcwd.Electronic.entities.CartItem;
import com.example.lcwd.Electronic.entities.User;
import com.example.lcwd.Electronic.exceptions.BadApiRequest;
import com.example.lcwd.Electronic.exceptions.ResourceNotFound;
import com.example.lcwd.Electronic.repositories.CartItemRepository;
import com.example.lcwd.Electronic.repositories.CartRepository;
import com.example.lcwd.Electronic.repositories.ProductRepository;
import com.example.lcwd.Electronic.repositories.UserRepository;
import com.example.lcwd.Electronic.services.CartService;

import com.example.lcwd.Electronic.entities.Product;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

@Service
public class CartServiceImpl implements CartService {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private CartItemRepository cartItemRepository;
    @Autowired
    private ModelMapper mapper;

    @Override
    public CartDto AddItemsToCart(String userId, AddItemToCartRequest request) {
        int quantity=request.getQuantity();
        String productId=request.getProductId();
        if(quantity<=0){
            throw new BadApiRequest("Requested quantity is not valid");
        }

        Product pr=productRepository.findById(productId).orElseThrow(()->new ResourceNotFound("resource with this id is not found")
        );

        User user=userRepository.findById(userId).orElseThrow(()->new ResourceNotFound("User with this email id is not found"));
        Cart cart=null;

        try{
            cart=cartRepository.findByUser(user).get();

        }catch (NoSuchElementException e){
            cart=new Cart();
            cart.setCartId(UUID.randomUUID().toString());
            cart.setCreatedAt(new Date());
            cart.setItems(new ArrayList<>());
        }
        List<CartItem>items=cart.getItems();

        boolean found = false;

        for (CartItem item : items) {
            if (item.getProduct().getProductId().equals(productId)) {
                item.setQuantity(quantity);
                item.setTotalPrice(quantity * pr.getDiscountedPrice());
                found = true;
                break;
            }
        }

        if (!found) {
            CartItem cartItem = CartItem.builder()
                    .quantity(quantity)
                    .totalPrice(quantity * pr.getDiscountedPrice())
                    .cart(cart)
                    .product(pr)
                    .build();

            items.add(cartItem);
        }

                cart.setUser(user);

                Cart update=cartRepository.save(cart);
                return mapper.map(update, CartDto.class);
    }

    @Override
    public void removeItemfromCart(String userId, int cartItemId) {
        CartItem cartItem1=cartItemRepository.findById(cartItemId).orElseThrow(()->new ResourceNotFound("CartItem with this id is not found"));
        cartItemRepository.delete(cartItem1);
    }

    @Override
    public void clearCart(String userId) {
        User user=userRepository.findById(userId).orElseThrow(()->new ResourceNotFound("User with this email id is not found"));
       Cart cart= cartRepository.findByUser(user).orElseThrow(()->new ResourceNotFound("cart is not available"));
        cart.getItems().clear();
        cartRepository.save(cart);

    }

    @Override
    public CartDto getCartByUser(String userId) {
        User user=userRepository.findById(userId).orElseThrow(()->new ResourceNotFound("User with this email id is not found"));
        Cart cart= cartRepository.findByUser(user).orElseThrow(()->new ResourceNotFound("cart is not available"));
        return mapper.map(cart,CartDto.class);
    }
}
