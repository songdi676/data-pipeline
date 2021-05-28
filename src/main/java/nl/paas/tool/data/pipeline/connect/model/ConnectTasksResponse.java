/*
 * Copyright Debezium Authors.
 *
 * Licensed under the Apache Software License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package nl.paas.tool.data.pipeline.connect.model;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * 获取运行连接器的任务列表返回
 * @return
 */
public class ConnectTasksResponse {

    private Map<String, String> id;
    private Map<String, String> config;

    public Map<String, String> getId() {
        return id;
    }

    public void setId(Map<String, ?> id) {
        this.id = id.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, entry -> String.valueOf(entry.getValue())));
    }

    public Map<String, String> getConfig() {
        return config;
    }

    public void setConfig(Map<String, ?> config) {
        this.config = config.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, entry -> String.valueOf(entry.getValue())));
    }
}
