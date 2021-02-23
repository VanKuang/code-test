package com.code.test.scheduler;

import com.code.test.repository.PaymentRepository;
import org.junit.Test;

import java.util.Collections;
import java.util.concurrent.TimeUnit;

import static org.mockito.Mockito.*;

public class PrintSchedulerTest {

    @Test
    public void testPrintPayments() {
        final PaymentRepository repository = mock(PaymentRepository.class);
        when(repository.getAll()).thenReturn(Collections.emptySet());

        final PrintScheduler printScheduler = new PrintScheduler(repository, 10, 10, TimeUnit.MILLISECONDS);
        printScheduler.start();

        verify(repository, timeout(1000L).atLeast(5)).getAll();
    }

}