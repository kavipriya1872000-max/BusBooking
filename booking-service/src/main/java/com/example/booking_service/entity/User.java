package com.example.booking_service.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue
    private int id;
    private String name;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Profile profile;


}
