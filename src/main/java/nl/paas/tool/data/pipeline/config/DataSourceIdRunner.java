package nl.paas.tool.data.pipeline.config;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import com.baomidou.dynamic.datasource.DynamicRoutingDataSource;

import org.apache.ibatis.mapping.DatabaseIdProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

/**
 * 缓存预热
 *
 * @author SongDi
 * @date 2019/05/15
 */
@Configuration
public class DataSourceIdRunner implements CommandLineRunner {
    @Autowired
    private DataSource dataSource;
    @Autowired
    private DatabaseIdProvider databaseIdProvider;
    public static final Map<String, String> DATASOURCEID_MAP = new HashMap<>();

    @Override
    public void run(String... args) throws Exception {
        DynamicRoutingDataSource ds = (DynamicRoutingDataSource)dataSource;
        for (Map.Entry<String, DataSource> entry : ds.getCurrentDataSources().entrySet()) {
            String databaseId = databaseIdProvider.getDatabaseId(entry.getValue());
            DATASOURCEID_MAP.put(entry.getKey(), databaseId);
        }
    }

}
