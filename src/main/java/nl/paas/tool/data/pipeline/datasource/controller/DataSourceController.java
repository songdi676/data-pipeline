package nl.paas.tool.data.pipeline.datasource.controller;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import javax.sql.DataSource;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.jinahya.database.metadata.bind.Context;
import com.github.jinahya.database.metadata.bind.Schema;
import com.github.jinahya.database.metadata.bind.Table;

import io.fabric8.kubernetes.api.model.ConfigMap;
import io.fabric8.kubernetes.api.model.ConfigMapBuilder;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.dsl.Resource;
import nl.paas.tool.data.pipeline.config.PipelineConfig;
import nl.paas.tool.data.pipeline.datasource.api.IDataSourceController;
import nl.paas.tool.data.pipeline.datasource.mapper.ReplicationStatMapper;
import nl.paas.tool.data.pipeline.datasource.model.DataSourceVo;
import nl.paas.tool.data.pipeline.datasource.model.postgresql.Lsn;
import nl.paas.tool.data.pipeline.datasource.model.postgresql.ReplicationSlot;
import nl.paas.tool.data.pipeline.datasource.model.postgresql.ReplicationStat;
import nl.paas.tool.data.pipeline.utils.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DataSourceController implements IDataSourceController {
    @Autowired
    private PipelineConfig pipelineConfig;
    private static final String CONFIG_KEY = "DataSource";
    @Autowired
    private DataSource dataSource;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private ReplicationStatMapper replicationStatMapper;

    @Override
    public List<DataSourceVo> getDatasource() {
        List<DataSourceVo> result = new ArrayList<>();
        try (final KubernetesClient client = new DefaultKubernetesClient()) {
            Resource<ConfigMap> configMapResource = client.configMaps().inNamespace(pipelineConfig.getNamespace())
                .withName(pipelineConfig.getConfigMapName());
            ConfigMap configMap = configMapResource.get();
            if (configMap != null) {
                String jsonString = configMap.getData().get(CONFIG_KEY);
                result = JsonUtils.jsonToList(jsonString, DataSourceVo.class);
            }
        }
        return result;
    }

    @Override
    public Object addDataSource(DataSourceVo dataSourceVo) {
        try (final KubernetesClient client = new DefaultKubernetesClient()) {
            Resource<ConfigMap> configMapResource = client.configMaps().inNamespace(pipelineConfig.getNamespace())
                .withName(pipelineConfig.getConfigMapName());
            List<DataSourceVo> oldList = getDatasource();
            Optional<DataSourceVo> exist =
                oldList.stream().filter(d -> d.getName().equals(dataSourceVo.getName())).findAny();
            if (exist.isPresent()) {
                //exist = dataSourceVo;
            } else {
                oldList.add(dataSourceVo);
            }
            ConfigMap configMap = configMapResource.createOrReplace(new ConfigMapBuilder().
                withNewMetadata().withName(pipelineConfig.getConfigMapName()).endMetadata().
                addToData(CONFIG_KEY, JsonUtils.objectToJson(oldList)).
                build());
        }
        return dataSourceVo;
    }

    @Override
    public Object deleteDataSource(String name) {
        try (final KubernetesClient client = new DefaultKubernetesClient()) {
            Resource<ConfigMap> configMapResource = client.configMaps().inNamespace(pipelineConfig.getNamespace())
                .withName(pipelineConfig.getConfigMapName());
            List<DataSourceVo> oldList = getDatasource();
            DataSourceVo exist = oldList.stream().filter(d -> d.getName().equals(name)).findAny().get();
            if (exist != null) {
                oldList.remove(exist);
            }
            ConfigMap configMap = configMapResource.createOrReplace(new ConfigMapBuilder().
                withNewMetadata().withName(pipelineConfig.getConfigMapName()).endMetadata().
                addToData(CONFIG_KEY, JsonUtils.objectToJson(oldList)).
                build());
        }
        return name;
    }

    public DataSourceVo getDataSource(String name) {
        DataSourceVo exist;
        List<DataSourceVo> oldList = getDatasource();
        exist = oldList.stream().filter(d -> d.getName().equals(name)).findAny().get();
        return exist;
    }

    @Override
    @DS("#name")
    public HashSet<Table> getTableInfoList(String name, String schemaName) throws SQLException {
        Context context =
            Context.newInstance(dataSource.getConnection()).suppress(SQLFeatureNotSupportedException.class);
        Schema schema = new Schema();
        schema.setTableCatalog(schemaName);
        HashSet<Table> tables = context.getTables(schema, null, null, new HashSet<>());
        return tables;
    }

    @DS("#name")
    @Override
    public List<ReplicationStat> fetchStatReplicationInfo(String name) {
        List<ReplicationStat> result = replicationStatMapper.selectList(Wrappers.emptyWrapper());
        result.forEach(r -> {
            Lsn lsn = Lsn.valueOf(r.getFlushLsn());
            r.setFlushLsnValue(lsn.getValue());
            Lsn replayLsn = Lsn.valueOf(r.getReplayLsn());
            r.setReplayLsnValue(replayLsn.getValue());
            Lsn sentLsn = Lsn.valueOf(r.getSentLsn());
            r.setSentLsnValue(sentLsn.getValue());
            Lsn writeLsn = Lsn.valueOf(r.getWriteLsn());
            r.setWriteLsnValue(writeLsn.getValue());
        });
        return result;
    }

    @Override
    @DS("#name")
    public List<ReplicationSlot> fetchAllReplicationSlotInfo(String name) {
        List<ReplicationSlot> result = jdbcTemplate
            .query("select *,A.current_wal_lsn from pg_replication_slots,pg_current_wal_lsn() AS A(current_wal_lsn)",
                (rs, var2) -> {

                    boolean active = rs.getBoolean("active");
                    String slotName = rs.getString("slot_name");
                    String slotType = rs.getString("slot_type");
                    String pluginName = rs.getString("plugin");
                    final Long xmin = rs.getLong("catalog_xmin");
                    Lsn currentWalLsn = tryParseLsn(slotName, pluginName, name, rs, "current_wal_lsn");
                    Lsn restartLsn = tryParseLsn(slotName, pluginName, name, rs, "restart_lsn");
                    Lsn confirmedFlushedLsn = tryParseLsn(slotName, pluginName, name, rs, "confirmed_flush_lsn");
                    ReplicationSlot replicationSlot =
                        new ReplicationSlot(active, confirmedFlushedLsn, restartLsn, xmin);
                    replicationSlot.setSlotName(slotName);
                    replicationSlot.setSlotType(slotType);
                    replicationSlot.setCurrentWalLsn(currentWalLsn);
                    return replicationSlot;

                });
        return result;
    }

    private Lsn tryParseLsn(String slotName, String pluginName, String database, ResultSet rs, String column)
        throws SQLException {
        Lsn lsn = null;

        String lsnStr = rs.getString(column);
        if (lsnStr == null) {
            return null;
        }
        try {
            lsn = Lsn.valueOf(lsnStr);
        } catch (Exception e) {
            throw new RuntimeException(
                "Value " + column + " in the pg_replication_slots table for slot = '" + slotName + "', plugin = '"
                    + pluginName + "', database = '" + database
                    + "' is not valid. This is an abnormal situation and the database status should be checked.");
        }
        if (!lsn.isValid()) {
            throw new RuntimeException("Invalid LSN returned from database");
        }
        return lsn;
    }
}
