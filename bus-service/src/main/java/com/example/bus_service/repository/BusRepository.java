package com.example.bus_service.repository;


import com.example.bus_service.entity.Bus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BusRepository extends JpaRepository<Bus, Integer> {

    List<Bus> findBySourceAndDestinationAndDepartureDate(String source, String destination, LocalDate departureDate);

}
