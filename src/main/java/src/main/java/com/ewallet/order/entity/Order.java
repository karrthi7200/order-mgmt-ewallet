package src.main.java.com.ewallet.order.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
@Data
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long id;

    @Column(name = "customer_id", nullable = false)
    private Long customerId;

    @Column(name = "product_id", nullable = false)
    private Long productId;

    @Column(name = "merchant_id", nullable = false)
    private Long merchantId;

    @Column(name = "amount", precision = 15, scale = 2, nullable = false)
    private BigDecimal amount;

    @Column(name = "status", length = 50, nullable = false)
    private String status;

    @Column(name = "addBalanceRequired", length = 50, nullable = false)
    private boolean addBalanceRequired;

    @Column(name = "payment_id", length = 50, nullable = false)
    private String paymentId;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
}

