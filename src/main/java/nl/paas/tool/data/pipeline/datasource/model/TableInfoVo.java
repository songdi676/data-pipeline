package nl.paas.tool.data.pipeline.datasource.model;

import com.baomidou.mybatisplus.annotation.TableField;

public class TableInfoVo {
    private String tableName;
    private String numRows;
    @TableField(exist = false)
    private String tableType;
    @TableField(exist = false)
    private String remarks;


    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getNumRows() {
        return numRows;
    }

    public void setNumRows(String numRows) {
        this.numRows = numRows;
    }

    public String getTableType() {
        return tableType;
    }

    public void setTableType(String tableType) {
        this.tableType = tableType;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}
