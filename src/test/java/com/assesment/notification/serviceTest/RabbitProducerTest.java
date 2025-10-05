package com.assesment.notification.serviceTest;

import com.assesment.notification.dataTo.EventRequest;
import com.assesment.notification.dataTo.EventResponse;
import com.assesment.notification.rabbitMqConfig.RabbitMqConfig;
import com.assesment.notification.service.NotificationService;
import com.assesment.notification.service.impl.NotificationServiceImpl;
import com.assesment.notification.taskExcecutorConfig.RabbitQueuesListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.testcontainers.containers.RabbitMQContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@SpringBootTest
@ActiveProfiles("test")
@Testcontainers
public class RabbitProducerTest {

    @Container
    static RabbitMQContainer rabbitMQContainer = new RabbitMQContainer("rabbitmq:3.11-management");

    @Autowired
    NotificationService notificationService;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @MockitoBean
    private FirebaseMessaging firebaseConfig;

    @MockitoBean
    private FirebaseApp firebaseApp;

    @MockitoBean
    private SimpleRabbitListenerContainerFactory simpleRabbitListenerContainerFactory;

    @MockitoBean
    private RabbitQueuesListener rabbitQueuesListener;

    @DynamicPropertySource
    static void rabbitMQProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.rabbitmq.host", rabbitMQContainer::getHost);
        registry.add("spring.rabbitmq.port", rabbitMQContainer::getAmqpPort);
        registry.add("spring.rabbitmq.username", rabbitMQContainer::getAdminUsername);
        registry.add("spring.rabbitmq.password", rabbitMQContainer::getAdminPassword);
    }

    @Test
    @DisplayName("processEventTest1")
    public void processEventTestEmail(){
        EventRequest eventRequest =
                new EventRequest("EMAIL", "https://client-system",
                        "abc@gmail.com", null, null,
                        "Test message", null);
        String queueName = "emailQueue";
        EventResponse response = notificationService.processEvents(eventRequest);
        EventRequest request = (EventRequest) rabbitTemplate.receiveAndConvert(queueName);
        assertEquals(request,eventRequest);

    }

    @Test
    @DisplayName("processEventTest2")
    public void processEventTestSms(){
        EventRequest eventRequest =
                new EventRequest("SMS", "https://client-system",
                        null, "+34431", null,
                        "Test message", null);
        String queueName = "smsQueue";
        EventResponse response = notificationService.processEvents(eventRequest);
        EventRequest request = (EventRequest) rabbitTemplate.receiveAndConvert(queueName);
        assertEquals(request,eventRequest);

    }

    @Test
    @DisplayName("processEventTest3")
    public void processEventTestPush(){
        EventRequest eventRequest =
                new EventRequest("PUSH", "https://client-system",
                        null, null, "device-1",
                        "Test message", null);
        String queueName = "pushNotificationQueue";
        EventResponse response = notificationService.processEvents(eventRequest);
        EventRequest request = (EventRequest) rabbitTemplate.receiveAndConvert(queueName);
        assertEquals(request,eventRequest);

    }

    @Test
    @DisplayName("processEventTestFail")
    public void processEventTestFail(){
        EventRequest eventRequest =
                new EventRequest("xyz", "https://client-system",
                        null, null, "device-1",
                        "Test message", null);
        String queueName1 = "pushNotificationQueue";
        String queueName2 = "emailQueue";
        String queueName3 = "smsQueue";
        EventResponse response = notificationService.processEvents(eventRequest);
        EventRequest request1 = (EventRequest) rabbitTemplate.receiveAndConvert(queueName1);
        EventRequest request2 = (EventRequest) rabbitTemplate.receiveAndConvert(queueName1);
        EventRequest request3 = (EventRequest) rabbitTemplate.receiveAndConvert(queueName1);
        assertNotEquals(request1,eventRequest);
        assertNotEquals(request2,eventRequest);
        assertNotEquals(request3,eventRequest);
        assertEquals("Invalid Parameters",response.getMessage());

    }

    @Test
    @DisplayName("processEventTestConcurrent")
    public void processEventTestConcurrent(){
        EventRequest eventRequest1 =
                new EventRequest("PUSH", "https://client-system",
                        null, null, "device-1",
                        "Test message", null);
        EventRequest eventRequest2 =
                new EventRequest("PUSH", "https://client-system",
                        null, null, "device-2",
                        "Test message 2", null);
        List<EventRequest> list = List.of(eventRequest1, eventRequest2);
        String queueName1 = "pushNotificationQueue";
        try(ExecutorService executorService = Executors.newFixedThreadPool(2)){

            executorService.execute(() -> {
                notificationService.processEvents(eventRequest1);
            });
            executorService.execute(() -> {
                notificationService.processEvents(eventRequest2);
            });
        }

        EventRequest request1 = (EventRequest) rabbitTemplate.receiveAndConvert(queueName1);
        EventRequest request2 = (EventRequest) rabbitTemplate.receiveAndConvert(queueName1);
        assertEquals(request1,eventRequest1);
        assertEquals(request2,eventRequest2);

    }


}
