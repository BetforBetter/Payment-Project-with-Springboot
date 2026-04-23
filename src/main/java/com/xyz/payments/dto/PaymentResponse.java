package com.xyz.payments.dto;

import com.xyz.payments.model.PaymentMethod;
import com.xyz.payments.model.PaymentStatus;
import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;
import java.time.Instant;

@Value
@Builder
public class PaymentResponse {
    String id;
    String orderId;
    String customerId;
    BigDecimal amount;
    String currency;
    PaymentMethod paymentMethod;
    PaymentStatus status;
    Instant createdAt;
    Instant updatedAt;
}
