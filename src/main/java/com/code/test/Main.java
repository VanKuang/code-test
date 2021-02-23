package com.code.test;

import com.code.test.handler.ExitHandler;
import com.code.test.handler.InputHandlerChain;
import com.code.test.handler.InputValidator;
import com.code.test.handler.PaymentHandler;
import com.code.test.repository.CacheablePaymentRepository;
import com.code.test.scheduler.PrintScheduler;

import java.nio.file.Paths;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        System.out.println("Please input the file name to create or load:");

        Dispatcher dispatcher = null;
        final Scanner sc = new Scanner(System.in);
        while (sc.hasNext()) {
            final String input = sc.nextLine();
            if (dispatcher == null) {
                dispatcher = init(input);
                System.out.println("Can start input payment...");
            } else {
                dispatcher.accept(input);
            }
        }
    }

    private static Dispatcher init(String input) {
        final CacheablePaymentRepository paymentRepository = new CacheablePaymentRepository();
        final PrintScheduler printScheduler = new PrintScheduler(paymentRepository);
        paymentRepository.setFile(Paths.get(input));
        paymentRepository.init();

        final Dispatcher dispatcher = new Dispatcher(
                new ExitHandler(),
                new InputHandlerChain(
                        new InputValidator(),
                        new PaymentHandler(paymentRepository)),
                e -> System.err.println(e.getLocalizedMessage()));

        printScheduler.start();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            paymentRepository.destroy();
            printScheduler.stop();
        }));

        return dispatcher;
    }

}
