package src.main.java.com.ewallet.order.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemEstimate {
    private Long productId;
    private BigDecimal price;
    private int quantity;
    private BigDecimal itemSubtotal;
}