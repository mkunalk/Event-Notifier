package com.assesment.notification.taskExcecutorConfig;

import com.assesment.notification.dataTo.EventRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RabbitQueuesListener {
    private final EmailProcessor emailProcessor;
    private final SmsProcessor smsProcessor;
    private final PushNotificationProcessor pushNotificationProcessor;

    @RabbitListener(queues = "emailQueue")
    @Async("taskExecutorEmail")
    public void receiveEmail(EventRequest emailDetails){
        System.out.println(emailDetails.getEventId() + " Email Received");
        emailProcessor.processEmail(emailDetails);
    }

    @RabbitListener(queues = "smsQueue")
    @Async("taskExecutorSms")
    public void receiveSms(EventRequest smsDetails){
        smsProcessor.processSms(smsDetails);
    }

    @RabbitListener(queues = "pushNotificationQueue")
    @Async("taskExecutorPush")
    public void receivePushNotification(EventRequest pushNotificationDetails){
        System.out.println(pushNotificationDetails.getEventId() + " Push Received");
        pushNotificationProcessor.processPushNotification(pushNotificationDetails);
    }

}
