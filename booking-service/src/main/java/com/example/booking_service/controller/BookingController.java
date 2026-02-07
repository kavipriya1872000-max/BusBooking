package com.example.booking_service.controller;

import com.example.booking_service.dto.BookingRequest;
import com.example.booking_service.dto.BookingResponse;
import com.example.booking_service.service.BookingService;
import com.example.booking_service.service.BookingServiceListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/booking")
public class BookingController {


    private final BookingService bookingService;
    private final BookingServiceListener bookingServiceListener;


    @Autowired
    public BookingController(BookingService bookingService, BookingServiceListener bookingServiceListener) {
        this.bookingService = bookingService;
        this.bookingServiceListener = bookingServiceListener;
    }

    @PostMapping("/bookings")
    public ResponseEntity<BookingResponse> createBooking(
            @RequestBody BookingRequest request) {
        System.out.println("BookingRequest - "+request);
//      BookingResponse response = bookingService.createBooking(request);
        BookingResponse response = bookingServiceListener.createBooking(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }


}
