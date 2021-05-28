package nl.paas.tool.data.pipeline.connect.model;

public class ConnectClusterConfig {
    public static final String CONNECT = "connect-";
    public static final String SERVICE = "service-";
    private String bootstrapServers;
    private String groupId;
    private Integer replicas;

    public String getBootstrapServers() {
        return bootstrapServers;
    }

    public void setBootstrapServers(String bootstrapServers) {
        this.bootstrapServers = bootstrapServers;
    }

    public String getGroupId() {
        return groupId;
    }

    public String getServiceName() {
        return SERVICE + groupId;
    }

    public String getDeploymentName() {
        return CONNECT + groupId;
    }

    public static String getServiceName(String groupId) {
        return SERVICE + groupId;
    }

    public static String getDeploymentName(String groupId) {
        return CONNECT + groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public Integer getReplicas() {
        return replicas;
    }

    public void setReplicas(Integer replicas) {
        this.replicas = replicas;
    }
}
