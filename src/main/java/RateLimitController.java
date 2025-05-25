import jakarta.servlet.http.HttpServletRequest;
import model.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class RateLimitController {

    Integer reqCount = 0;
    @Autowired
    RateLimiterFixedWindow rateLimiter;

    @GetMapping("/limited")
    public ResponseEntity<Map<String, String>> limitedRequest(HttpServletRequest request) {
        Request rateLimitRequest = new Request();
        rateLimitRequest.setId(reqCount++);
        rateLimiter.processRequest(rateLimitRequest);
        String clientIp = request.getRemoteAddr(); // Extract the IP address
        Map<String, String> response = new HashMap<>();
        response.put("message", "limited");
        response.put("clientIp", clientIp); // Include the IP in the response
        return ResponseEntity.ok(response);
    }

    @GetMapping("/unlimited")
    public ResponseEntity<Map<String, String>> unlimitedRequest(HttpServletRequest request) {
        Request rateLimitRequest = new Request();
        rateLimitRequest.setId(reqCount++);
        rateLimiter.processRequest(rateLimitRequest);
        String clientIp = request.getRemoteAddr(); // Extract the IP address
        Map<String, String> response = new HashMap<>();
        response.put("message", "unlimited");
        response.put("clientIp", clientIp); // Include the IP in the response
        return ResponseEntity.ok(response);
    }
}