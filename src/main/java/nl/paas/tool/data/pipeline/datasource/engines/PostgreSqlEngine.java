package nl.paas.tool.data.pipeline.datasource.engines;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.baomidou.mybatisplus.generator.IDatabaseQuery;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.builder.ConfigBuilder;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.querys.PostgreSqlQuery;

import io.debezium.connector.postgresql.connection.Lsn;
import io.debezium.connector.postgresql.connection.PostgresConnection;
import io.debezium.jdbc.JdbcConfiguration;
import io.debezium.jdbc.JdbcConnection;
import nl.paas.tool.data.pipeline.datasource.model.DataSourceVo;
import nl.paas.tool.data.pipeline.datasource.model.postgresql.ReplicationSlot;

public class PostgreSqlEngine extends PostgreSqlQuery {
    private ConfigBuilder configBuilder;

    private PostgresConnection postgresConnection;

    public PostgreSqlEngine(DataSourceVo dataSourceVo) {
        DataSourceConfig dataSourceConfig =
            new DataSourceConfig.Builder(dataSourceVo.getUrl(), dataSourceVo.getUsername(), dataSourceVo.getPassword())
                .build();
        this.configBuilder = new ConfigBuilder(null, dataSourceConfig, null, null, null, null);
        JdbcConfiguration.Builder build = JdbcConfiguration.create();
        JdbcConfiguration cc = build.withHostname(dataSourceVo.getHostname()).withPort(dataSourceVo.getPort())
            .withUser(dataSourceVo.getUsername()).withDatabase(dataSourceVo.getDbname())
            .withPassword(dataSourceVo.getPassword()).build();
        this.postgresConnection = new PostgresConnection(cc, false);

    }

    public List<TableInfo> getTableInfoList() {
        List<TableInfo> tableInfos = (new IDatabaseQuery.DefaultDatabaseQuery(this.configBuilder)).queryTables();
        return tableInfos;
    }

    public List<ReplicationSlot> fetchAllReplicationSlotInfo() throws SQLException {
        final List<ReplicationSlot> replicationSlotList = queryALLSlot(rs -> {

            List<ReplicationSlot> replicationSlot = new ArrayList<>();
            if (rs.next()) {
                boolean active = rs.getBoolean("active");
                String slotName = rs.getString("slot_name");
                String pluginName = rs.getString("plugin");
                final Lsn confirmedFlushedLsn = parseConfirmedFlushLsn(slotName, pluginName, rs);
                if (confirmedFlushedLsn == null) {
                    return null;
                }
                Lsn restartLsn = parseRestartLsn(slotName, pluginName, rs);
                if (restartLsn == null) {
                    return null;
                }
                final Long xmin = rs.getLong("catalog_xmin");
                replicationSlot.add(new ReplicationSlot(active, confirmedFlushedLsn, restartLsn, xmin));
            } else {
                replicationSlot.add(ReplicationSlot.INVALID);
            }
            return replicationSlot;
        });
        return replicationSlotList;
    }

    protected List<ReplicationSlot> queryALLSlot(JdbcConnection.ResultSetMapper<List<ReplicationSlot>> map)
        throws SQLException {
        return this.postgresConnection.prepareQueryAndMap("select * from pg_replication_slots", statement -> {
        }, map);
    }

    private Lsn parseConfirmedFlushLsn(String slotName, String pluginName, ResultSet rs) {
        Lsn confirmedFlushedLsn = null;

        try {
            confirmedFlushedLsn = tryParseLsn(slotName, pluginName, rs, "confirmed_flush_lsn");
        } catch (SQLException e) {
            try {
                confirmedFlushedLsn = tryParseLsn(slotName, pluginName, rs, "restart_lsn");
            } catch (SQLException e2) {
                throw new RuntimeException("Neither confirmed_flush_lsn nor restart_lsn could be found");
            }
        }

        return confirmedFlushedLsn;
    }

    private Lsn parseRestartLsn(String slotName, String pluginName, ResultSet rs) {
        Lsn restartLsn = null;
        try {
            restartLsn = tryParseLsn(slotName, pluginName, rs, "restart_lsn");
        } catch (SQLException e) {
            throw new RuntimeException("restart_lsn could be found");
        }

        return restartLsn;
    }

    private Lsn tryParseLsn(String slotName, String pluginName, ResultSet rs, String column) throws SQLException {
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
                    + pluginName + "', database = '"
                    + "' is not valid. This is an abnormal situation and the database status should be checked.");
        }
        if (!lsn.isValid()) {
            throw new RuntimeException("Invalid LSN returned from database");
        }
        return lsn;
    }

    public ConfigBuilder getConfigBuilder() {
        return configBuilder;
    }

    public void setConfigBuilder(ConfigBuilder configBuilder) {
        this.configBuilder = configBuilder;
    }

    public PostgresConnection getPostgresConnection() {
        return postgresConnection;
    }

    public void setPostgresConnection(PostgresConnection postgresConnection) {
        this.postgresConnection = postgresConnection;
    }
}
