package com.xyz.payments.dto;
import com.xyz.payments.model.PaymentStatus;
import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;
import java.time.Instant;

@Value
@Builder
public class PaymentEvent {
    String paymentId;
    String orderId;
    String customerId;
    BigDecimal amount;
    String currency;
    PaymentStatus status;
    Instant occurredAt;
}
