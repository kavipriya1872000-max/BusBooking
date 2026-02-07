package com.example.booking_service.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "bookings")
@Data
public class Booking{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String bookingId;     // UUID

    @Column(unique = true, nullable = false)
    private String pnr;

    private String busNumber;
    private LocalDate travelDate;

    private String email;
    private String phone;

    @Enumerated(EnumType.STRING)
    private BookingStatus status; // PENDING, CONFIRMED, CANCELLED

    private LocalDateTime createdAt;

    private LocalDateTime seatLockedAt;

}
