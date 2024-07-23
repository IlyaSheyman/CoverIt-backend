package main_service.kafka.consumer;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.TopicPartition;
import org.springframework.kafka.listener.AcknowledgingConsumerAwareMessageListener;
import org.springframework.kafka.listener.KafkaBackoffException;
import org.springframework.kafka.listener.KafkaConsumerBackoffManager;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.kafka.listener.adapter.AbstractDelegatingMessageListenerAdapter;
import org.springframework.kafka.support.Acknowledgment;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Slf4j
public class DelayedMessageListenerAdapter<K, V> extends AbstractDelegatingMessageListenerAdapter<MessageListener<K, V>>
        implements AcknowledgingConsumerAwareMessageListener<K, V> {

    private final Map<String, Duration> delaysPerTopic = new HashMap<>();
    private final String listenerId;
    private Duration defaultDelay;
    private final KafkaConsumerBackoffManager kafkaConsumerBackoffManager;

    public DelayedMessageListenerAdapter(MessageListener<K, V> delegate,
                                         KafkaConsumerBackoffManager kafkaConsumerBackoffManager,
                                         Duration defaultDelay,
                                         String listenerId) {
        super(delegate);
        this.kafkaConsumerBackoffManager = kafkaConsumerBackoffManager;
        this.defaultDelay = defaultDelay;
        this.listenerId = listenerId;
    }

    public void setDelayForTopic(String topic, Duration delay) {
        Objects.requireNonNull(topic, "Topic cannot be null");
        Objects.requireNonNull(delay, "Delay cannot be null");
        log.debug("Setting delay {} for listener id {}", delay, this.listenerId);
        this.delaysPerTopic.put(topic, delay);
    }

    public void setDefaultDelay(Duration delay) {
        Objects.requireNonNull(delay, "Delay cannot be null");
        log.debug("Setting delay {} for listener id {}", delay, this.listenerId);
        this.defaultDelay = delay;
    }

    @Override
    public void onMessage(ConsumerRecord<K, V> consumerRecord, Acknowledgment acknowledgment, Consumer<?, ?> consumer) throws KafkaBackoffException {
        Duration delay = delaysPerTopic.getOrDefault(consumerRecord.topic(), this.defaultDelay);
        long nextExecutionTimestamp = consumerRecord.timestamp() + (delay != null ? delay.toMillis() : 0);
        this.kafkaConsumerBackoffManager.backOffIfNecessary(createContext(consumerRecord, nextExecutionTimestamp, consumer));

        MessageListener<K, V> delegate = getDelegate();
        delegate.onMessage(consumerRecord);
    }

    private KafkaConsumerBackoffManager.Context createContext(ConsumerRecord<K, V> data, long nextExecutionTimestamp, Consumer<?, ?> consumer) {
        return this.kafkaConsumerBackoffManager.createContext(nextExecutionTimestamp,
                this.listenerId,
                new TopicPartition(data.topic(), data.partition()), consumer);
    }
}
