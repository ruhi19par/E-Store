package com.example.lcwd.Electronic.dtos;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateOrderRequest {
    private String orderStatus;
    private String paymentStatus;
    private String userId;
    private String orderId;

}
