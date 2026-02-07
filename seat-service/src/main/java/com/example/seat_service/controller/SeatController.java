package com.example.seat_service.controller;

import com.example.seat_service.dto.SeatLockRequest;
import com.example.seat_service.service.SeatService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/seats")
public class SeatController {

    private final SeatService seatService;

    public SeatController(SeatService seatService) {
        this.seatService = seatService;
    }

    @GetMapping("/available")
    public List<String> getAvailableSeats(
            @RequestParam String busNumber,
            @RequestParam LocalDate travelDate) {
        return seatService.getAvailableSeats(busNumber, travelDate);
    }

    @PostMapping("/lock")
    public ResponseEntity<?> lockSeats(@RequestBody SeatLockRequest request) {
        System.out.println("Locking seats: " + request);
        seatService.lockSeats(
                request.getBookingId(),
                request.getBusNumber(),
                request.getTravelDate(),
                request.getSeatNumber()
        );
        return ResponseEntity.ok("Seats locked");
    }

    @PostMapping("/confirm")
    public ResponseEntity<?> confirm(@RequestParam String bookingId) {
        seatService.confirmSeats(bookingId);
        return ResponseEntity.ok("Seats booked");
    }

    @PostMapping("/release")
    public ResponseEntity<?> release(@RequestParam String bookingId) {
        seatService.releaseSeats(bookingId);
        return ResponseEntity.ok("Seats released");
    }
}
