package nl.paas.tool.data.pipeline.connect.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @description: plugin 列表
 */
public class ConnectorPlugin {
    @JsonProperty("class")
    private String className;

    private String type;

    private String version;

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
