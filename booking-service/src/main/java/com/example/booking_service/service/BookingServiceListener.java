package com.example.booking_service.service;

import com.example.booking_service.dto.BookingCreatedEvent;
import com.example.booking_service.dto.BookingRequest;
import com.example.booking_service.dto.BookingResponse;
import com.example.booking_service.entity.Booking;
import com.example.booking_service.entity.BookingStatus;
import com.example.booking_service.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class BookingServiceListener {
    private final KafkaTemplate<String, BookingCreatedEvent> kafkaTemplate;
    private final BookingRepository bookingRepository;


    @Autowired
    public BookingServiceListener(KafkaTemplate<String, BookingCreatedEvent> kafkaTemplate, BookingRepository bookingRepository) {
        this.kafkaTemplate = kafkaTemplate;
        this.bookingRepository = bookingRepository;
    }

    @KafkaListener(topics = "seat-locked", groupId = "booking-group")
    public void seatLocked(String bookingId) {
        System.out.println("seat locked ");
        Booking booking = bookingRepository.findByBookingId(bookingId);
        if(booking.getStatus() != BookingStatus.PENDING){
            return;
        }
        bookingRepository.updateBookingStatus(bookingId, BookingStatus.SEAT_LOCKED);
    }

    @KafkaListener(topics = "seat-lock-failed", groupId = "booking-group")
    public void seatLockFailed(String bookingId) {
        System.out.println("seat locked failed");
        bookingRepository.updateBookingStatus(bookingId, BookingStatus.CANCELLED);
    }

    public BookingResponse createBooking(BookingRequest request) {

        System.out.println("Creating booking for bus: " + request.getBusNumber() +
                ", date: " + request.getTravelDate() +
                ", seats: " + request.getSeatNumbers());

        String bookingId = UUID.randomUUID().toString();

        System.out.println("bookingId " + bookingId);

        Booking booking = new Booking();
        booking.setBookingId(bookingId);
        booking.setPnr(generatePNR());
        booking.setBusNumber(request.getBusNumber());
        booking.setTravelDate(request.getTravelDate());
        booking.setEmail(request.getEmail());
        booking.setPhone(request.getPhone());
        booking.setStatus(BookingStatus.PENDING);
        booking.setCreatedAt(LocalDateTime.now());
        bookingRepository.save(booking);


        BookingCreatedEvent bookingCreatedEvent = new BookingCreatedEvent();
        bookingCreatedEvent.setBookingId(bookingId);
        bookingCreatedEvent.setBusNumber(request.getBusNumber());
        bookingCreatedEvent.setSeatNumbers(request.getSeatNumbers());
        bookingCreatedEvent.setTravelDate(request.getTravelDate());

        handleSendEvent(bookingCreatedEvent);
        System.out.println("After sent the data");

        return new BookingResponse(
                bookingId,
                booking.getPnr(),
                "PENDING",
                "Booking initiated"
        );

    }

    private void handleSendEvent(BookingCreatedEvent bookingCreatedEvent) {
        kafkaTemplate.send("booking-created", bookingCreatedEvent);
    }

    @KafkaListener(topics = "payment-success", groupId = "booking-group")
    public void paymentSuccess(String bookingId) {
        System.out.println("Payment success for bookingId: " + bookingId);
        Booking booking = bookingRepository.findByBookingId(bookingId);
        if (booking.getStatus() == BookingStatus.CONFIRMED) {
            return;
        }
        bookingRepository.updateBookingStatus(
                bookingId,
                BookingStatus.CONFIRMED
        );
    }

    @KafkaListener(topics = "payment-failed", groupId = "booking-group")
    public void paymentFailed(String bookingId) {
        System.out.println("Payment failed for bookingId: " + bookingId);
        Booking booking = bookingRepository.findByBookingId(bookingId);
        if (booking.getStatus() == BookingStatus.CANCELLED) {
            return;
        }
        bookingRepository.updateBookingStatus(
                bookingId,
                BookingStatus.CANCELLED
        );
    }


    private String generatePNR() {
        return "BUS-" + UUID.randomUUID().toString()
                .substring(0, 8)
                .toUpperCase();
    }

}
