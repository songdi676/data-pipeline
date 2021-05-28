/*
 * Copyright Debezium Authors.
 *
 * Licensed under the Apache Software License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package nl.paas.tool.data.pipeline.connect.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ConnectConnectorStatusResponse {

    public enum Type {
        sink, source
    }

    @JsonProperty("connector")
    public ConnectConnectorStatus connectorStatus;

    public String name;

    @JsonProperty("tasks")
    public List<ConnectTaskStatus> taskStates;

    @JsonProperty("type")
    public Type connectorType;

    @Override
    public String toString() {
        return "ConnectConnectorStatusResponse{" + "name='" + name + '\'' + ", connectorStatus=" + connectorStatus
            + ", taskStates=" + taskStates + ", connectorType=" + connectorType + '}';
    }
}
