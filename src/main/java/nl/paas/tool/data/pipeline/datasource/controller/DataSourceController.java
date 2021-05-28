package nl.paas.tool.data.pipeline.datasource.controller;

import java.sql.Driver;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.converts.MySqlTypeConvert;
import com.baomidou.mybatisplus.generator.config.querys.MySqlQuery;
import com.baomidou.mybatisplus.generator.keywords.MySqlKeyWordsHandler;

import io.fabric8.kubernetes.api.model.ConfigMap;
import io.fabric8.kubernetes.api.model.ConfigMapBuilder;
import io.fabric8.kubernetes.api.model.DoneableConfigMap;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.dsl.Resource;
import nl.paas.tool.data.pipeline.config.PipelineConfig;
import nl.paas.tool.data.pipeline.datasource.api.IDataSourceController;
import nl.paas.tool.data.pipeline.datasource.model.DataSourceVo;
import nl.paas.tool.data.pipeline.utils.JsonUtils;
import nonapi.io.github.classgraph.json.JSONUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DataSourceController implements IDataSourceController {
    @Autowired
    private PipelineConfig pipelineConfig;
    private static final String CONFIG_KEY = "DataSource";

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
            DataSourceVo exist =
                oldList.stream().filter(d -> d.getName().equals(dataSourceVo.getName())).findAny().get();
            if (exist != null) {
                exist = dataSourceVo;
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
}
