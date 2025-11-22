package src.main.java.com.ewallet.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import src.main.java.com.ewallet.order.entity.Order;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    // Custom query methods if needed
    List<Order> findByCustomerId(Long customerId);

    List<Order> findByStatus(String status);
}

