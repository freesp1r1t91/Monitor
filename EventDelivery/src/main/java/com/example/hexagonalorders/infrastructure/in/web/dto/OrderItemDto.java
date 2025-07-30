package com.example.hexagonalorders.infrastructure.in.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.math.BigDecimal;

/**
 * Data Transfer Object for OrderItem data.
 * This class is used to transfer order item data between the adapter layer
 * and the application layer, keeping the domain model isolated.
 */
@Data
public class OrderItemDto {

    @Schema(description = "Product number.", example = "PROD-001", required = true)
    private String productNumber;

    @Schema(description = "Quantity of the product.", example = "2", required = true)
    private Integer quantity;

    @Schema(description = "Unit price of the product.", example = "99.99", required = true)
    private BigDecimal unitPrice;

    public OrderItemDto() {}

    public OrderItemDto(String productNumber, Integer quantity, BigDecimal unitPrice) {
        this.productNumber = productNumber;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }
} 