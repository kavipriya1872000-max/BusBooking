package com.example.seat_service.repository;

import com.example.seat_service.entity.Seat;
import com.example.seat_service.entity.SeatStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface SeatRepository extends JpaRepository<Seat, Long> {

    List<Seat> findByBusNumberAndTravelDateAndStatus(
            String busNumber,
            LocalDate travelDate,
            SeatStatus status
    );

    List<Seat> findByBusNumberAndTravelDateAndSeatNumberIn(
            String busNumber,
            LocalDate travelDate,
            List<String> seatNumbers
    );

    List<Seat> findByBookingId(String bookingId);
}
