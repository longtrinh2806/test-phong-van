package com.example.demo.controller;

import com.example.demo.entity.OrderDetails;
import com.example.demo.entity.ProductStatistics;
import com.example.demo.response.CreateOrderResponse;
import com.example.demo.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("api/v1/order")
public class OrderController {
    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<CreateOrderResponse> createOrder(@RequestBody List<OrderDetails> orderDetails) {
        return ResponseEntity.ok(orderService.createNewOrder(orderDetails));
    }

    @GetMapping("/statistics")
    public ResponseEntity<List<ProductStatistics>> getProductStatistics(
            @RequestParam("from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDate from,
            @RequestParam("to") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDate to) {
        List<ProductStatistics> statistics = orderService.getProductStatistics(from, to);
        return ResponseEntity.ok(statistics);
    }
}
