package com.assesment.notification.taskExcecutorConfig;

import com.assesment.notification.dataTo.CallBackResponse;
import com.assesment.notification.dataTo.EventRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.Random;

@Component
@RequiredArgsConstructor
public class EmailProcessor {

    private final JavaMailSender javaMailSender;
    private final RestTemplate restTemplate;
    private final Random random = new Random();

    @Value("${spring.mail.username}")
    private String username;

    public void processEmail(EventRequest emailDetails){

        try{
            if(random.nextDouble() < 0.1) {
                throw new RuntimeException("Simulated Processing Failure");
            }
            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
            simpleMailMessage.setFrom(username);
            simpleMailMessage.setTo(emailDetails.getRecipient());
            simpleMailMessage.setText(emailDetails.getMessage());
            simpleMailMessage.setSubject("Sprih Assesment");
            javaMailSender.send(simpleMailMessage);
            System.out.println(emailDetails.getEventId() + " EMail Processed By " + Thread.currentThread().getName());

            CallBackResponse response = new CallBackResponse(emailDetails.getEventId(), "COMPLETED",
                    emailDetails.getEventType(), null, new Date());
            HttpEntity<CallBackResponse> entity = new HttpEntity<>(response);
            restTemplate.exchange(emailDetails.getCallbackUrl(), HttpMethod.POST, entity, void.class);
        } catch (Exception e){
            CallBackResponse response = new CallBackResponse(emailDetails.getEventId(),"FAILED",
                    emailDetails.getEventType(), e.getMessage(), new Date());
            HttpEntity<CallBackResponse> entity = new HttpEntity<>(response);
            restTemplate.exchange(emailDetails.getCallbackUrl(), HttpMethod.POST, entity, void.class);
        }

    }
}
