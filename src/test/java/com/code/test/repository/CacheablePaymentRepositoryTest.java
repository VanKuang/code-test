package com.code.test.repository;

import com.code.test.domain.Payment;
import org.junit.Test;

import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.Assert.assertEquals;

public class CacheablePaymentRepositoryTest {

    @Test
    public void testWithoutSavingToFile() {
        final PaymentRepository repository = new CacheablePaymentRepository();
        saveAndVerify(repository);
    }

    @Test
    public void testWithSavingToFile() throws Exception {
        final Path file = Paths.get("unit_test.txt");
        try {
            final CacheablePaymentRepository repository = new CacheablePaymentRepository();
            repository.setFile(file);
            repository.init();

            saveAndVerify(repository);
            repository.destroy();

            final CacheablePaymentRepository newRepository = new CacheablePaymentRepository();
            newRepository.setFile(file);
            newRepository.init();

            assertEquals(1, repository.getAll().size());
        } finally {
            Files.delete(file);
        }
    }

    private void saveAndVerify(final PaymentRepository repository) {
        repository.save(new Payment("USD", new BigDecimal("100")));

        assertEquals(1, repository.getAll().size());
        assertEquals("USD", repository.get("USD").getCcy());
        assertEquals(new BigDecimal("100"), repository.get("USD").getAmount());

        repository.save(new Payment("USD", new BigDecimal("-100")));

        assertEquals(1, repository.getAll().size());
        assertEquals("USD", repository.get("USD").getCcy());
        assertEquals(new BigDecimal("0"), repository.get("USD").getAmount());
    }

}