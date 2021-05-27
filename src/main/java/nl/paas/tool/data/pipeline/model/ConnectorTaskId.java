package nl.paas.tool.data.pipeline.model;

public class ConnectorTaskId {
    private String connector;
    private int task;

    public String getConnector() {
        return connector;
    }

    public void setConnector(String connector) {
        this.connector = connector;
    }

    public int getTask() {
        return task;
    }

    public void setTask(int task) {
        this.task = task;
    }
}
