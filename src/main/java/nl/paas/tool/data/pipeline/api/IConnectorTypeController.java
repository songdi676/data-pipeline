package nl.paas.tool.data.pipeline.api;

import java.util.List;

import nl.paas.tool.data.pipeline.model.ConnectorTypeInfo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @description:
 * @param:
 * @return:
 * @author: linyb
 * @date: 2021/1/26
 */
@RequestMapping(value = "/data-pipeline/connector-types")
public interface IConnectorTypeController {

    /**
     * 连接器列表
     *
     * @return
     */
    @GetMapping
    List<ConnectorTypeInfo> getConnectorTypes();

}
