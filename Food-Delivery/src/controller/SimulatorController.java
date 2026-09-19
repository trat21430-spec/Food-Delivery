package controller;

import model.LockMechanism;
import repository.OrderRepository;

import java.util.concurrent.CountDownLatch;

public class SimulatorController {

    private final OrderController orderController;
    private final OrderRepository orderRepository;

    public SimulatorController(OrderController orderController,
                               OrderRepository orderRepository) {

        this.orderController = orderController;
        this.orderRepository = orderRepository;
    }

    /**
     * Run concurrent simulation.
     */
    public SimulationResult runSimulation(
            int numberOfOrders,
            LockMechanism mechanism) {

        if (numberOfOrders <= 0) {
            throw new IllegalArgumentException(
                    "Number of orders must be greater than 0"
            );
        }

        if (mechanism == null) {
            mechanism = LockMechanism.NO_LOCK;
        }

        CountDownLatch startLatch =
                new CountDownLatch(1);

        CountDownLatch finishLatch =
                new CountDownLatch(numberOfOrders);

        long startTime = System.currentTimeMillis();

        // Customer threads
        for (int i = 0; i < numberOfOrders; i++) {

            final int index = i;

            Thread customerThread = new Thread(() -> {

                try {

                    startLatch.await();

                    String orderId =
                            "SIM-" + index;

                    orderController.dispatchDriver(
                            orderId,
                            mechanism
                    );

                } catch (InterruptedException e) {

                    Thread.currentThread().interrupt();

                } finally {

                    finishLatch.countDown();
                }
            });

            customerThread.start();
        }

        // Start all threads simultaneously
        startLatch.countDown();

        try {

            finishLatch.await();

        } catch (InterruptedException e) {

            Thread.currentThread().interrupt();
        }

        long endTime =
                System.currentTimeMillis();

        long executionTime =
                endTime - startTime;

        int doubleAssignments =
                orderRepository.detectDoubleAssignments();

        int oversell =
                orderRepository.detectOversellItems();

        int driverOverload =
                orderRepository.detectDriverOverload();

        return new SimulationResult(
                mechanism,
                numberOfOrders,
                executionTime,
                doubleAssignments,
                oversell,
                driverOverload
        );
    }
}