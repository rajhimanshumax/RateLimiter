import model.Request;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.assertThrows;

class RateLimiterFixedWindowTest {

    @Test
    void testThreadSafety() throws InterruptedException {
        RateLimiterFixedWindow rateLimiter = new RateLimiterFixedWindow();
        int threadCount = 20; // More than the window size
        CountDownLatch latch = new CountDownLatch(threadCount);
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);

        for (int i = 0; i < threadCount; i++) {
            int threadId = i; // Create a final or effectively final variable
            executorService.submit(() -> {
                try {
                    Request request = new Request();
                    request.setId(threadId); // Use the captured variable
                    rateLimiter.processRequest(request);
                } catch (RuntimeException e) {
                    System.out.println("Thread " + threadId + " failed: " + e.getMessage());
                    // Expected for requests exceeding the limit
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        executorService.shutdown();

        // Assert that the rate limiter fails due to thread safety issues
        assertThrows(RuntimeException.class, () -> rateLimiter.processRequest(new Request()));
    }
}