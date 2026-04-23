package com.xyz.payments.controller;

import com.xyz.payments.dto.CreatePaymentRequest;
import com.xyz.payments.dto.PaymentResponse;
import com.xyz.payments.dto.UpdatePaymentStatusRequest;
import com.xyz.payments.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<PaymentResponse> createPayment(@Valid @RequestBody CreatePaymentRequest request) {
        return paymentService.createPayment(request);
    }

    @GetMapping("/{paymentId}")
    public Mono<PaymentResponse> getPaymentById(@PathVariable String paymentId) {
        return paymentService.getPaymentById(paymentId);
    }

    @GetMapping
    public Flux<PaymentResponse> getPayments(@RequestParam(required = false) String customerId) {
        return customerId != null ? paymentService.getPaymentsByCustomer(customerId) : paymentService.getAllPayments();
    }

    @PatchMapping("/{paymentId}/status")
    public Mono<PaymentResponse> updateStatus(@PathVariable String paymentId,
                                              @Valid @RequestBody UpdatePaymentStatusRequest request) {
        return paymentService.updateStatus(paymentId, request.getStatus());
    }
}
