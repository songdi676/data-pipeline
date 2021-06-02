package nl.paas.tool.data.pipeline.datasource.api;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import com.github.jinahya.database.metadata.bind.Table;

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
    Object deleteDataSource(@PathVariable("name") String name);

    @GetMapping(value = "{name}/{schema}/table/list")
    HashSet<Table> getTableInfoList(@PathVariable("name") String name,@PathVariable("schema") String schema) throws SQLException;

    @GetMapping(value = "{name}/slot/list")
    List<Map<String, Object>> fetchAllReplicationSlotInfo(@PathVariable("name") String name) throws SQLException;

}
