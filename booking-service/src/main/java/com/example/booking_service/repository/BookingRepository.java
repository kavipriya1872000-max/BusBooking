package com.example.booking_service.repository;

import com.example.booking_service.entity.Booking;
import com.example.booking_service.entity.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingRepository extends JpaRepository<Booking,Long> {

    @Modifying
    @Query("""
        UPDATE Booking b
        SET b.status = :status
        WHERE b.bookingId = :bookingId
    """)
    int updateBookingStatus(
            @Param("bookingId") String bookingId,
            @Param("status") BookingStatus status
    );

    Booking findByBookingId(String bookingId);

}
