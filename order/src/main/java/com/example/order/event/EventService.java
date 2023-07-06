package com.example.order.event;

import com.example.order.OutboxEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Service
public class EventService {

    @Autowired
    private OutBoxRepository outBoxRepository;

    @EventListener
    public void handleOutboxEvent(OutboxEvent event) {
        System.out.println("Got event : " + event);
        UUID uuid = UUID.randomUUID();
        OutBoxEntity entity = new OutBoxEntity(
                uuid,
                event.getAggregateId(),
                event.getEventType(),
                event.getPayload().toString(),
                new Date()
        );
        outBoxRepository.save(entity);
        outBoxRepository.delete(entity);
    }
}
