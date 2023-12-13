package com.example.demo.service.implementation;

import com.example.demo.entity.OrderDetails;
import com.example.demo.entity.Orders;
import com.example.demo.entity.ProductStatistics;
import com.example.demo.entity.Products;
import com.example.demo.repository.OrderDetailsRepository;
import com.example.demo.repository.OrderRepository;
import com.example.demo.repository.ProductsRepository;
import com.example.demo.response.CreateOrderResponse;
import com.example.demo.service.OrderService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
public class OrderServiceIpml implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderDetailsRepository orderDetailsRepository;
    private final ProductsRepository productsRepository;

    public OrderServiceIpml(OrderRepository orderRepository, OrderDetailsRepository orderDetailsRepository, ProductsRepository productsRepository) {
        this.orderRepository = orderRepository;
        this.orderDetailsRepository = orderDetailsRepository;
        this.productsRepository = productsRepository;
    }

    @Override
    public CreateOrderResponse createNewOrder(List<OrderDetails> orderDetails) {
        if (orderDetails == null)
            throw new IllegalArgumentException("Danh sách này ít nhất phải có 1 phần tử");
        for (var tmp : orderDetails) {
            if (tmp.getQuantity() <= 0 && tmp.getQuantity() >= 100 )
                throw new IllegalArgumentException("Các phần tử trong danh sách OrderDetail phải có Quantity lớn hơn 0 và nhỏ hơn 100.");
        }

        var order = Orders
                .builder()
                .totalPrice(getTotalPrice(orderDetails))
                .build();

        orderRepository.save(order);

        for (var tmp : orderDetails) {
            orderDetailsRepository.save(tmp);
        }

        return CreateOrderResponse
                .builder()
                .message("Create Order Sucessfully")
                .build();
    }

    @Override
    public List<ProductStatistics> getProductStatistics(LocalDate from, LocalDate to) {
        List<ProductStatistics> statistics = new ArrayList<>();

        List<Orders> orders = orderRepository.findOrdersByDateRange(from.atStartOfDay(), to.atTime(LocalTime.MAX));

        Map<UUID, Integer> productQuantityMap = new HashMap<>();

        for (Orders order : orders) {
            List<OrderDetails> orderDetailsList = order.getOrderDetailsList();
            for (OrderDetails orderDetails : orderDetailsList) {
                UUID productId = orderDetails.getProduct().getId();
                int quantity = orderDetails.getQuantity();

                if (productQuantityMap.containsKey(productId)) {
                    int currentQuantity = productQuantityMap.get(productId);
                    productQuantityMap.put(productId, currentQuantity + quantity);
                } else {
                    productQuantityMap.put(productId, quantity);
                }
            }
        }

        for (Map.Entry<UUID, Integer> entry : productQuantityMap.entrySet()) {
            UUID productId = entry.getKey();
            int quantitySold = entry.getValue();

            Optional<Products> productOptional = productsRepository.findById(productId);
            if (productOptional.isPresent()) {
                Products product = productOptional.get();
                ProductStatistics productStatistics = new ProductStatistics(productId.toString(), product.getName(), quantitySold);
                statistics.add(productStatistics);
            }
        }

        return statistics;
    }

    private Integer getTotalPrice(List<OrderDetails> orderDetails) {
        int totalPrice = 0;
        for (var tmp : orderDetails) {
            totalPrice = tmp.getQuantity() * tmp.getPrice();
        }
        return totalPrice;
    }
}
