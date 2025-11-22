package src.main.java.com.ewallet.order.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Entity
@Table(name = "merchant")
@Data
public class Merchant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "merchant_id")
    private Long merchant_id;

    @Column(name = "merchant_name", length = 100, nullable = false)
    private String merchant_name;

}