package src.main.java.com.ewallet.order.clients;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import src.main.java.com.ewallet.order.entity.Customer;

@Service
public class CustomerClient {

    private final WebClient webClient;

    public CustomerClient(WebClient.Builder builder) {
        this.webClient = builder.baseUrl("http://localhost:8083").build(); // customer-mgmt MS
    }

    public Customer getCustomerById(Long customerId) {
        return webClient.get()
                .uri("/customers/{id}", customerId)
                .retrieve()
                .bodyToMono(Customer.class)
                .block();
    }
}
