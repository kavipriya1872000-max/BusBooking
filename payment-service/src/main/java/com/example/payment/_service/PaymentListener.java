package com.example.payment._service;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class PaymentListener {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public PaymentListener(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @KafkaListener(topics = "payment-processed", groupId = "payment-service-group")
    public void handlePaymentProcessed(String bookingId) {

        System.out.println("Payment processed for bookingId: " + bookingId);

        kafkaTemplate.send("payment-success", bookingId);
    }

    @KafkaListener(topics = "payment-failed", groupId = "payment-service-group")
    public void handlePaymentFailed(String bookingId) {

        System.out.println("Payment failed for bookingId: " + bookingId);

        kafkaTemplate.send("payment-failure", bookingId);
    }
}
