package com.example.seat_service.config;

import com.example.seat_service.entity.Seat;
import com.example.seat_service.entity.SeatStatus;
import com.example.seat_service.repository.SeatRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;

@Configuration
public class SeatDataLoader {

    @Bean
    CommandLineRunner seedSeats(SeatRepository seatRepository) {
        return args -> {

            String busNumber = "BUS100";
            LocalDate travelDate = LocalDate.of(2025, 1, 10);

            // Avoid duplicate insert on restart
            if (seatRepository.count() > 0) {
                return;
            }

            for (int i = 1; i <= 10; i++) {
                Seat seat = new Seat();
                seat.setBusNumber(busNumber);
                seat.setTravelDate(travelDate);
                seat.setSeatNumber("A" + i);
                seat.setStatus(SeatStatus.AVAILABLE);

                seatRepository.save(seat);
            }

            System.out.println("✅ Seat data loaded successfully");
        };
    }
}
