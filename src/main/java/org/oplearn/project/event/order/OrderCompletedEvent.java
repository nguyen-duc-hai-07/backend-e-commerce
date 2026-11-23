package org.oplearn.project.event.order;

import org.oplearn.project.dto.response.OrderItemResponse;

import java.math.BigDecimal;
import java.util.List;

public record OrderCompletedEvent(
    Long orderId,
    String orderCode,
    Long userId,
    BigDecimal totalAmount,
    List<OrderItemResponse> items
) {}
