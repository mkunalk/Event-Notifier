package com.assesment.notification.rabbitMqConfig;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {

    @Bean
    public Queue emailQueue(){
        return new Queue("emailQueue");
    }

    @Bean
    public Queue smsQueue(){
        return new Queue("smsQueue");
    }

    @Bean
    public Queue pushNotificationQueue(){
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
}
