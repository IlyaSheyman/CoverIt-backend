package main_service.rabbitmq;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;


@Configuration
public class RabbitConfig {

    @Bean
    public Queue queue() {
        return new Queue("cover.check.queue", true);
    }

    @Bean
    public CustomExchange exchange() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-delayed-type", "topic");
        return new CustomExchange("cover.exchange", "x-delayed-message", true, false, args);
    }

    @Bean
    public Binding binding(Queue queue, CustomExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with("cover.check.queue").noargs();
    }
}
