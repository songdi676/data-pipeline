package nl.paas.tool.data.pipeline.datasource.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;

import nl.paas.tool.data.pipeline.datasource.model.TableInfoVo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface TableInfoMapper extends BaseMapper<TableInfoVo> {

    @Select.List({
        @Select(value = "select t.table_name,t.num_rows from user_tables t  order by TABLE_NAME asc ", databaseId = "oracle"),
        @Select(value = "select relname as table_name, reltuples as num_rows from pg_class where relkind = 'r' order by TABLE_NAME asc;", databaseId = "postgresql")})
    List<TableInfoVo> getAllTableCount(@Param(Constants.WRAPPER) Wrapper wrapper);
}
