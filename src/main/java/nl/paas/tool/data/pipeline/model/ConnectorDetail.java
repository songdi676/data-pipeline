package nl.paas.tool.data.pipeline.model;

public class ConnectorDetail {
    private String name;
    private ConnectorInfo info;
    private ConnectorStateInfo status;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ConnectorInfo getInfo() {
        return info;
    }

    public void setInfo(ConnectorInfo info) {
        this.info = info;
    }

    public ConnectorStateInfo getStatus() {
        return status;
    }

    public void setStatus(ConnectorStateInfo status) {
        this.status = status;
    }
}
