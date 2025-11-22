package src.main.java.com.ewallet.order.models;

import lombok.Data;

import java.util.List;

@Data
public class OrderRequest {
    private Long customerId;
    private List<OrderItem> items;
    private String paymentId;
    private String sourceSystem;
}

