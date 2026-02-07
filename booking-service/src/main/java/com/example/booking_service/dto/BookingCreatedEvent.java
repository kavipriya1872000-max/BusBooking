package com.example.booking_service.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class BookingCreatedEvent {
    private String bookingId;
    private String busNumber;
    private LocalDate travelDate;
    private List<String> seatNumbers;
}
