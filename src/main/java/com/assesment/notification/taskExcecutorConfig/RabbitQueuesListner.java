package com.assesment.notification.taskExcecutorConfig;

import com.assesment.notification.dataTo.EventRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RabbitQueuesListner {
    private final EmailProcessor emailProcessor;
    private final SmsProcessor smsProcessor;
    private final PushNotificationProcessor pushNotificationProcessor;

    @RabbitListener(queues = "emailQueue")
    public void receiveEmail(EventRequest emailDetails){
        emailProcessor.processEmail(emailDetails);
    }

    @RabbitListener(queues = "smsQueue")
    public void receiveSms(EventRequest smsDetails){
        smsProcessor.processSms(smsDetails);
    }

    @RabbitListener(queues = "pushNotificationQueue")
    public void receivePushNotification(EventRequest pushNotificationDetails){
        pushNotificationProcessor.processPushNotification(pushNotificationDetails);
    }

}
