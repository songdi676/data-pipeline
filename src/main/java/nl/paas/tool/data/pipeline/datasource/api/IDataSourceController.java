package nl.paas.tool.data.pipeline.datasource.api;

import java.util.List;

import com.baomidou.mybatisplus.generator.config.DataSourceConfig;

import io.fabric8.kubernetes.api.model.apps.Deployment;
import nl.paas.tool.data.pipeline.connect.model.ConnectClusterConfig;
import nl.paas.tool.data.pipeline.datasource.model.DataSourceVo;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 数据源管理接口
 *
 * @author SongDi
 */
@RequestMapping(value = "/data-pipeline/datasource")
public interface IDataSourceController {

    /**
     * 数据源列表
     *
     * @return
     */
    @GetMapping(value = "/list")
    List<DataSourceVo> getDatasource();

    /**
     * 添加数据源
     *
     * @return
     */
    @PostMapping
    Object addDataSource(@RequestBody DataSourceVo dataSourceVo);

    /**
     * 删除数据源
     *
     * @return
     */
    @DeleteMapping(value = "{name}")
    Object deleteDataSource(@PathVariable("dataSourceId") String name);

}
