package src.main.java.com.ewallet.order.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderEstimateResponse {
    private Long customerId;
    private List<OrderItemEstimate> items;
    private BigDecimal subtotal;
    private BigDecimal walletFee;
    private BigDecimal total;
    private String currency;
}

