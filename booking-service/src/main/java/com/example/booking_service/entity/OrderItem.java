package com.example.booking_service.entity;


import jakarta.persistence.*;

@Entity
public class OrderItem {

    @Id
    @GeneratedValue
    private Long id;

    private String productName;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;
}
