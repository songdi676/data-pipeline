/*
 * Copyright Debezium Authors.
 *
 * Licensed under the Apache Software License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package nl.paas.tool.data.pipeline.connect.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ConnectConnectorStatus {

    @JsonProperty("state")
    public ConnectorStatus.State status;

    @JsonProperty("worker_id")
    public String workerId;

    @Override
    public String toString() {
        return "ConnectConnectorStatus{" + "status=" + status + ", workerId='" + workerId + '\'' + '}';
    }
}
