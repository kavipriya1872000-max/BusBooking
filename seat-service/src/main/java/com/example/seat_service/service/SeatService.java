package com.example.seat_service.service;

import com.example.seat_service.entity.Seat;
import com.example.seat_service.entity.SeatStatus;
import com.example.seat_service.repository.SeatRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class SeatService {

    private final SeatRepository seatRepository;


    @Autowired
    public SeatService(SeatRepository seatRepository) {
        this.seatRepository = seatRepository;
    }

    public List<String> getAvailableSeats(String busNumber, LocalDate travelDate) {
        return seatRepository
                .findByBusNumberAndTravelDateAndStatus(busNumber, travelDate, SeatStatus.AVAILABLE)
                .stream()
                .map(Seat::getSeatNumber)
                .toList();
    }



    @Transactional
    public void confirmSeats(String bookingId) {
        List<Seat> seats = seatRepository.findByBookingId(bookingId);

        for (Seat seat : seats) {
            if (seat.getStatus() != SeatStatus.LOCKED) {
                throw new RuntimeException("Seat " + seat.getSeatNumber() + " is not locked");
            }
            seat.setStatus(SeatStatus.BOOKED);
        }

        seatRepository.saveAll(seats);

    }

    @Transactional
    @KafkaListener(topics = "seat-release", groupId = "seat-service-group")
    public void releaseSeats(String bookingId) {
        List<Seat> seats = seatRepository.findByBookingId(bookingId);

        for (Seat seat : seats) {
            if (seat.getStatus() != SeatStatus.LOCKED) {
                throw new RuntimeException("Seat " + seat.getSeatNumber() + " is not locked");
            }
            seat.setStatus(SeatStatus.AVAILABLE);
            seat.setBookingId(null);
        }

        seatRepository.saveAll(seats);
    }


    @Transactional
    public void lockSeats(String bookingId, String busNumber, LocalDate travelDate, List<String> seatNumber) {
        List<Seat> seats = seatRepository.findByBusNumberAndTravelDateAndSeatNumberIn(
                busNumber,
                travelDate,
                seatNumber
        );

        System.out.println("Seats found for locking: " + seats.toString());

        for (Seat seat : seats) {
            if (seat.getStatus() != SeatStatus.AVAILABLE) {
                throw new RuntimeException("Seat " + seat.getSeatNumber() + " is not available");
            }
            seat.setStatus(SeatStatus.LOCKED);
            seat.setBookingId(bookingId);
        }

        seatRepository.saveAll(seats);
    }
}
