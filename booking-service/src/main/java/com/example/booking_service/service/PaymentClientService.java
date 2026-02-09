package com.example.booking_service.service;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class PaymentClientService {

    @Autowired
    private RestTemplate restTemplate;

    @CircuitBreaker(name = "paymentService", fallbackMethod = "paymentFallback")
    public String makePayment() {

        String url = "http://PAYMENT-SERVICE/pay";

        return restTemplate.getForObject(url, String.class);
    }

    // Fallback method (MUST have same return type)
    public String paymentFallback(Exception ex) {

        return "Payment service is down. Please try later.";
    }
}
