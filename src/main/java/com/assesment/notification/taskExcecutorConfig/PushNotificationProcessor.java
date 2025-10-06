package com.assesment.notification.taskExcecutorConfig;

import com.assesment.notification.dataTo.CallBackResponse;
import com.assesment.notification.dataTo.EventRequest;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.Random;

@Component
@RequiredArgsConstructor
public class PushNotificationProcessor {
    private final FirebaseMessaging firebaseMessaging;
    private final RestTemplate restTemplate;
    private final Random random;


    public void processPushNotification(EventRequest pushNotificationDetails) throws FirebaseMessagingException {
        try{
            if(random.nextDouble() < 0.1) {
                throw new RuntimeException("Simulated Processing Failure");
            }
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
                            pushNotificationDetails.getEventType(), e.getMessage(), new Date());
            HttpEntity<CallBackResponse> entity = new HttpEntity<>(response);
            restTemplate.exchange(pushNotificationDetails.getCallbackUrl(), HttpMethod.POST, entity, void.class);
            throw e;
        }
    }

}
