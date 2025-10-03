package com.assesment.notification.dataTo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class EventRequest {

    String eventType;
    String callbackUrl;
    String recipient;
    String phoneNumber;
    String deviceId;
    String message;
    String eventId;
}
