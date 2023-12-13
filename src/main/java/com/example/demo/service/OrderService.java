package com.example.demo.service;
import com.example.demo.entity.OrderDetails;
import com.example.demo.entity.ProductStatistics;
import com.example.demo.response.CreateOrderResponse;

import java.time.LocalDate;
import java.util.List;

public interface OrderService {
    CreateOrderResponse createNewOrder(List<OrderDetails> orderDetails);

    List<ProductStatistics> getProductStatistics(LocalDate from, LocalDate to);
}
