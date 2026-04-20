package com.example.lcwd.Electronic.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class CreateOrderRequest {
    @NotBlank(message = "cartId is required !! ")
    private String cartId;
    @NotBlank(message = "userId is required !! ")
    private String userId;
    @Builder.Default
    private String orderStatus="PENDING";
    @Builder.Default
    private String paymentStatus="NOTPAID";
    @NotBlank(message = "BillingAddress is required !! ")
    private String billingAddress;
    @NotBlank(message = "BillingPhone is required !! ")
    private String billingPhone;
    @NotBlank(message = "BillingName is required !! ")
    private String billingName;




//    private User user;



}
