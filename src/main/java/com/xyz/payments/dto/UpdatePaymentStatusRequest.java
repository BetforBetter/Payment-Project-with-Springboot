package com.xyz.payments.dto;

import com.xyz.payments.model.PaymentStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdatePaymentStatusRequest {

    @NotNull
    private PaymentStatus status;
}
