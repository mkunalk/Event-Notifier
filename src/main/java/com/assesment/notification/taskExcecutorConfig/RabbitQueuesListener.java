package com.assesment.notification.taskExcecutorConfig;

import com.assesment.notification.dataTo.EventRequest;
import com.google.firebase.messaging.FirebaseMessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.DependsOn;
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
    public void receiveEmail(EventRequest emailDetails) throws Exception {
        emailProcessor.processEmail(emailDetails);
    }
    @RabbitListener(queues = "smsQueue")
    @Async("taskExecutorSms")
    public void receiveSms(EventRequest smsDetails) throws Exception{
        smsProcessor.processSms(smsDetails);
    }

    @RabbitListener(queues = "pushNotificationQueue")
    @Async("taskExecutorPush")
    public void receivePushNotification(EventRequest pushNotificationDetails) throws FirebaseMessagingException {
        pushNotificationProcessor.processPushNotification(pushNotificationDetails);
    }

}
