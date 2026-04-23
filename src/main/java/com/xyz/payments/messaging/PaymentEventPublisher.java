package com.xyz.payments.messaging;

import com.xyz.payments.dto.PaymentEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentEventPublisher {

    private final KafkaTemplate<String, PaymentEvent> kafkaTemplate;

    @Value("${app.kafka.payment-topic}")
    private String paymentTopic;

    public void publish(PaymentEvent event) {
        kafkaTemplate.send(paymentTopic, event.getPaymentId(), event);
        log.info("Published payment event for paymentId={} status={}", event.getPaymentId(), event.getStatus());
    }
}
