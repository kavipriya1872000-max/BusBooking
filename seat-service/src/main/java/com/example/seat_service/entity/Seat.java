package com.example.seat_service.entity;

import com.example.seat_service.entity.SeatStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Table(name = "seat",uniqueConstraints = {@UniqueConstraint(columnNames = {"busNumber", "seatNumber", "travelDate"})})
@Data
public class Seat  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String busNumber;
    private LocalDate travelDate;
    private String seatNumber;

    @Enumerated(EnumType.STRING)
    private SeatStatus status; // AVAILABLE, LOCKED, BOOKED

    private String bookingId; // temporary lock
}
