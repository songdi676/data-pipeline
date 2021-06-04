package nl.paas.tool.data.pipeline.datasource.provider;

import com.baomidou.dynamic.datasource.toolkit.DynamicDataSourceContextHolder;

import nl.paas.tool.data.pipeline.config.DataSourceIdRunner;

public abstract class SqlProvider {
    public String getDataBaseId() {
        String dataSourceName = DynamicDataSourceContextHolder.peek();
        String databaseId = DataSourceIdRunner.DATASOURCEID_MAP.get(dataSourceName);
        return databaseId;
    }

}
