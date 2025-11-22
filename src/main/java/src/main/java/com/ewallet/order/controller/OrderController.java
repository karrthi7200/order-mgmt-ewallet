package src.main.java.com.ewallet.order.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import src.main.java.com.ewallet.order.entity.Order;
import src.main.java.com.ewallet.order.models.OrderEstimateResponse;
import src.main.java.com.ewallet.order.models.OrderRequest;
import src.main.java.com.ewallet.order.models.OrderResponse;
import src.main.java.com.ewallet.order.models.OrderValidationResponse;
import src.main.java.com.ewallet.order.service.OrderService;

@RestController
@RequestMapping("/order")
@Tag(name = "Orders", description = "Order management APIs")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/submit")
    @Operation(
            summary = "Create a new order",
            description = "Places a new order in the system and returns the created order details."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Order created successfully",
                    content = @Content(schema = @Schema(implementation = Order.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request payload", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public ResponseEntity<OrderResponse> createOrder(
            @Parameter(description = "Order request payload", required = true)
            @RequestBody OrderRequest request, @RequestHeader("isMock") boolean isMock) {
        OrderResponse order = orderService.placeOrder(request, isMock);
        return ResponseEntity.status(HttpStatus.CREATED).body(order);
    }

    @PostMapping("/estimate")
    @Operation(
            summary = "Estimate order cost",
            description = "Provides an estimated cost for the given order request."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order estimate calculated",
                    content = @Content(schema = @Schema(implementation = OrderEstimateResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request payload", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public ResponseEntity<OrderEstimateResponse> estimateOrder(
            @Parameter(description = "Order request payload", required = true)
            @RequestBody OrderRequest request, @RequestHeader("isMock") boolean isMock) {
        OrderEstimateResponse response = orderService.estimateOrder(request, isMock);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/validate")
    @Operation(
            summary = "Validate order",
            description = "Validates the order request against business rules and returns validation status."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order validation result",
                    content = @Content(schema = @Schema(implementation = OrderValidationResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request payload", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public ResponseEntity<OrderValidationResponse> validateOrder(
            @Parameter(description = "Order request payload", required = true)
            @RequestBody OrderRequest request, @RequestHeader("isMock") boolean isMock) {
        OrderValidationResponse response = orderService.validateOrder(request, isMock);
        return ResponseEntity.ok(response);
    }
}