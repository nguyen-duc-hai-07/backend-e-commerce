package org.oplearn.project.event.order;

import org.oplearn.project.dto.response.OrderResponse;

public record OrderCreatedEvent(
    OrderResponse orderResponse,
    String recipientEmail
) {}
