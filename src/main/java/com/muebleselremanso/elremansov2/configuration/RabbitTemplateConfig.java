package com.muebleselremanso.elremansov2.configuration;

import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitTemplateConfig {

    private final ConnectionFactory connectionFactory;
    private final MessageConverter messageConverter;

    public RabbitTemplateConfig(ConnectionFactory connectionFactory, MessageConverter messageConverter) {
        this.connectionFactory = connectionFactory;
        this.messageConverter = messageConverter;
    }

    @Bean
    public RabbitTemplate rabbitTemplate() {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter);
        return rabbitTemplate;
    }
}