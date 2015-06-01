package com.easemob.kafka;

import kafka.server.KafkaServer;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * @author stliu <stliu@apache.org>
 * @date 4/26/15
 */
@Component
public class KafkaStarter implements InitializingBean, DisposableBean {

    @Autowired
    private KafkaServer server;

    @Override
    public void destroy() throws Exception {
        server.shutdown();
    }

    @Override
    public void afterPropertiesSet() throws Exception {

        server.startup();
    }
}
