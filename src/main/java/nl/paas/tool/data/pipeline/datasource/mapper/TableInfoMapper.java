package nl.paas.tool.data.pipeline.datasource.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import nl.paas.tool.data.pipeline.datasource.model.TableInfoVo;
import nl.paas.tool.data.pipeline.datasource.provider.TableInfoProvider;
import org.apache.ibatis.annotations.SelectProvider;

public interface TableInfoMapper extends BaseMapper<TableInfoVo> {

    @SelectProvider(type = TableInfoProvider.class, method = "getSql")
    List<TableInfoVo> getAllTableCount();
}
