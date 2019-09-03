package com.ogic.spikesystemorderservice.config;

import com.ogic.spikesystemorderservice.common.ObjectDeserializer;
import com.ogic.spikesystemorderservice.common.ObjectSerializer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.listener.ContainerProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * @author ogic
 */
@Configuration
public class KafkaConfig {

    @Value("${kafka.producer.bootstrapServers}")
    private String producerBootstrapServers;

    @Value("${kafka.producer.retries}")
    private String producerRetries;

    @Value("${kafka.producer.batchSize}")
    private String producerBatchSize;

    @Value("${kafka.producer.lingerMs}")
    private String producerLingerMs;

    @Value("${kafka.producer.bufferMemory}")
    private String producerBufferMemory;

    @Value("${kafka.consumer.bootstrapServers}")
    private String consumerBootstrapServers;

    @Value("${kafka.consumer.groupId}")
    private String consumerGroupId;

    @Value("${kafka.consumer.enableAutoCommit}")
    private String consumerEnableAutoCommit;

    @Value("${kafka.consumer.autoCommitIntervalMs}")
    private String consumerAutoCommitIntervalMs;

    @Value("${kafka.consumer.sessionTimeoutMs}")
    private String consumerSessionTimeoutMs;

    @Value("${kafka.consumer.maxPollRecords}")
    private String consumerMaxPollRecords;

    @Value("${kafka.consumer.autoOffsetReset}")
    private String consumerAutoOffsetReset;

    /**
     * ProducerFactory
     * @return
     */
    @Bean
    public ProducerFactory<Object, Object> producerFactory(){
        Map<String, Object> configs = new HashMap<>(10);
        configs.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, producerBootstrapServers);
        configs.put(ProducerConfig.RETRIES_CONFIG, producerRetries);
        configs.put(ProducerConfig.BATCH_SIZE_CONFIG, producerBatchSize);
        configs.put(ProducerConfig.LINGER_MS_CONFIG, producerLingerMs);
        configs.put(ProducerConfig.BUFFER_MEMORY_CONFIG, producerBufferMemory);
        configs.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configs.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, ObjectSerializer.class);

        return new DefaultKafkaProducerFactory<Object, Object>(configs);
    }

    /**
     * KafkaTemplate
     * @return
     */
    @Bean
    public KafkaTemplate<Object, Object> kafkaTemplate(){
        return new KafkaTemplate<>(producerFactory(), true);
    }

    /**
     * ConsumerFactory
     * @return
     */
    @Bean
    public ConsumerFactory<Object, Object> consumerFactory(){
        Map<String, Object> configs = new HashMap<>(10);
        configs.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, consumerBootstrapServers);
        configs.put(ConsumerConfig.GROUP_ID_CONFIG, consumerGroupId);
        configs.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, consumerEnableAutoCommit);
        configs.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, consumerAutoCommitIntervalMs);
        configs.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, consumerSessionTimeoutMs);
        configs.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, consumerMaxPollRecords);
        configs.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, consumerAutoOffsetReset);
        configs.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        configs.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ObjectDeserializer.class);

        return new DefaultKafkaConsumerFactory<>(configs);
    }

    /**
     * 添加KafkaListenerContainerFactory，用于批量消费消息
     * @return
     */
    @Bean
    public KafkaListenerContainerFactory<?> batchContainerFactory(){
        ConcurrentKafkaListenerContainerFactory<Object, Object> containerFactory = new ConcurrentKafkaListenerContainerFactory<>();
        containerFactory.setConsumerFactory(consumerFactory());
        containerFactory.setConcurrency(4);
        containerFactory.setBatchListener(true);
        containerFactory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL_IMMEDIATE);

        return containerFactory;
    }
}
