package src.main.java.com.ewallet.order.clients;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import src.main.java.com.ewallet.order.models.WalletResponse;

@Service
public class WalletClient {

    private final WebClient webClient;

    public WalletClient(WebClient.Builder builder) {
        this.webClient = builder.baseUrl("http://localhost:8084").build(); // wallet-mgmt MS
    }

    public WalletResponse checkBalance(Long customerId) {
        return webClient.get()
                .uri("/wallets/{customerId}/check", customerId)
                .retrieve()
                .bodyToMono(WalletResponse.class)
                .block();
    }
}
