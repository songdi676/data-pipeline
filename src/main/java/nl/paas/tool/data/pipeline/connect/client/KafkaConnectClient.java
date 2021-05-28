/*
 * Copyright Debezium Authors.
 *
 * Licensed under the Apache Software License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package nl.paas.tool.data.pipeline.connect.client;

import java.util.List;
import java.util.Map;

import nl.paas.tool.data.pipeline.connect.model.ConfigInfos;
import nl.paas.tool.data.pipeline.connect.model.ConnectConnectorStatusResponse;
import nl.paas.tool.data.pipeline.connect.model.ConnectTaskStatus;
import nl.paas.tool.data.pipeline.connect.model.ConnectorInfo;
import nl.paas.tool.data.pipeline.connect.model.ConnectorDetail;
import nl.paas.tool.data.pipeline.connect.model.ConnectTasksResponse;
import nl.paas.tool.data.pipeline.connect.model.ConnectorPlugin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

public interface KafkaConnectClient {

    @PostMapping(value = "/connectors", produces = "application/json")
    String createConnector(ConnectorInfo configuration);

    @GetMapping(value = "/connectors", produces = "application/json")
    List<String> listConnectors();

    @GetMapping(value = "/connectors", produces = "application/json")
    Map<String, ConnectorDetail> listConnectorsExpand(@RequestParam("expand") String expand);

    @GetMapping(value = "/connectors/{connector-name}", produces = "application/json")
    ConnectorInfo getConnectorInfo(@PathVariable("connector-name") String connectorName);

    @GetMapping(value = "/connectors/{connector-name}/status", produces = "application/json")
    ConnectConnectorStatusResponse getConnectorStatus(@PathVariable("connector-name") String connectorName);

    @GetMapping(value = "/connectors/{connector-name}/config", produces = "application/json")
    Map<String, String> getConnectorConfig(@PathVariable("connector-name") String connectorName);

    @PutMapping(value = "/connectors/{connector-name}/config", produces = "application/json")
    Object putConnectorConfig(@PathVariable("connector-name") String connectorName,
        @RequestParam("forward") Boolean forward, @RequestBody Map<String, String> connectorConfig);

    @DeleteMapping(value = "/connectors/{connector-name}", produces = "application/json")
    Object deleteConnector(@PathVariable("connector-name") String connectorName);

    @PutMapping(value = "/connectors/{connector-name}/pause", produces = "application/json")
    Object pauseConnector(@PathVariable("connector-name") String connectorName);

    @PutMapping(value = "/connectors/{connector-name}/resume", produces = "application/json")
    Object resumeConnector(@PathVariable("connector-name") String connectorName);

    @PostMapping(value = "/connectors/{connector-name}/restart", consumes = "application/json", produces = "application/json")
    Object restartConnector(@PathVariable("connector-name") String connectorName);

    @PostMapping(value = "/connectors/{connector-name}/tasks/{task-number}/restart", consumes = "application/json", produces = "application/json")
    Object restartConnectorTask(@PathVariable("connector-name") String connectorName,
        @PathVariable("task-number") int taskNumber);

    @PostMapping(value = "/connectors/{connector-name}/tasks", consumes = "application/json", produces = "application/json")
    ConnectTasksResponse getConnectorTasks(@PathVariable("connector-name") String connectorName);

    @PostMapping(value = "/connectors/{connector-name}/tasks/{task-number}/status", consumes = "application/json", produces = "application/json")
    ConnectTaskStatus getConnectorTaskStatus(@PathVariable("connector-name") String connectorName,
        @PathVariable("task-number") int taskNumber);

    @GetMapping(value = "/connector-plugins", produces = "application/json")
    List<ConnectorPlugin> getConnectorPlugins();

    @PutMapping(value = "/connector-plugins/{connectorType}/config/validate", produces = "application/json")
    ConfigInfos validateConfigs(@PathVariable("connectorType") String connType,
        @RequestBody Map<String, String> connectorConfig);
}
