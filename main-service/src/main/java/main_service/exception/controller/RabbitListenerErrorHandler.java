package main_service.exception.controller;

import org.springframework.amqp.rabbit.support.ListenerExecutionFailedException;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

@Component("rabbitListenerErrorHandler")
public class RabbitListenerErrorHandler implements org.springframework.amqp.rabbit.listener.api.RabbitListenerErrorHandler {

    @Override
    public Object handleError(org.springframework.amqp.core.Message message,
                              Message<?> amqpMessage,
                              ListenerExecutionFailedException e) {
        return null;
    }
}