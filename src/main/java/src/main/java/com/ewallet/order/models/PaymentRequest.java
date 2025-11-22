package src.main.java.com.ewallet.order.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRequest {

    private Long customerId;
    private BigDecimal amount;
    private String paymentId;

}

