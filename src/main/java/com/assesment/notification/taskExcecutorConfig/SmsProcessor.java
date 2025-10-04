package com.assesment.notification.taskExcecutorConfig;

import com.assesment.notification.dataTo.CallBackResponse;
import com.assesment.notification.dataTo.EventRequest;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Date;

@Component
@RequiredArgsConstructor
public class SmsProcessor {

    @Value("${twilio.account.sid}")
    private String accountId;

    @Value("${twilio.auth.token}")
    private String authToken;

    @Value(("${twilio.phone.number}"))
    private String twilioPhoneNumber;

    private final RestTemplate restTemplate;

    @Async("taskExecutor")
    public void processSms(EventRequest smsDetails){
        try {
            Twilio.init(accountId, authToken);
            Message message = Message.creator(
                    new PhoneNumber(smsDetails.getPhoneNumber()),
                    new PhoneNumber(twilioPhoneNumber),
                    smsDetails.getMessage()).create();

            CallBackResponse response =
                    new CallBackResponse(smsDetails.getEventId(),"COMPLETED",
                            smsDetails.getEventType(), null, new Date());
            HttpEntity<CallBackResponse> entity = new HttpEntity<>(response);
            restTemplate.exchange(smsDetails.getCallbackUrl(), HttpMethod.POST, entity, void.class);
        } catch (Exception e){
            CallBackResponse response =
                    new CallBackResponse(smsDetails.getEventId(),"FAILED",
                            smsDetails.getEventType(), null, new Date());
            HttpEntity<CallBackResponse> entity = new HttpEntity<>(response);
            restTemplate.exchange(smsDetails.getCallbackUrl(), HttpMethod.POST, entity, void.class);
        }

    }
}
