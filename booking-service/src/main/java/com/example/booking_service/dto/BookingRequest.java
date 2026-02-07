package com.example.booking_service.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class BookingRequest {
    private String busNumber;
    private LocalDate travelDate;

    private String email;
    private String phone;

    private List<String> seatNumbers;
}
