package com.easemob.kafka.health;

import kafka.controller.KafkaController;
import kafka.network.SocketServer;
import kafka.server.*;
import kafka.utils.KafkaScheduler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.AbstractHealthIndicator;
import org.springframework.boot.actuate.health.Health;
import org.springframework.stereotype.Component;

/**
 * @author stliu <stliu@apache.org>
 * @date 4/26/15
 */
@SuppressWarnings("unused")
@Component
public class KafkaHealthIndicator extends AbstractHealthIndicator {
    private static final Logger logger = LoggerFactory.getLogger(KafkaHealthIndicator.class);
    @Autowired
    private KafkaServer server;

    @Override
    protected void doHealthCheck(Health.Builder builder) throws Exception {
        try {
            SocketServer socketServer = server.socketServer();

            KafkaApis apis = server.apis();

            KafkaController controller = server.kafkaController();

//        OffsetManager offsetManager = server.offsetManager();
//        server.replicaManager();
//        server.topicConfigManager();
//
            controllerHealthCheck(builder, controller);

            socketServerHealthCheck(builder, socketServer);
        }catch (Exception e){
            logger.error("Failed to check kafka's health",e);
            builder.down(e);
        }

    }

    private void socketServerHealthCheck(Health.Builder builder, SocketServer socketServer) {
        builder
                .withDetail("broker_id", socketServer.brokerId())

                .withDetail("port", socketServer.port())
        ;
    }

    private void controllerHealthCheck(Health.Builder builder, KafkaController controller) {
        byte state = controller.brokerState().currentState();
        boolean isActive = controller.isActive();
        if (!isActive) {
            builder.down();
        }else{
            builder.up();
        }
        builder.withDetail("is_active", isActive);

        builder.withDetail("epoch", controller.epoch());
//        controller.controllerContext().allLiveReplicas().foreach(p -> builder.withDetail("all_live_replicas" + p.topic(), p.toString()));
        //TODO ============
        controller.controllerContext().allTopics();
        controller.controllerContext().liveBrokers();
        controller.controllerContext().partitionLeadershipInfo();
        //============
        controllerState(state, builder);
    }

    /**
     * see {@link BrokerStates} for more details
     */
    private void controllerState(byte state, Health.Builder builder) {
        switch (state) {
            case 0:
                builder.outOfService().withDetail("state", NotRunning.class.getSimpleName());
                break;
            case 1:
                builder.outOfService().withDetail("state", Starting.class.getSimpleName());
                break;
            case 2:
                builder.outOfService().withDetail("state", RecoveringFromUncleanShutdown.class.getSimpleName());
                break;
            case 3:
                builder.up().withDetail("state", RunningAsBroker.class.getSimpleName());
                break;
            case 4:
                builder.up().withDetail("state", RunningAsController.class.getSimpleName());
                break;
            case 5:
                builder.down().withDetail("state", PendingControlledShutdown.class.getSimpleName());
                break;
            case 6:
                builder.down().withDetail("state", BrokerShuttingDown.class.getSimpleName());
                break;
        }

    }
}
