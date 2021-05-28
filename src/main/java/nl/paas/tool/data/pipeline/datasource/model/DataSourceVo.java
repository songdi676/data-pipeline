package nl.paas.tool.data.pipeline.datasource.model;

import com.baomidou.mybatisplus.generator.config.DataSourceConfig;

public class DataSourceVo extends DataSourceConfig {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
