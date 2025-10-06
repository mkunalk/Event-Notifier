package com.assesment.notification.rabbitMqConfig;

import com.rabbitmq.client.ConnectionFactory;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.RetryInterceptorBuilder;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.retry.RejectAndDontRequeueRecoverer;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.retry.interceptor.RetryOperationsInterceptor;

@Configuration
public class RabbitMqConfig {

//    @Value("${rabbitmq.username}")
//    private String username;
//    @Value("${rabbitmq.password}")
//    private String password;
//    @Value("${rabbitmq.host}")
//    private String host;

    @Bean
    @Primary
    public Queue emailQueue(){
        return new Queue("emailQueue");
    }

    @Bean
    public Queue smsQueue(){
        return new Queue("smsQueue");
    }

    @Bean
    public Queue pushNotificationQueue(){
        System.out.println("push queue created");
        return new Queue("pushNotificationQueue");
    }

    @Bean
    public Exchange exchange(){
        return new DirectExchange("events");
    }

    @Bean
    public Binding bindingEmail(Queue emailQueue, Exchange exchange){
        return BindingBuilder
                .bind(emailQueue)
                .to(exchange)
                .with("email")
                .noargs();
    }

    @Bean
    public Binding bindingSMS(Queue smsQueue, Exchange exchange){
        return BindingBuilder
                .bind(smsQueue)
                .to(exchange)
                .with("sms")
                .noargs();
    }

    @Bean
    public Binding bindingPushNotification(Queue pushNotificationQueue, Exchange exchange){
        return BindingBuilder
                .bind(pushNotificationQueue)
                .to(exchange)
                .with("push-notification")
                .noargs();
    }

    @Bean
    public MessageConverter converter(){
        return new Jackson2JsonMessageConverter();
    }
//    @Bean
//    public CachingConnectionFactory  connectionFactory(){
//        CachingConnectionFactory connectionFactorye = new CachingConnectionFactory();
//        connectionFactorye.setHost(host);
//        connectionFactorye.setVirtualHost("/");
//        connectionFactorye.setUsername(username);
//        connectionFactorye.setPassword(password);
//        return connectionFactorye;
//    }

//    @Bean
//    @Profile("!test")
//    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory() {
//        final SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
//        factory.setConnectionFactory(connectionFactory());
//        factory.setMessageConverter(converter());
//        factory.setConcurrentConsumers(3);
//        factory.setMaxConcurrentConsumers(3);
//        factory.setAcknowledgeMode(AcknowledgeMode.AUTO);
//        factory.setAdviceChain(setRetries());
//        factory.setForceStop(false);
//        return factory;
//    }

//    @Bean()
//    public RetryOperationsInterceptor setRetries() {
//        return RetryInterceptorBuilder.stateless()
//                .maxAttempts(4)
//                .backOffOptions(1000,
//                        2,
//                        10000)
//                .recoverer(new RejectAndDontRequeueRecoverer())
//                .build();
//    }
}
