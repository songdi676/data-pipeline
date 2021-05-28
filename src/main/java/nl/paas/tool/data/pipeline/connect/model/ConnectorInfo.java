/*
 * Copyright Debezium Authors.
 *
 * Licensed under the Apache Software License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package nl.paas.tool.data.pipeline.connect.model;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ConnectorInfo {

    private String name;
    private Map<String, String> config;
    private List<ConnectorTaskId> tasks;

    public ConnectorInfo() {
    }

    public ConnectorInfo(String name) {
        this.name = name;
    }

    public ConnectorInfo(String name, Map<String, ?> config) {
        this.name = name;
        setConfig(config);
    }

    public List<ConnectorTaskId> getTasks() {
        return tasks;
    }

    public void setTasks(List<ConnectorTaskId> tasks) {
        this.tasks = tasks;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, String> getConfig() {
        return config;
    }

    public void setConfig(Map<String, ?> config) {
        this.config = config.entrySet().stream()
            .collect(Collectors.toMap(Map.Entry::getKey, entry -> String.valueOf(entry.getValue())));
    }
}
