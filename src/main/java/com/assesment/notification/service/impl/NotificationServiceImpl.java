package com.assesment.notification.service.impl;

import com.assesment.notification.dataTo.EventRequest;
import com.assesment.notification.dataTo.EventResponse;
import com.assesment.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {
    private final RabbitTemplate template;
    private final AtomicLong eventNumber = new AtomicLong(0);

    @Override
    public EventResponse processEvents(EventRequest request) {
        String exchangeName = "events";
        String message = "Event accepted for processing";
        String eventId = "e"+ eventNumber.incrementAndGet();
        switch (request.getEventType()) {
            case "EMAIL" -> {
                String emailRoutingKey = "email";
                request.setEventId(eventId);
                template.convertAndSend(exchangeName, emailRoutingKey, request);
            }
            case "SMS" -> {
                String smsRoutingKey = "sms";
                request.setEventId(eventId);
                template.convertAndSend(exchangeName, smsRoutingKey, request);
            }
            case "PUSH" -> {
                request.setEventId(eventId);
                String pushNotificationRoutingKey = "push-notification";
                template.convertAndSend(exchangeName, pushNotificationRoutingKey, request);
            }
            default -> {
                return new EventResponse(null, "Invalid Parameters");
            }
        }

        return new EventResponse(eventId, message);
    }
}
