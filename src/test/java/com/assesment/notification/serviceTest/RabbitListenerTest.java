package com.assesment.notification.serviceTest;

import com.assesment.notification.dataTo.EventRequest;
import com.assesment.notification.taskExcecutorConfig.EmailProcessor;
import com.assesment.notification.taskExcecutorConfig.PushNotificationProcessor;
import com.assesment.notification.taskExcecutorConfig.RabbitQueuesListener;
import com.assesment.notification.taskExcecutorConfig.SmsProcessor;
import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.rest.api.v2010.account.MessageCreator;
import com.twilio.type.PhoneNumber;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockedConstruction;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.web.client.RestTemplate;
import org.testcontainers.containers.RabbitMQContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.Duration;
import java.util.Random;
import java.util.concurrent.CompletableFuture;

import static com.twilio.rest.api.v2010.account.Message.*;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
@Testcontainers
public class RabbitListenerTest {

    @Container
    static RabbitMQContainer rabbitMQContainer = new RabbitMQContainer("rabbitmq:3.11-management");

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private RabbitQueuesListener rabbitQueuesListener;

    @MockitoSpyBean
    private EmailProcessor emailProcessor;

    @MockitoSpyBean
    private SmsProcessor smsProcessor;

    @MockitoSpyBean
    private PushNotificationProcessor pushNotificationProcessor;

    @MockitoBean
    private FirebaseMessaging firebaseConfig;

    @MockitoBean
    private JavaMailSender javaMailSender;

    @MockitoBean
    private RestTemplate restTemplate;

    @MockitoBean
    private FirebaseApp firebaseApp;

    @MockitoBean
    private Random random;

    @DynamicPropertySource
    static void rabbitMQProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.rabbitmq.host", rabbitMQContainer::getHost);
        registry.add("spring.rabbitmq.port", rabbitMQContainer::getAmqpPort);
        registry.add("spring.rabbitmq.username", rabbitMQContainer::getAdminUsername);
        registry.add("spring.rabbitmq.password", rabbitMQContainer::getAdminPassword);
    }

    @Test
    @DisplayName("rabbitListenerTest")
    public void rabbitListenerTest() throws FirebaseMessagingException {

        EventRequest eventRequest1 =
                new EventRequest("EMAIL", "https://client-system",
                        "abc@gmail.com", null, null,
                        "Test message", "e1");
        EventRequest eventRequest2 =
                new EventRequest("SMS", "https://client-system",
                        null, "+123456778", null,
                        "Test message", "e1");
        EventRequest eventRequest3 =
                new EventRequest("PUSH", "https://client-system",
                        null, null, "device-1",
                        "Test message", "e1");
        rabbitTemplate.convertAndSend("events", "email", eventRequest1);
        rabbitTemplate.convertAndSend("events", "sms", eventRequest2);
        rabbitTemplate.convertAndSend("events", "push-notification", eventRequest3);

        doNothing().when(javaMailSender).send((SimpleMailMessage) Mockito.any());
        when(restTemplate.exchange(Mockito.anyString(), Mockito.any(HttpMethod.class),
                Mockito.any(), Mockito.any(Class.class))).thenReturn(null);
        when(firebaseConfig.send(Mockito.any())).thenReturn(null);
        await().atMost(Duration.ofSeconds(5)).untilAsserted(() -> {
            verify(emailProcessor, times(1)).processEmail(Mockito.any());
            verify(smsProcessor, times(1)).processSms(Mockito.any());
            verify(pushNotificationProcessor, times(1)).processPushNotification(Mockito.any());
        });
    }

    @Test
    @DisplayName("simulatedFailureTest")
    public void simulatedFailureTest() throws Exception {
        EventRequest eventRequest1 =
                new EventRequest("EMAIL", "https://client-system",
                        "abc@gmail.com", null, null,
                        "Test message", "e1");

        doNothing().when(javaMailSender).send((SimpleMailMessage) Mockito.any());
        when(restTemplate.exchange(Mockito.anyString(), Mockito.any(HttpMethod.class),
                Mockito.any(), Mockito.any(Class.class))).thenReturn(null);
        when(random.nextDouble()).thenReturn(0.02);
        rabbitTemplate.convertAndSend("events", "email", eventRequest1);
        await().atMost(Duration.ofSeconds(5)).untilAsserted(() -> {
            verify(emailProcessor, times(1)).processEmail(Mockito.any());
        });


    }



}
