package nl.paas.tool.data.pipeline.connect.api;

import java.util.List;

import io.fabric8.kubernetes.api.model.apps.Deployment;
import nl.paas.tool.data.pipeline.connect.model.ConnectClusterConfig;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @description:
 * @param:
 * @return:
 * @author: linyb
 * @date: 2021/1/26
 */
@RequestMapping(value = "/data-pipeline/connect-clusters")
public interface IConnectClustersController {

    /**
     * 集群列表
     *
     * @return
     */
    @GetMapping(value = "/list")
    List<Deployment> getClusters();

    /**
     * 添加集群
     *
     * @return
     */
    @PostMapping
    Object addClusters(@RequestBody ConnectClusterConfig connectClusterConfig);

    /**
     * 添加集群
     *
     * @return
     */
    @DeleteMapping(value = "{groupId}")
    Object deleteClusters(@PathVariable("groupId") String groupId);

}
