package com.easemob.kafka;

import kafka.server.KafkaConfig;
import kafka.server.KafkaServer;
import kafka.server.KafkaServerStartable;
import kafka.utils.SystemTime$;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;

import java.util.Properties;

/**
 * @author stliu <stliu@apache.org>
 * @date 4/26/15
 */
@SuppressWarnings("unused")
@Configuration
public class KafkaConfiguration {
    @Autowired
    private KafkaProperties properties;
    KafkaConfig kafkaConfig;

    @Bean
    @ConditionalOnMissingBean
    public KafkaProperties zookeeperProperties() {
        return new KafkaProperties();
    }

    @Bean
    public KafkaConfig kafkaConfig(){
        if(kafkaConfig == null){
            kafkaConfig = new KafkaConfig(properties.toFlatProperties());
        }
        return kafkaConfig;
    }


    @Bean
    public KafkaServer kafkaServer(){
      return  new KafkaServer(kafkaConfig(), SystemTime$.MODULE$);
    }
}
