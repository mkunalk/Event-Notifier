package com.assesment.notification.taskExcecutorConfig;

import com.assesment.notification.dataTo.CallBackResponse;
import com.assesment.notification.dataTo.EventRequest;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Date;

@Component
@RequiredArgsConstructor
public class PushNotificationProcessor {
    private final FirebaseMessaging firebaseMessaging;
    private final RestTemplate restTemplate;

    @Async("taskExecutor")
    public void processPushNotification(EventRequest pushNotificationDetails){

        try{
            Message message = Message
                    .builder()
                    .setToken(pushNotificationDetails.getDeviceId())
                    .setNotification(Notification.builder()
                            .setTitle("Sprih Assesment")
                            .setBody(pushNotificationDetails.getMessage())
                            .build())
                    .build();
            firebaseMessaging.send(message);
            CallBackResponse response =
                    new CallBackResponse(pushNotificationDetails.getEventId(), "COMPLETED",
                            pushNotificationDetails.getEventType(), null, new Date());
            HttpEntity<CallBackResponse> entity = new HttpEntity<>(response);
            restTemplate.exchange(pushNotificationDetails.getCallbackUrl(), HttpMethod.POST, entity, void.class);
        } catch (Exception e){
            CallBackResponse response =
                    new CallBackResponse(pushNotificationDetails.getEventId(), "FAILED",
                            pushNotificationDetails.getEventType(), null, new Date());
            HttpEntity<CallBackResponse> entity = new HttpEntity<>(response);
            restTemplate.exchange(pushNotificationDetails.getCallbackUrl(), HttpMethod.POST, entity, void.class);
        }
    }

}
