package src.main.java.com.ewallet.order.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "customers")
@Data
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customer_id")
    private Long id;

    @Column(name = "name", length = 100, nullable = false)
    private String name;

    @Column(name = "email", length = 150, unique = true, nullable = false)
    private String email;

    @Column(name = "status", length = 150, unique = true, nullable = false)
    private boolean active;
}