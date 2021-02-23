package com.code.test.repository;

import com.code.test.domain.Payment;

import java.util.Set;

public interface PaymentRepository {

    void save(Payment payment);

    Payment get(String ccy);

    Set<Payment> getAll();

}
