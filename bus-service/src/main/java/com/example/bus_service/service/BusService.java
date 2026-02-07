package com.example.bus_service.service;

import com.example.bus_service.entity.Bus;
import com.example.bus_service.repository.BusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service

public class BusService {

    @Autowired
    private BusRepository busRepository;

    // Service method
    public List<Bus> searchBus(String source, String destination, String travelDate) {
        LocalDate date = LocalDate.parse(travelDate); // expects "YYYY-MM-DD"
        return busRepository.findBySourceAndDestinationAndDepartureDate(source, destination, date);
    }

    public List<Bus> saveBus(List<Bus> busList) {
        return busRepository.saveAll(busList);
    }
}
