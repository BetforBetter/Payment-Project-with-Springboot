package com.xyz.payments.service;

import com.xyz.payments.dto.CreatePaymentRequest;
import com.xyz.payments.dto.PaymentEvent;
import com.xyz.payments.dto.PaymentResponse;
import com.xyz.payments.exception.PaymentNotFoundException;
import com.xyz.payments.messaging.PaymentEventPublisher;
import com.xyz.payments.model.Payment;
import com.xyz.payments.model.PaymentStatus;
import com.xyz.payments.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final PaymentEventPublisher paymentEventPublisher;

    public Mono<PaymentResponse> createPayment(CreatePaymentRequest request) {
        Payment payment = Payment.builder()
                .orderId(request.getOrderId())
                .customerId(request.getCustomerId())
                .amount(request.getAmount())
                .currency(request.getCurrency().toUpperCase())
                .paymentMethod(request.getPaymentMethod())
                .status(PaymentStatus.INITIATED)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();

        return paymentRepository.save(payment)
                .doOnSuccess(this::publishEvent)
                .map(this::toResponse);
    }

    public Mono<PaymentResponse> getPaymentById(String paymentId) {
        return paymentRepository.findById(paymentId)
                .switchIfEmpty(Mono.error(new PaymentNotFoundException(paymentId)))
                .map(this::toResponse);
    }

    public Flux<PaymentResponse> getPaymentsByCustomer(String customerId) {
        return paymentRepository.findByCustomerId(customerId)
                .map(this::toResponse);
    }

    public Flux<PaymentResponse> getAllPayments() {
        return paymentRepository.findAll()
                .map(this::toResponse);
    }

    public Mono<PaymentResponse> updateStatus(String paymentId, PaymentStatus status) {
        return paymentRepository.findById(paymentId)
                .switchIfEmpty(Mono.error(new PaymentNotFoundException(paymentId)))
                .map(payment -> {
                    payment.setStatus(status);
                    payment.setUpdatedAt(Instant.now());
                    return payment;
                })
                .flatMap(paymentRepository::save)
                .doOnSuccess(this::publishEvent)
                .map(this::toResponse);
    }

    private void publishEvent(Payment payment) {
        PaymentEvent event = PaymentEvent.builder()
                .paymentId(payment.getId())
                .orderId(payment.getOrderId())
                .customerId(payment.getCustomerId())
                .amount(payment.getAmount())
                .currency(payment.getCurrency())
                .status(payment.getStatus())
                .occurredAt(Instant.now())
                .build();
        paymentEventPublisher.publish(event);
    }

    private PaymentResponse toResponse(Payment payment) {
        return PaymentResponse.builder()
                .id(payment.getId())
                .orderId(payment.getOrderId())
                .customerId(payment.getCustomerId())
                .amount(payment.getAmount())
                .currency(payment.getCurrency())
                .paymentMethod(payment.getPaymentMethod())
                .status(payment.getStatus())
                .createdAt(payment.getCreatedAt())
                .updatedAt(payment.getUpdatedAt())
                .build();
    }
}
