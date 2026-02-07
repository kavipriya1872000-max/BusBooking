package com.example.booking_service.service;

import com.example.booking_service.dto.BookingCreatedEvent;
import com.example.booking_service.dto.BookingRequest;
import com.example.booking_service.dto.BookingResponse;
import com.example.booking_service.entity.BookingStatus;
import com.example.booking_service.entity.Booking;
import com.example.booking_service.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final WebClient webClient;
    private final KafkaTemplate<String,BookingCreatedEvent> kafkaTemplate;

    @Autowired
    public BookingService(
            BookingRepository bookingRepository,
            WebClient webClient,
            KafkaTemplate<String, BookingCreatedEvent> kafkaTemplate) {

        this.bookingRepository = bookingRepository;
        this.webClient = webClient;
        this.kafkaTemplate = kafkaTemplate;
    }



    @KafkaListener(topics = "seat-locked", groupId = "booking-group")
    public void seatLocked(String bookingId){
        System.out.println("seat locked ");
        bookingRepository.updateBookingStatus(bookingId,BookingStatus.CONFIRMED);
    }

    @KafkaListener(topics = "seat-lock-failed", groupId = "booking-group")
    public void seatLockFailed(String bookingId){
        System.out.println("seat locked failed");
        bookingRepository.updateBookingStatus(bookingId,BookingStatus.CANCELLED);
    }

    public BookingResponse createBooking(BookingRequest request) {

        System.out.println("Creating booking for bus: " + request.getBusNumber() +
                ", date: " + request.getTravelDate() +
                ", seats: " + request.getSeatNumbers());

        String bookingId = UUID.randomUUID().toString();

        System.out.println("bookingId "+bookingId);

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

//syschornus Booking using rest
//        webClient.post()
//                .uri("http://seat-service/api/seats/lock")
//                .bodyValue(new SeatLockRequest(
//                        bookingId,
//                        request.getBusNumber(),
//                        request.getTravelDate(),
//                        request.getSeatNumbers()
//                ))
//                .retrieve()
//                .bodyToMono(Void.class)
//                .block();
//
//        Booking booking = new Booking();
//        booking.setBookingId(bookingId);
//        booking.setPnr(generatePNR());
//        booking.setBusNumber(request.getBusNumber());
//        booking.setTravelDate(request.getTravelDate());
//        booking.setEmail(request.getEmail());
//        booking.setPhone(request.getPhone());
//        booking.setStatus(BookingStatus.PENDING);
//        booking.setCreatedAt(LocalDateTime.now());

    }

    private void handleSendEvent(BookingCreatedEvent bookingCreatedEvent) {
        kafkaTemplate.send("booking-created", bookingCreatedEvent);
    }



    private String generatePNR() {
        return "BUS-" + UUID.randomUUID().toString()
                .substring(0, 8)
                .toUpperCase();
    }
}
