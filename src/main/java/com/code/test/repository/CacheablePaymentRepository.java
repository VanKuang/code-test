package com.code.test.repository;

import com.code.test.domain.Payment;

import java.io.*;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class CacheablePaymentRepository implements PaymentRepository {

    private Path file;
    private PrintWriter writer;

    private final Map<String, Payment> payments = new ConcurrentHashMap<>();

    public void setFile(final Path file) {
        this.file = file;
    }

    public void init() {
        if (file != null) {
            init(file);
        }
    }

    public void destroy() {
        if (writer != null) {
            writer.close();
        }
    }

    @Override
    public void save(final Payment payment) {
        if (file != null) {
            // PrintWriter is threadsafe
            // keep all records/history
            writer.println(payment.getCcy() + " " + payment.getAmount().toPlainString());
            writer.flush();
        }
        saveToCache(payment);
    }

    @Override
    public Payment get(final String ccy) {
        return payments.get(ccy);
    }

    @Override
    public Set<Payment> getAll() {
        return new HashSet<>(payments.values());
    }

    private void init(final Path path) {
        try {
            final File file = path.toFile();
            if (file.exists()) {
                try (final BufferedReader reader = Files.newBufferedReader(path)) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        final String[] values = line.split(" ");
                        final Payment payment = new Payment(values[0], new BigDecimal(values[1]));
                        saveToCache(payment);
                    }
                }
                System.out.println("Loaded " + file.getAbsolutePath() + ", size=" + payments.size());
            } else {
                // initial the file with empty content
                Files.write(path, new byte[0]);
                System.out.println(file.getName() + " created");
            }
            writer = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));
        } catch (IOException e) {
            System.err.println("Fail to read file");
            e.printStackTrace();
            throw new UncheckedIOException(e);
        }
    }

    private void saveToCache(Payment payment) {
        payments.compute(
                payment.getCcy(),
                (ccy, p) -> new Payment(ccy, p == null ? payment.getAmount() : p.getAmount().add(payment.getAmount())));
    }
}
