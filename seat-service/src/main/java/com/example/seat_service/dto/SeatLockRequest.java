package com.example.seat_service.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class SeatLockRequest {

    private String bookingId;
    private String busNumber;
    private LocalDate travelDate;
    private List<String> seatNumber;


}
