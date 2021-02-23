package com.code.test.domain;

import java.math.BigDecimal;

public class Payment {

    private final String ccy;
    private final BigDecimal amount;

    public Payment(String ccy, BigDecimal amount) {
        this.ccy = ccy;
        this.amount = amount;
    }

    public String getCcy() {
        return ccy;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    @Override
    public String toString() {
        return "Payment{" +
                "ccy='" + ccy + '\'' +
                ", amount=" + amount +
                '}';
    }
}
