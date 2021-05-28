package nl.paas.tool.data.pipeline.connect.service;

import nl.paas.tool.data.pipeline.connect.api.IConnectorsLifecycleController;
import nl.paas.tool.data.pipeline.connect.client.KafkaConnectClient;
import nl.paas.tool.data.pipeline.connect.client.KafkaConnectClientFactory;
import nl.paas.tool.data.pipeline.connect.model.ConnectorInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ConnectorsLifecycleController implements IConnectorsLifecycleController {
    @Autowired
    private KafkaConnectClientFactory kafkaConnectClientFactory;

    @Override
    public Object createConnector(String cluster, String connectorTypeId,
        ConnectorInfo kafkaConnectConfig) {
        KafkaConnectClient kafkaConnectClient = kafkaConnectClientFactory.getClient(cluster);
        return kafkaConnectClient.createConnector(kafkaConnectConfig);
    }

    @Override
    public Object deleteConnector(String cluster, String connectorName) {
        KafkaConnectClient kafkaConnectClient = kafkaConnectClientFactory.getClient(cluster);
        return kafkaConnectClient.deleteConnector(connectorName);
    }

    @Override
    public Object pauseConnector(String cluster, String connectorName) {
        KafkaConnectClient kafkaConnectClient = kafkaConnectClientFactory.getClient(cluster);
        return kafkaConnectClient.pauseConnector(connectorName);
    }

    @Override
    public Object resumeConnector(String cluster, String connectorName) {
        KafkaConnectClient kafkaConnectClient = kafkaConnectClientFactory.getClient(cluster);
        return kafkaConnectClient.resumeConnector(connectorName);
    }

    @Override
    public Object restartConnector(String cluster, String connectorName) {
        KafkaConnectClient kafkaConnectClient = kafkaConnectClientFactory.getClient(cluster);
        return kafkaConnectClient.restartConnector(connectorName);
    }

    @Override
    public Object restartTask(String cluster, String connectorName, int taskNumber) {
        KafkaConnectClient kafkaConnectClient = kafkaConnectClientFactory.getClient(cluster);
        return kafkaConnectClient.restartConnectorTask(connectorName, taskNumber);
    }
}
