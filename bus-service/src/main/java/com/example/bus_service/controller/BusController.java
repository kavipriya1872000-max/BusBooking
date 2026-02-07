package com.example.bus_service.controller;


import com.example.bus_service.entity.Bus;
import com.example.bus_service.service.BusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class BusController {

    @Autowired
    private BusService busService;

    @GetMapping("/test")
    public String testService(){
        return "Bus service is up and running!";
    }
    @GetMapping("/searchBus")
    public List<Bus> searchBus(@RequestParam("source") String source,
                               @RequestParam("destination") String destination,
                               @RequestParam("date") String travelDate){
        System.out.println("searchBus  -  "+source + destination + travelDate);
        return busService.searchBus(source,destination,travelDate);
    }

    @PostMapping("/saveBus")
    public List<Bus> createBus(@RequestBody List<Bus> busList){
        return busService.saveBus(busList);
    }
}
