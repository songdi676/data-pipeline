/*
 * Copyright Debezium Authors.
 *
 * Licensed under the Apache Software License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package nl.paas.tool.data.pipeline.connect.client;

import feign.Contract;
import feign.Feign;
import feign.codec.Decoder;
import feign.codec.Encoder;
import nl.paas.tool.data.pipeline.connect.model.ConnectClusterConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.FeignClientsConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;

@Import(FeignClientsConfiguration.class)
@Component
public class KafkaConnectClientFactory {

    @Autowired
    private Decoder decoder;
    @Autowired
    private Encoder encoder;
    @Autowired
    private Contract contract;

    public KafkaConnectClient getClient(String clusterName) {
        KafkaConnectClient kafkaConnectClient;
        String name = ConnectClusterConfig.getServiceName(clusterName);

        kafkaConnectClient = Feign.builder().encoder(encoder).decoder(decoder).contract(contract)
            .target(KafkaConnectClient.class, "http://" + name + ":8083");

        return kafkaConnectClient;
    }

}
