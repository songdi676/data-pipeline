package nl.paas.tool.data.pipeline.api;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import nl.paas.tool.data.pipeline.model.ConfigInfos;
import nl.paas.tool.data.pipeline.model.ConnectorDetail;
import nl.paas.tool.data.pipeline.model.ConnectorInfo;
import nl.paas.tool.data.pipeline.model.ConnectConnectorStatusResponse;
import nl.paas.tool.data.pipeline.model.ConnectTaskStatus;
import nl.paas.tool.data.pipeline.model.ConnectTasksResponse;
import nl.paas.tool.data.pipeline.model.ConnectorPlugin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @description:
 * @param:
 * @return:
 * @author: linyb
 * @date: 2021/1/26
 */
@RequestMapping(value = "/data-pipeline/connectors")
public interface IConnectorsController {

    /**
     * 连接器列表
     *
     * @return
     */
    @GetMapping(value = "/{cluster}")
    List<String> listConnectors(@PathVariable("cluster") String cluster);

    /**
     * 连接器列表扩展
     *
     * @return
     */
    @GetMapping(value = "/expand/{cluster}")
    List<ConnectorDetail> listConnectorsExpand(@PathVariable("cluster") String cluster);

    /**
     * 获取指定连接器信息
     *
     * @return
     */
    @GetMapping(value = "/{cluster}/{connectorname}")
    ConnectorInfo getConnectorInfo(@PathVariable("cluster") String cluster,
        @PathVariable("connectorname") String connectorname);

    /**
     * 获取指定连接器状态
     *
     * @return
     */
    @GetMapping(value = "/{cluster}/{connectorname}/status")
    ConnectConnectorStatusResponse getSingleConnectorStatus(@PathVariable("cluster") String cluster,
        @PathVariable("connectorname") String connectorname) throws IOException;

    @GetMapping(value = "/{cluster}/{connectorname}/config")
    Map<String, String> getConnectorConfig(@PathVariable("cluster") String cluster,
        @PathVariable("connectorname") String connectorname) throws IOException;

    @PutMapping(value = "/{cluster}/{connectorname}/config")
    Object putConnectorConfig(@PathVariable("cluster") String cluster,
        @PathVariable("connectorname") String connectorname, @RequestParam("forward") Boolean forward,
        @RequestBody Map<String, String> connectorConfig) throws IOException;

    /**
     * 获取运行连接器的任务列表
     *
     * @return
     */
    @GetMapping(value = "/{cluster}/{connectorname}/tasks")
    ConnectTasksResponse getConnectorTasks(@PathVariable("cluster") String cluster,
        @PathVariable("connectorname") String connectorname);

    /**
     * 获取任务的当前状态
     *
     * @return
     */
    @GetMapping(value = "/{cluster}/{connectorname}/task/{tasknumber}/status")
    ConnectTaskStatus getConnectorTasksStatus(@PathVariable("cluster") String cluster,
        @PathVariable("connectorname") String connectorname, @PathVariable("tasknumber") int taskNumber);

    /**
     * 获取 Connector plugin 列表
     *
     * @return
     */
    @GetMapping(value = "/{cluster}/connector-plugins")
    List<ConnectorPlugin> getConnectorPlugins(@PathVariable("cluster") String cluster);

    /**
     * 获取 Connector plugin 列表
     *
     * @return
     */
    @PutMapping(value = "/{cluster}/connector-plugins/{connectorType}/config/validate")
    ConfigInfos validateConfigs(@PathVariable("cluster") String cluster,
        @PathVariable("connectorType") String connectorType, Map<String, String> connectorConfig);
}
