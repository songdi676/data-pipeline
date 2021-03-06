/*
 * Copyright Debezium Authors.
 *
 * Licensed under the Apache Software License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package nl.paas.tool.data.pipeline.connect.model;

import java.util.List;

public class ConnectorTypeInfo {

    public String id;
    public String className;
    public String displayName;
    public String version;
    public boolean enabled;
    public List<ConnectorProperty> properties;

    public ConnectorTypeInfo() {
    }

    public ConnectorTypeInfo(String id, String className, String displayName, String version, boolean enabled, List<ConnectorProperty> properties) {
        this.id = id;
        this.className = className;
        this.displayName = displayName;
        this.version = version;
        this.enabled = enabled;
        this.properties = properties;
    }
}
