package com.xyz.payments.service;

import com.xyz.payments.dto.CreatePaymentRequest;
import com.xyz.payments.messaging.PaymentEventPublisher;
import com.xyz.payments.model.Payment;
import com.xyz.payments.model.PaymentMethod;
import com.xyz.payments.model.PaymentStatus;
import com.xyz.payments.repository.PaymentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private PaymentEventPublisher paymentEventPublisher;

    private PaymentService paymentService;

    @BeforeEach
    void setUp() {
        paymentService = new PaymentService(paymentRepository, paymentEventPublisher);
    }

    @Test
    void shouldCreatePaymentAndPublishEvent() {
        CreatePaymentRequest request = new CreatePaymentRequest();
        request.setOrderId("ORD-1001");
        request.setCustomerId("CUST-901");
        request.setAmount(new BigDecimal("249.99"));
        request.setCurrency("usd");
        request.setPaymentMethod(PaymentMethod.CARD);

        Payment saved = Payment.builder()
                .id("pay_1")
                .orderId(request.getOrderId())
                .customerId(request.getCustomerId())
                .amount(request.getAmount())
                .currency("USD")
                .paymentMethod(PaymentMethod.CARD)
                .status(PaymentStatus.INITIATED)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();

        when(paymentRepository.save(any(Payment.class))).thenReturn(Mono.just(saved));

        StepVerifier.create(paymentService.createPayment(request))
                .assertNext(response -> {
                    assertEquals("pay_1", response.getId());
                    assertEquals(PaymentStatus.INITIATED, response.getStatus());
                    assertEquals("USD", response.getCurrency());
                })
                .verifyComplete();

        ArgumentCaptor<Payment> paymentCaptor = ArgumentCaptor.forClass(Payment.class);
        verify(paymentRepository, times(1)).save(paymentCaptor.capture());
        verify(paymentEventPublisher, times(1)).publish(any());
        assertEquals("ORD-1001", paymentCaptor.getValue().getOrderId());
    }
}
