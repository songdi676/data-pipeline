package nl.paas.tool.data.pipeline.connect.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import io.fabric8.kubernetes.api.model.EnvVar;
import io.fabric8.kubernetes.api.model.EnvVarBuilder;
import io.fabric8.kubernetes.api.model.HostPathVolumeSource;
import io.fabric8.kubernetes.api.model.IntOrString;
import io.fabric8.kubernetes.api.model.Service;
import io.fabric8.kubernetes.api.model.ServiceBuilder;
import io.fabric8.kubernetes.api.model.Volume;
import io.fabric8.kubernetes.api.model.VolumeMount;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import io.fabric8.kubernetes.api.model.apps.DeploymentBuilder;
import io.fabric8.kubernetes.api.model.apps.DeploymentList;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;
import nl.paas.tool.data.pipeline.connect.api.IConnectClustersController;
import nl.paas.tool.data.pipeline.config.PipelineConfig;
import nl.paas.tool.data.pipeline.connect.model.ConnectClusterConfig;
import nl.paas.tool.data.pipeline.connect.model.ConnectConfigEnvKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ConnectClustersController implements IConnectClustersController {

    public static final String PAAS_DATA_PIPELINE_CLUSTER_NAME = "paas.data.pipeline.cluster.name";
    public static final String KAFKA_OPTS_VALUE =
        "-javaagent:/kafka/jmx_prometheus_javaagent.jar=8080:/kafka/config.yml";
    public static final int PORT_8083 = 8083;
    public static final int PORT_1976 = 1976;
    public static final int PORT_8080 = 8080;
    @Autowired
    private PipelineConfig pipelineConfig;

    @Override
    public List<Deployment> getClusters() {
        DeploymentList list;
        try (final KubernetesClient client = new DefaultKubernetesClient()) {
            list = client.apps().deployments().inNamespace(pipelineConfig.getNamespace())
                .withLabel(PAAS_DATA_PIPELINE_CLUSTER_NAME).list();
        }
        return list.getItems();
    }

    @Override
    public Object addClusters(ConnectClusterConfig connectClusterConfig) {
        List<Deployment> deployments = this.getClusters();
        for (Deployment d : deployments) {
            if (d.getMetadata().getName().equals(connectClusterConfig.getDeploymentName())) {
                throw new IllegalArgumentException(connectClusterConfig.getGroupId() + "已经存在");
            }
        }
        Collection<Volume> volumes = new HashSet<>();
        Volume localtimeVolume = new Volume();
        localtimeVolume.setName("localtime");
        HostPathVolumeSource hostPathVolumeSource = new HostPathVolumeSource();
        hostPathVolumeSource.setPath("/etc/localtime");
        localtimeVolume.setHostPath(hostPathVolumeSource);
        volumes.add(localtimeVolume);

        VolumeMount localtimeVolumeMount = new VolumeMount();
        localtimeVolumeMount.setName("localtime");
        localtimeVolumeMount.setMountPath("/etc/localtime");

        Deployment deployment =
            new DeploymentBuilder().withNewMetadata().withName(connectClusterConfig.getDeploymentName())
                .addToLabels(PAAS_DATA_PIPELINE_CLUSTER_NAME, connectClusterConfig.getGroupId()).endMetadata()
                .withNewSpec().withReplicas(connectClusterConfig.getReplicas()).withNewTemplate().withNewMetadata()
                .addToLabels("app", "data-pipeline-connect")
                .addToLabels(PAAS_DATA_PIPELINE_CLUSTER_NAME, connectClusterConfig.getGroupId())
                .addToAnnotations("connect.prometheus.io/port", PORT_8080 + "")
                .addToAnnotations("connect.prometheus.io/scrape", "true").endMetadata().withNewSpec().addNewContainer()
                .withName("connect").withImage(pipelineConfig.getImage()).withVolumeMounts(localtimeVolumeMount)
                .withImagePullPolicy("Always").withEnv(convertToEnvVarList(connectClusterConfig)).addNewPort()
                .withContainerPort(PORT_8083).withContainerPort(PORT_1976).withContainerPort(PORT_8080).endPort()
                .endContainer().addAllToVolumes(volumes).endSpec().endTemplate().withNewSelector()
                .addToMatchLabels(PAAS_DATA_PIPELINE_CLUSTER_NAME, connectClusterConfig.getGroupId()).endSelector()
                .endSpec().build();
        Service service =

            new ServiceBuilder().withNewMetadata().withName(connectClusterConfig.getServiceName()).endMetadata()
                .withNewSpec().withSelector(
                Collections.singletonMap(PAAS_DATA_PIPELINE_CLUSTER_NAME, connectClusterConfig.getGroupId()))
                .addNewPort().withName("port").withProtocol("TCP").withPort(PORT_8083)
                .withTargetPort(new IntOrString(PORT_8083)).endPort().addNewPort().withName("port-2")
                .withProtocol("TCP").withPort(PORT_8080).withTargetPort(new IntOrString(PORT_8080)).endPort()
                .addNewPort().withName("port-3").withProtocol("TCP").withPort(PORT_1976)
                .withTargetPort(new IntOrString(PORT_1976)).endPort().endSpec().build();
        try (final KubernetesClient client = new DefaultKubernetesClient()) {
            client.services().inNamespace(pipelineConfig.getNamespace()).create(service);
            client.apps().deployments().inNamespace(pipelineConfig.getNamespace()).createOrReplace(deployment);
        }
        return null;
    }

    public static List<EnvVar> convertToEnvVarList(ConnectClusterConfig connectClusterConfig) {
        List<EnvVar> envVars = new ArrayList<>();
        envVars.add(new EnvVarBuilder().withName(ConnectConfigEnvKey.BOOTSTRAP_SERVERS)
            .withValue(connectClusterConfig.getBootstrapServers()).build());
        envVars.add(
            new EnvVarBuilder().withName(ConnectConfigEnvKey.GROUP_ID).withValue(connectClusterConfig.getGroupId())
                .build());
        envVars.add(new EnvVarBuilder().withName(ConnectConfigEnvKey.STATUS_STORAGE_TOPIC)
            .withValue(connectClusterConfig.getGroupId() + "_status").build());
        envVars.add(new EnvVarBuilder().withName(ConnectConfigEnvKey.OFFSET_STORAGE_TOPIC)
            .withValue(connectClusterConfig.getGroupId() + "_offsets").build());
        envVars.add(new EnvVarBuilder().withName(ConnectConfigEnvKey.CONFIG_STORAGE_TOPIC)
            .withValue(connectClusterConfig.getGroupId() + "_configs").build());
        envVars.add(new EnvVarBuilder().withName(ConnectConfigEnvKey.KAFKA_OPTS).withValue(KAFKA_OPTS_VALUE).build());
        envVars.add(new EnvVarBuilder().withName(ConnectConfigEnvKey.JMX_PORT).withValue(PORT_1976 + "").build());
        return envVars;
    }

    @Override
    public Object deleteClusters(String groupId) {
        try (final KubernetesClient client = new DefaultKubernetesClient()) {
            client.services().inNamespace(pipelineConfig.getNamespace())
                .withName(ConnectClusterConfig.getServiceName(groupId)).delete();
            client.apps().deployments().inNamespace(pipelineConfig.getNamespace())
                .withName(ConnectClusterConfig.getDeploymentName(groupId)).delete();
        }
        return null;
    }
}
