package main_service.kafka.consumer;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.listener.*;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import java.time.Duration;

@EnableKafka
@Configuration
public class KafkaListenerConfig {

    @Bean
    public ConcurrentKafkaListenerContainerFactory<Object, Object>
    kafkaListenerContainerFactory(ConsumerFactory<Object, Object> consumerFactory,
                                  ListenerContainerRegistry registry, TaskScheduler scheduler) {
        ConcurrentKafkaListenerContainerFactory<Object, Object> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory);
        KafkaConsumerBackoffManager backOffManager = createBackOffManager(registry, scheduler);
        factory.getContainerProperties()
                .setAckMode(ContainerProperties.AckMode.RECORD);
        factory.setContainerCustomizer(container -> {
            DelayedMessageListenerAdapter<Object, Object> delayedAdapter = wrapWithDelayedMessageListenerAdapter(backOffManager, container);
            delayedAdapter.setDelayForTopic("cover-check", Duration.ofSeconds(10));
            delayedAdapter.setDefaultDelay(Duration.ZERO);
            container.setupMessageListener(delayedAdapter);
        });
        return factory;
    }

    @SuppressWarnings("unchecked")
    private DelayedMessageListenerAdapter<Object, Object> wrapWithDelayedMessageListenerAdapter(
            KafkaConsumerBackoffManager backOffManager,
            ConcurrentMessageListenerContainer<Object, Object> container) {

        return new DelayedMessageListenerAdapter<>((MessageListener<Object, Object>) container.getContainerProperties()
                .getMessageListener(), backOffManager, container.getListenerId());
    }


    private ContainerPartitionPausingBackOffManager createBackOffManager(ListenerContainerRegistry registry, TaskScheduler scheduler) {
        return new ContainerPartitionPausingBackOffManager(registry,
                new ContainerPausingBackOffHandler(new ListenerContainerPauseService(registry, scheduler)));
    }

    @Bean
    public TaskScheduler taskScheduler() {
        return new ThreadPoolTaskScheduler();
    }
}
