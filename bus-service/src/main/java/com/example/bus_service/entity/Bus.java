package com.example.bus_service.entity;


import com.example.bus_service.dto.BusStatus;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "buses")
public class Bus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Business identifiers
    private String busNumber;      // TN01AB1234
    private String busName;        // KPN Travels

    // Route details
    private String source;         // Chennai
    private String destination;    // Bangalore

    // Schedule
    private LocalDate departureDate;  // 2025-01-15
    private LocalTime departureTime;
    private LocalTime arrivalTime;

    // Fare
    private Double fare;

    // Capacity (STATIC info)
    private Integer totalSeats;

    // Metadata
    @Enumerated(EnumType.STRING)
    private BusStatus status;      // ACTIVE, CANCELLED
}
