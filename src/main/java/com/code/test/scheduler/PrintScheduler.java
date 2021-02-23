package com.code.test.scheduler;

import com.code.test.domain.Payment;
import com.code.test.repository.PaymentRepository;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class PrintScheduler {

    private final PaymentRepository paymentRepository;
    private final long initialDelay;
    private final long period;
    private final TimeUnit timeUnit;

    private ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);

    public PrintScheduler(final PaymentRepository paymentRepository) {
        this(paymentRepository, 1, 1, TimeUnit.MINUTES);
    }

    public PrintScheduler(final PaymentRepository paymentRepository,
                          final long initialDelay,
                          final long period,
                          final TimeUnit timeUnit) {
        this.paymentRepository = paymentRepository;
        this.initialDelay = initialDelay;
        this.period = period;
        this.timeUnit = timeUnit;
    }

    public void start() {
        executorService.scheduleAtFixedRate(() -> {
            try {
                final Set<Payment> payments = paymentRepository.getAll();
                final StringBuilder content = new StringBuilder();
                content.append(System.lineSeparator())
                        .append(System.lineSeparator())
                        .append("===========Current payments===========")
                        .append(System.lineSeparator())
                        .append("====Timestamp: ")
                        .append(Instant.now())
                        .append(System.lineSeparator());
                payments.stream()
                        .filter(p -> !p.getAmount().equals(BigDecimal.ZERO))
                        .forEach(p -> content.append(p.getCcy())
                                .append(" ")
                                .append(p.getAmount().toPlainString())
                                .append(System.lineSeparator()));
                content.append("=======================================")
                        .append(System.lineSeparator())
                        .append(System.lineSeparator());
                System.out.println(content.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, initialDelay, period, timeUnit);
    }

    public void stop() {
        executorService.shutdownNow();
    }
}
