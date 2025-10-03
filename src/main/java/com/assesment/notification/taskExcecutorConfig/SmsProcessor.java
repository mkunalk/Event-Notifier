package com.assesment.notification.taskExcecutorConfig;

import com.assesment.notification.dataTo.EventRequest;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class SmsProcessor {

    @Async("taskExecutor")
    public void processSms(EventRequest smsDetails){

        // send sms to number


    }
}
