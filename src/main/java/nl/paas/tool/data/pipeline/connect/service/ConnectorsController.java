package nl.paas.tool.data.pipeline.connect.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import nl.paas.tool.data.pipeline.connect.api.IConnectorsController;
import nl.paas.tool.data.pipeline.connect.client.KafkaConnectClient;
import nl.paas.tool.data.pipeline.connect.client.KafkaConnectClientFactory;
import nl.paas.tool.data.pipeline.connect.model.ConfigInfos;
import nl.paas.tool.data.pipeline.connect.model.ConnectConnectorStatusResponse;
import nl.paas.tool.data.pipeline.connect.model.ConnectTaskStatus;
import nl.paas.tool.data.pipeline.connect.model.ConnectorDetail;
import nl.paas.tool.data.pipeline.connect.model.ConnectorInfo;
import nl.paas.tool.data.pipeline.connect.model.ConnectTasksResponse;
import nl.paas.tool.data.pipeline.connect.model.ConnectorPlugin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ConnectorsController implements IConnectorsController {

    @Autowired
    private KafkaConnectClientFactory kafkaConnectClientFactory;

    @Override
    public List<String> listConnectors(String cluster) {
        KafkaConnectClient kafkaConnectClient = kafkaConnectClientFactory.getClient(cluster);
        List<String> connectors = kafkaConnectClient.listConnectors();
        return connectors;
    }

    @Override
    public List<ConnectorDetail> listConnectorsExpand(String cluster) {
        List<ConnectorDetail> result = new ArrayList<>();
        KafkaConnectClient kafkaConnectClient = kafkaConnectClientFactory.getClient(cluster);
        Map<String, ConnectorDetail> connectors = kafkaConnectClient.listConnectorsExpand("status");
        Map<String, ConnectorDetail> connectorsInfo = kafkaConnectClient.listConnectorsExpand("info");
        connectors.forEach((key, status) -> {
            ConnectorDetail connectorDetail = new ConnectorDetail();
            connectorDetail.setName(key);
            connectorDetail.setStatus(status.getStatus());
            connectorDetail.setInfo(connectorsInfo.get(key).getInfo());
            result.add(connectorDetail);
        });
        return result;
    }

    /**
     * ???????????????????????????
     *
     * @param cluster
     * @param connectorname
     * @return
     */
    @Override
    public ConnectorInfo getConnectorInfo(String cluster, String connectorname) {
        KafkaConnectClient kafkaConnectClient = kafkaConnectClientFactory.getClient(cluster);
        ConnectorInfo connectConnectorConfig = kafkaConnectClient.getConnectorInfo(connectorname);
        return connectConnectorConfig;
    }

    /**
     * ???????????????????????????
     *
     * @param cluster
     * @param connectorname
     * @return
     */
    @Override
    public ConnectConnectorStatusResponse getSingleConnectorStatus(String cluster, String connectorname)
        throws IOException {
        KafkaConnectClient kafkaConnectClient = kafkaConnectClientFactory.getClient(cluster);
        ConnectConnectorStatusResponse connectConnectorStatus = kafkaConnectClient.getConnectorStatus(connectorname);
        return connectConnectorStatus;
    }

    @Override
    public Map<String, String> getConnectorConfig(String cluster, String connectorname) throws IOException {
        KafkaConnectClient kafkaConnectClient = kafkaConnectClientFactory.getClient(cluster);
        Map<String, String> connectConnectorStatus = kafkaConnectClient.getConnectorConfig(connectorname);
        return connectConnectorStatus;
    }

    @Override
    public Object putConnectorConfig(String cluster, String connectorname, Boolean forward,
        Map<String, String> connectorConfig) throws IOException {
        KafkaConnectClient kafkaConnectClient = kafkaConnectClientFactory.getClient(cluster);
        Object connectConnectorStatus = kafkaConnectClient.putConnectorConfig(connectorname, forward, connectorConfig);
        return connectConnectorStatus;
    }

    /**
     * ????????????????????????????????????
     *
     * @param
     * @return
     */
    @Override
    public ConnectTasksResponse getConnectorTasks(String cluster, String connectorName) {
        KafkaConnectClient kafkaConnectClient = kafkaConnectClientFactory.getClient(cluster);
        ConnectTasksResponse connectTasksResponse = kafkaConnectClient.getConnectorTasks(connectorName);
        return connectTasksResponse;
    }

    /**
     * ???????????????????????????
     *
     * @param cluster
     * @param connectorname
     * @param taskNumber
     * @return
     */
    @Override
    public ConnectTaskStatus getConnectorTasksStatus(String cluster, String connectorname, int taskNumber) {
        KafkaConnectClient kafkaConnectClient = kafkaConnectClientFactory.getClient(cluster);
        ConnectTaskStatus connectTaskStatus = kafkaConnectClient.getConnectorTaskStatus(connectorname, taskNumber);
        return connectTaskStatus;
    }

    /**
     * ????????????
     *
     * @return
     */
    @Override
    public List<ConnectorPlugin> getConnectorPlugins(String cluster) {
        KafkaConnectClient kafkaConnectClient = kafkaConnectClientFactory.getClient(cluster);
        List<ConnectorPlugin> connectorPlugins = kafkaConnectClient.getConnectorPlugins();
        return connectorPlugins;
    }

    @Override
    public ConfigInfos validateConfigs(String cluster, String connectorType, Map<String, String> connectorConfig) {
        KafkaConnectClient kafkaConnectClient = kafkaConnectClientFactory.getClient(cluster);
        ConfigInfos validation = kafkaConnectClient.validateConfigs(connectorType, connectorConfig);
        return validation;
    }
}
