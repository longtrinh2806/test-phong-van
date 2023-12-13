package com.example.demo.config;

import com.example.demo.entity.Products;
import com.example.demo.repository.ProductsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Configuration
public class AppConfig {
    private final ProductsRepository productsRepository;

    @Autowired
    public AppConfig(ProductsRepository productsRepository) {
        this.productsRepository = productsRepository;
    }

    @Bean
    public List<Products> productConfig() {
        var product1 = Products
                .builder()
                .id(UUID.fromString("e30bdd0f-ac79-45c1-baa3-df2ba533146c"))
                .code("P01")
                .name("Apple")
                .price(5)
                .quantity(30)
                .build();
        var product2 = Products
                .builder()
                .id(UUID.fromString("178d9376-fc54-4d94-86cc-3ea7d22ca2dc"))
                .code("P02")
                .name("Banana")
                .price(6)
                .quantity(50)
                .build();
        var product3 = Products
                .builder()
                .id(UUID.fromString("302072aa-ecde-4752-85b3-aef54ec61660"))
                .code("C03")
                .name("Lemon")
                .price(2)
                .quantity(60)
                .build();
        productsRepository.save(product1);
        productsRepository.save(product2);
        productsRepository.save(product3);

        List<Products> productsList = new ArrayList<>();
        productsList.add(product1);
        productsList.add(product2);
        productsList.add(product3);

        return productsList;
    }
}
