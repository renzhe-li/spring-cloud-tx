package com.demo.tx.payment.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "kafka")
@Data
public class KafkaConfiguration {
    private String bootstrapServers;
    @Value("${kafka.producer.topics}")
    private String[] topics;
    @Value("${kafka.producer.partitions}")
    private int partitions;

}
