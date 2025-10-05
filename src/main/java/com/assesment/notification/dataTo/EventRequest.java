package com.assesment.notification.dataTo;

import lombok.*;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class EventRequest {

    @NonNull
    String eventType;

    @NonNull
    String callbackUrl;

    String recipient;

    String phoneNumber;

    String deviceId;

    @NonNull
    String message;

    String eventId;
}
