package com.example.order.event;

import com.example.order.Order;
import com.example.order.OutboxEvent;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class EventUtils {
    public static OutboxEvent createEnrollEvent(Order order) {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.convertValue(order, JsonNode.class);

        return new OutboxEvent(
                order.getId(),
                "ORDER_CREATED",
                jsonNode
        );
    }
}
