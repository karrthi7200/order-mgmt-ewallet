package src.main.java.com.ewallet.order.service;

import org.springframework.stereotype.Service;
import src.main.java.com.ewallet.order.clients.CustomerClient;
import src.main.java.com.ewallet.order.clients.PaymentClient;
import src.main.java.com.ewallet.order.clients.ProductClient;
import src.main.java.com.ewallet.order.clients.WalletClient;
import src.main.java.com.ewallet.order.constants.ErrorCodes;
import src.main.java.com.ewallet.order.entity.Customer;
import src.main.java.com.ewallet.order.entity.Order;
import src.main.java.com.ewallet.order.entity.Product;
import src.main.java.com.ewallet.order.exceptions.ServiceException;
import src.main.java.com.ewallet.order.models.*;
import src.main.java.com.ewallet.order.repository.OrderRepository;
import src.main.java.com.ewallet.order.util.JsonUtil;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepo;
    private final ProductClient productClient;
    private final CustomerClient customerClient;
    private final WalletClient walletClient;
    private final PaymentClient paymentClient;

    public OrderServiceImpl(OrderRepository orderRepo, ProductClient productClient,
                            CustomerClient customerClient, WalletClient walletClient,
                            PaymentClient paymentClient) {
        this.orderRepo = orderRepo;
        this.productClient = productClient;
        this.customerClient = customerClient;
        this.walletClient = walletClient;
        this.paymentClient = paymentClient;
    }

    public OrderResponse placeOrder(OrderRequest request, boolean isMock) {
        if(isMock) {
            String mockResponse = "{"
                    + "\"orderId\":10001,"
                    + "\"customerId\":101,"
                    + "\"productId\":1012,"
                    + "\"merchantId\":2001,"
                    + "\"amount\":656.50,"
                    + "\"status\":\"COMPLETED\","
                    + "\"createdAt\":\"2025-11-22T16:15:00\","
                    + "\"addBalanceRequired\":false,"
                    + "\"paymentId\":\"PAY12345\","
                    + "\"messages\":["
                    + "\"Customer is active\","
                    + "\"Product Laptop is available\","
                    + "\"Product Mouse is available\","
                    + "\"Payment processed successfully.\""
                    + "]"
                    + "}";
            return JsonUtil.stringToJson(mockResponse, OrderResponse.class);
        }

        List<String> messages = new ArrayList<>();

        // 1. Validate customer
        Customer customer = customerClient.getCustomerById(request.getCustomerId());
        if (customer == null) {
            throw new ServiceException(ErrorCodes.CUSTOMER_NOT_FOUND, "Customer does not exist.");
        }
        if (!customer.isActive()) {
            throw new ServiceException(ErrorCodes.CUSTOMER_NOT_FOUND, "Customer is inactive.");
        }
        messages.add("Customer is active");

        BigDecimal totalAmount = BigDecimal.ZERO;
        Long merchantId = null;

        // 2. Validate products and calculate total
        for (OrderItem item : request.getItems()) {
            Product product = productClient.getProductById(item.getProductId());
            if (product == null) {
                throw new ServiceException(ErrorCodes.PRODUCT_NOT_FOUND,
                        "Product not found: " + item.getProductId());
            }
            if (!"ACTIVE".equalsIgnoreCase(product.getProductStatus())) {
                throw new ServiceException(ErrorCodes.PRODUCT_NOT_FOUND,
                        "Product not available: " + item.getProductId());
            }
            messages.add("Product " + product.getName() + " is available");

            if (merchantId == null) {
                merchantId = product.getMerchantId(); // assume all items belong to same merchant
            }

            BigDecimal lineTotal = product.getPrice()
                    .multiply(BigDecimal.valueOf(item.getQuantity()));
            totalAmount = totalAmount.add(lineTotal);
        }

        // 3. Validate wallet balance
        WalletResponse walletBalance = walletClient.checkBalance(customer.getId());
        boolean addBalanceRequired = false;
        String status;
        String transactionId = null;

        if (walletBalance == null) {
            throw new ServiceException(ErrorCodes.UNKNOWN_ERROR, "Unable to fetch wallet balance.");
        }

        if (walletBalance.getBalanceAmount().compareTo(totalAmount) < 0) {
            addBalanceRequired = true;
            status = "INSUFFICIENT_BALANCE";
            messages.add("Wallet balance is insufficient. Please add funds.");
        } else {
            // 4. Proceed with payment
            PaymentResult paymentResult = paymentClient.initiatePayment(
                    customer.getId(),
                    totalAmount,
                    request.getPaymentId() // use paymentId from request
            );

            if (paymentResult == null) {
                throw new ServiceException(ErrorCodes.UNKNOWN_ERROR, "Payment service unavailable.");
            }

            transactionId = paymentResult.getTransactionId();
            if ("SUCCESS".equalsIgnoreCase(paymentResult.getStatus())) {
                status = "COMPLETED";
                messages.add("Payment processed successfully.");
            } else {
                throw new ServiceException(ErrorCodes.PAYMENT_FAILED,
                        "Payment failed. Try again later.");
            }
        }

        // 5. Persist order entity
        Order order = new Order();
        order.setCustomerId(customer.getId());
        order.setMerchantId(merchantId);
        order.setAmount(totalAmount);
        order.setStatus(status);
        order.setCreatedAt(LocalDateTime.now());
        order.setAddBalanceRequired(addBalanceRequired);
        order.setPaymentId(transactionId);
        order.setPaymentId(request.getPaymentId());

        order = orderRepo.save(order);

        // 6. Map to OrderResponse
        OrderResponse response = new OrderResponse();
        response.setOrderId(order.getId());
        response.setCustomerId(order.getCustomerId());
        response.setMerchantId(order.getMerchantId());
        response.setAmount(order.getAmount());
        response.setStatus(order.getStatus());
        response.setCreatedAt(order.getCreatedAt().toString());
        response.setAddBalanceRequired(order.isAddBalanceRequired());
        response.setPaymentId(order.getPaymentId());
        response.setMessages(messages);

        return response;
    }

    public OrderEstimateResponse estimateOrder(OrderRequest request, boolean isMock) {
        if(isMock) {
            String mockResponse = "{"
                    + "\"customerId\":101,"
                    + "\"items\":["
                    + "{"
                    + "\"productId\":501,"
                    + "\"price\":250.00,"
                    + "\"quantity\":2,"
                    + "\"itemSubtotal\":500.00"
                    + "},"
                    + "{"
                    + "\"productId\":502,"
                    + "\"price\":150.00,"
                    + "\"quantity\":1,"
                    + "\"itemSubtotal\":150.00"
                    + "}"
                    + "],"
                    + "\"subtotal\":650.00,"
                    + "\"walletFee\":6.50,"
                    + "\"total\":656.50,"
                    + "\"currency\":\"INR\""
                    + "}";
            return JsonUtil.stringToJson(mockResponse, OrderEstimateResponse.class);
        }

        BigDecimal subtotal = BigDecimal.ZERO;
        List<OrderItemEstimate> itemEstimates = new ArrayList<>();

        for (OrderItem item : request.getItems()) {
            Product product = productClient.getProductById(item.getProductId());

            BigDecimal itemSubtotal = product.getPrice()
                    .multiply(BigDecimal.valueOf(item.getQuantity()));

            subtotal = subtotal.add(itemSubtotal);

            itemEstimates.add(new OrderItemEstimate(
                    product.getId(),
                    product.getPrice(),
                    item.getQuantity(),
                    itemSubtotal
            ));
        }

        BigDecimal walletFee = subtotal.multiply(BigDecimal.valueOf(0.01)); // 1% fee
        BigDecimal total = subtotal.add(walletFee);

        return new OrderEstimateResponse(
                request.getCustomerId(),
                itemEstimates,
                subtotal,
                walletFee,
                total,
                "INR"
        );
    }

    public OrderValidationResponse validateOrder(OrderRequest request, boolean isMock) {
        Customer customer = customerClient.getCustomerById(request.getCustomerId());
        if (customer == null) {
            throw new ServiceException(ErrorCodes.CUSTOMER_NOT_FOUND, "Customer does not exist.");
        }

        BigDecimal subtotal = BigDecimal.ZERO;

        for (OrderItem item : request.getItems()) {
            Product product = productClient.getProductById(item.getProductId());
            if (product == null) {
                throw new ServiceException(ErrorCodes.PRODUCT_NOT_FOUND,
                        "Product with ID " + item.getProductId() + " does not exist.");
            }

            if (product.getStock() < item.getQuantity()) {
                throw new ServiceException(ErrorCodes.INSUFFICIENT_STOCK,
                        "Insufficient stock for product ID " + item.getProductId());
            }

            subtotal = subtotal.add(product.getPrice()
                    .multiply(BigDecimal.valueOf(item.getQuantity())));
        }

        WalletResponse walletResponse = walletClient.checkBalance(customer.getId());

        if (walletResponse.getBalanceAmount().compareTo(subtotal) < 0) {
            throw new ServiceException(ErrorCodes.INSUFFICIENT_BALANCE, "Insufficient wallet balance.");
        }

        return new OrderValidationResponse(true,
                "Order is valid. Customer exists, products available, and wallet balance sufficient.");
    }





}
