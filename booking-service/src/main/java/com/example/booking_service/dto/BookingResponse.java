package com.example.booking_service.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingResponse {

    private String bookingId;
    private String pnr;
    private String status;
    private String message;

}
