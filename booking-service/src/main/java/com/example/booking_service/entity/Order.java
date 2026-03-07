package com.example.booking_service.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class Order {


    @Id
    @GeneratedValue
    private int id;

    @OneToMany(mappedBy = "order",cascade = CascadeType.ALL)
    private List<OrderItem> orderItemList;
}
