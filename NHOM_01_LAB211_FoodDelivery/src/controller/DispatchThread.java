package controller;

<<<<<<< HEAD
import model.LockMechanism;
import exception.DoubleAssignmentException;
import exception.DriverOverloadException;
=======
import exception.DoubleAssignmentException;
import exception.DriverOverloadException;
import model.LockMechanism;
>>>>>>> e10ffdc516208d587aa591da018bcddb3663aff9

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

public class DispatchThread extends Thread {
    private CountDownLatch startLatch;
    private CountDownLatch finishLatch;
    private OrderController orderController;
    private String orderId;
    private LockMechanism mechanism;
    private AtomicInteger doubleAssignmentErrors;
    private AtomicInteger driverOverloadErrors;
    private AtomicInteger otherErrors;

    public DispatchThread(CountDownLatch startLatch, CountDownLatch finishLatch,
                          OrderController orderController, String orderId,
                          LockMechanism mechanism, AtomicInteger doubleAssignmentErrors,
                          AtomicInteger driverOverloadErrors, AtomicInteger otherErrors) {
        this.startLatch = startLatch;
        this.finishLatch = finishLatch;
        this.orderController = orderController;
        this.orderId = orderId;
        this.mechanism = mechanism;
        this.doubleAssignmentErrors = doubleAssignmentErrors;
        this.driverOverloadErrors = driverOverloadErrors;
        this.otherErrors = otherErrors;
    }

    @Override
    public void run() {
        try {
<<<<<<< HEAD
            this.startLatch.await(); // True concurrency barrier
=======
            this.startLatch.await();
>>>>>>> e10ffdc516208d587aa591da018bcddb3663aff9
            this.orderController.dispatchPending(this.orderId, this.mechanism);
        } catch (DoubleAssignmentException e) {
            this.doubleAssignmentErrors.incrementAndGet();
        } catch (DriverOverloadException e) {
            this.driverOverloadErrors.incrementAndGet();
        } catch (Exception e) {
            this.otherErrors.incrementAndGet();
        } finally {
            this.finishLatch.countDown();
        }
    }
}
