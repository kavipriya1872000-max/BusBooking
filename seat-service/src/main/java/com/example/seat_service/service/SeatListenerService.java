package com.example.seat_service.service;

import com.example.seat_service.dto.BookingCreatedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.kafka.annotation.KafkaListener;


@Service
public class SeatListenerService {

    private final KafkaTemplate<String,String> kafkaTemplate;
    private final SeatService seatService;

    @Autowired
    public SeatListenerService(KafkaTemplate<String, String> kafkaTemplate, SeatService seatService) {
        this.kafkaTemplate = kafkaTemplate;
        this.seatService = seatService;
    }

    @KafkaListener(topics = "booking-created" ,groupId = "seat-service-group")
    public void handleBookingCreated(BookingCreatedEvent bookingCreatedEvent){
        System.out.println("Received booking created event for bookingId: " + bookingCreatedEvent.getBookingId());
        try {
            seatService.lockSeats(
                    bookingCreatedEvent.getBookingId(),
                    bookingCreatedEvent.getBusNumber(),
                    bookingCreatedEvent.getTravelDate(),
                    bookingCreatedEvent.getSeatNumbers()
            );
            System.out.println("Seats locked for bookingId: " + bookingCreatedEvent.getBookingId());
            kafkaTemplate.send("seat-locked", bookingCreatedEvent.getBookingId());
        }catch (Exception e){
            System.out.println("Failed to lock seats for bookingId: " + bookingCreatedEvent.getBookingId() + ", reason: " + e.getMessage());
            kafkaTemplate.send("seat-lock-failed", bookingCreatedEvent.getBookingId());
        }
    }

    @KafkaListener(topics = "seat-release", groupId = "seat-service-group")
    public void releaseSeats(String bookingId){
        System.out.println("Releasing seats for bookingId: " + bookingId);
        seatService.releaseSeats(bookingId);
    }
}
