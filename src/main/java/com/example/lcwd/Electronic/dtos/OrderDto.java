package com.example.lcwd.Electronic.dtos;



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
public class OrderDto {
    private String orderId;
    @Builder.Default
    private String orderStatus="PENDING";
    @Builder.Default
    private String paymentStatus="NOTPAID";

    private int orderAmount;

    private String billingAddress;
    private String billingPhone;
    private String billingName;

    private Date orderedDate=new Date();
    private Date deliveredDate;


//    private User user;


    private List<OrderItemDto> orderItems=new ArrayList<>();
}
