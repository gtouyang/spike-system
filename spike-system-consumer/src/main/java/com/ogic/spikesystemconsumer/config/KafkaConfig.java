package com.ogic.spikesystemconsumer.config;

import com.ogic.spikesystemconsumer.common.ObjectSerializer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

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

        return new DefaultKafkaProducerFactory<>(configs);
    }

    /**
     * KafkaTemplate
     * @return
     */
    @Bean
    public KafkaTemplate<Object, Object> kafkaTemplate(){
        return new KafkaTemplate<>(producerFactory(), true);
    }

}
