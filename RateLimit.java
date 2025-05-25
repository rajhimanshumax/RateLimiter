package ratelimiter;

import lombok.Data;
import ratelimiter.model.Request;

import java.util.Queue;

@Data
public class RateLimit {
    private long reqCount = 0;
    private static final int windowSize = 10;
    private static final int timeLimit = 1000;
    private long lastRequestTime;

    void processRequest(Request request) {
        if (hasLimit()) {
            System.out.println("Request with id: " + request.getId() + " is processed");
        } else {
            System.out.println("Request with id: " + request.getId() + " is rejected");
        }
    }

    boolean hasLimit() {
        if (reqCount > 0) {
            long timeDiff = System.currentTimeMillis() - lastRequestTime;
            if (timeDiff < timeLimit) {
                if (reqCount == windowSize) {
                    return false;
                }
            } else {
                reqCount = 0;
            }
        }
        reqCount++;
        lastRequestTime = System.currentTimeMillis();
        return true;
    }
}
