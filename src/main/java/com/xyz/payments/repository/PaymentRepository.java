package com.xyz.payments.repository;

import com.xyz.payments.model.Payment;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface PaymentRepository extends ReactiveMongoRepository<Payment, String> {
    Flux<Payment> findByCustomerId(String customerId);
    Flux<Payment> findByStatus(String status);
}
