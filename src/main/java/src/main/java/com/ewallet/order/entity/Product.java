package src.main.java.com.ewallet.order.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Entity
@Table(name = "products")
@Data
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long id;

    @Column(name = "name", length = 100, nullable = false)
    private String name;

    @Column(name = "price", precision = 15, scale = 2, nullable = false)
    private BigDecimal price;

    @Column(name = "merchant_id", precision = 15, scale = 2, nullable = false)
    private Long merchantId;

    @Column(name = "product_status", precision = 15, scale = 2, nullable = false)
    private String productStatus;

    @Column(name = "available_stock")
    private int stock;
}