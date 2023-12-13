package com.example.demo.request;

import lombok.Getter;

import java.util.UUID;


@Getter
public class OrderDetailsRequest {
    private Integer quantity;
    private Integer price;
    private UUID productId;
}
