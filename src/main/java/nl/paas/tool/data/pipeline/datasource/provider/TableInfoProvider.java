package nl.paas.tool.data.pipeline.datasource.provider;

import java.util.HashMap;
import java.util.Map;

import nl.paas.tool.data.pipeline.datasource.model.TableInfoVo;
import org.apache.ibatis.builder.annotation.ProviderContext;
import org.apache.ibatis.jdbc.SQL;

public class TableInfoProvider extends SqlProvider {
    public static final Map<String, SQL> SQL_MAP = new HashMap<>();

    static {
        SQL_MAP.put("postgresql", new SQL() {{
            SELECT("relname as table_name", "reltuples as num_rows", "obj_description(relfilenode, 'pg_class')");
            FROM("pg_class");
            WHERE("relkind = 'r'");
            ORDER_BY("TABLE_NAME asc");

        }});
        SQL_MAP.put("oracle", new SQL() {{
            SELECT("t.table_name", "num_rows", "comments as remarks");
            FROM("user_tables t");
            LEFT_OUTER_JOIN("user_tab_comments c on c.TABLE_NAME=t.TABLE_NAME");
            ORDER_BY("table_name asc");

        }});
    }

    public String getSql(TableInfoVo model, ProviderContext providerContext) {

        String databaseId = getDataBaseId();
        SQL sql = SQL_MAP.get(databaseId);
        if (sql == null) {
            return null;
        }
        return sql.toString();
    }

}
