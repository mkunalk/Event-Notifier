package com.assesment.notification.taskExcecutorConfig;

import com.assesment.notification.dataTo.EventRequest;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class PushNotificationProcessor {

    @Async("taskExecutor")
    public void processPushNotification(EventRequest pushNotificationDetails){

        // we have to send notification here



    }

}
