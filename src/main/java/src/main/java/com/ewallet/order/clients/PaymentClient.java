package src.main.java.com.ewallet.order.clients;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import src.main.java.com.ewallet.order.models.PaymentRequest;
import src.main.java.com.ewallet.order.models.PaymentResult;

@Service
public class PaymentClient {

    private final WebClient webClient;

    public PaymentClient(WebClient.Builder builder) {
        this.webClient = builder.baseUrl("http://localhost:8085").build(); // payment-mgmt MS
    }

    public PaymentResult initiatePayment(Long customerId, java.math.BigDecimal amount, String paymentId) {
        return webClient.post()
                .uri("/payments/initiate")
                .bodyValue(new PaymentRequest(customerId, amount, paymentId))
                .retrieve()
                .bodyToMono(PaymentResult.class)
                .block();
    }
}

