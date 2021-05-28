/*
 * Copyright Debezium Authors.
 *
 * Licensed under the Apache Software License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package nl.paas.tool.data.pipeline.connect.api;

import nl.paas.tool.data.pipeline.connect.model.ConnectorInfo;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping(value = "/data-pipeline/connector")
public interface IConnectorsLifecycleController {

    /**
     * @description:创建连接器
     * @date: 2021/3/12
     */
    @PostMapping(value = "/{cluster}/{connector-type-id}")
    Object createConnector(@PathVariable("cluster") String cluster,
        @PathVariable("connector-type-id") String connectorTypeId,
        @RequestBody ConnectorInfo kafkaConnectConfig);

    /**
     * @description:删除连接器
     * @date: 2021/3/12
     */
    @DeleteMapping(value = "/{cluster}/{connectorname}")
    Object deleteConnector(@PathVariable("cluster") String cluster,
        @PathVariable("connectorname") String connectorName);

    /**
     * @description:暂停连接器
     * @date: 2021/3/12
     */
    @PutMapping(value = "/{cluster}/{connectorname}/pause")
    Object pauseConnector(@PathVariable("cluster") String cluster, @PathVariable("connectorname") String connectorName);

    /**
     * @description:恢复暂停的连接器
     * @date: 2021/3/12
     */
    @PutMapping(value = "/{cluster}/{connectorname}/resume")
    Object resumeConnector(@PathVariable("cluster") String cluster,
        @PathVariable("connectorname") String connectorName);

    /**
     * @description:重启连接器
     * @date: 2021/3/12
     */
    @PostMapping(value = "/{cluster}/{connectorname}/restart")
    Object restartConnector(@PathVariable("cluster") String cluster,
        @PathVariable("connectorname") String connectorName);

    /**
     * @description:重启个别任务
     * @date: 2021/3/12
     */
    @PostMapping(value = "/{cluster}/{connectorname}/task/{tasknumber}/restart")
    Object restartTask(@PathVariable("cluster") String cluster, @PathVariable("connectorname") String connectorName,
        @PathVariable("tasknumber") int taskNumber);

}
