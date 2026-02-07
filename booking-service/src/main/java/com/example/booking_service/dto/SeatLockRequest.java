package com.example.booking_service.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class SeatLockRequest {

    private String bookingId;
    private String busNumber;
    private String travelDate;
    private List<String> seatNumber;


    public SeatLockRequest(String bookingId, String busNumber, LocalDate travelDate, List<String> seatNumbers) {
        this.bookingId = bookingId;
        this.busNumber = busNumber;
        this.travelDate = travelDate.toString();
        this.seatNumber = seatNumbers;
    }
}
