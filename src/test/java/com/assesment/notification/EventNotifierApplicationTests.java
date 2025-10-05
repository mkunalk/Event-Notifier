package com.assesment.notification;

import com.assesment.notification.controller.NotificationController;
import com.assesment.notification.taskExcecutorConfig.FirebaseConfig;
import com.assesment.notification.taskExcecutorConfig.RabbitQueuesListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest()
@ActiveProfiles("test")
class EventNotifierApplicationTests {

    @Autowired
    private EventNotifierApplication eventNotifierApplication;

    @MockitoBean
    private FirebaseMessaging firebaseConfig;

    @MockitoBean
    private FirebaseApp firebaseApp;

    @MockitoBean
    private CachingConnectionFactory connectionFactory;

    @MockitoBean
    private SimpleRabbitListenerContainerFactory simpleRabbitListenerContainerFactory;

    @MockitoBean
    private RabbitQueuesListener rabbitQueuesListener;

	@Test
	void contextLoads() {
        assertThat(eventNotifierApplication).isNotNull();
	}

}
