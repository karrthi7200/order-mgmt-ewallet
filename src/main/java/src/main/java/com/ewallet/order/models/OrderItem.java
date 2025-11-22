package src.main.java.com.ewallet.order.models;

import lombok.Data;

@Data
public class OrderItem {
    private Long productId;
    private int quantity;
}

