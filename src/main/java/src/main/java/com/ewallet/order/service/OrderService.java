package src.main.java.com.ewallet.order.service;

import src.main.java.com.ewallet.order.entity.Order;
import src.main.java.com.ewallet.order.models.OrderEstimateResponse;
import src.main.java.com.ewallet.order.models.OrderRequest;
import src.main.java.com.ewallet.order.models.OrderResponse;
import src.main.java.com.ewallet.order.models.OrderValidationResponse;

public interface OrderService {
    OrderValidationResponse validateOrder(OrderRequest orderRequest, boolean isMock);
    OrderEstimateResponse estimateOrder(OrderRequest orderRequest, boolean isMock);
    OrderResponse placeOrder(OrderRequest orderRequest, boolean isMock);
}
