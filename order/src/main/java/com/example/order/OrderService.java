package com.example.order;

import com.example.order.event.EventPublisher;
import com.example.order.event.EventUtils;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private EventPublisher eventPublisher;

    @Transactional
    public Order create(Order order) throws Exception {
        // Create data into database
        Order newOrder = orderRepository.save(order);

        // Publish the event
        eventPublisher.fire(EventUtils.createEnrollEvent(newOrder));

        return newOrder;
    }

    public Order get(Integer orderId) {
        Optional<Order> order = orderRepository.findById(orderId);
        if (order.isPresent()) {
            return order.get();
        }
        throw new RuntimeException("Order not found");
    }
}
