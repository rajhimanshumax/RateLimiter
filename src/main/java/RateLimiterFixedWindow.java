import lombok.Data;
import model.Request;
import org.springframework.stereotype.Service;

/// Without thread safety, this implementation is not suitable for production use.
@Data
@Service
public class RateLimiterFixedWindow {
    private long reqCount = 0;
    private static final int windowSize = 10;
    private static final int timeLimit = 1000;
    private long lastRequestTime;

    void processRequest(Request request) {
        if (hasLimit()) {
            System.out.println("Request with id: " + request.getId() + " is processed");
            System.out.println("Request Count is"+ reqCount);
        } else {
            throw new RuntimeException("Request with id: " + request.getId() + " is not processed");
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
