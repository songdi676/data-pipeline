/*
 * Copyright (c) 2011-2021, baomidou (jobob@qq.com).
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * <p>
 * https://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package nl.paas.tool.data.pipeline.datasource.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;

/**
 * 表信息，关联到当前字段信息
 *
 * @author YangHu
 * @since 2016/8/30
 */
public class TableInfo {
    private final Set<String> importPackages = new TreeSet<>();
    private boolean convert;
    private String name;
    private String comment;
    private String entityName;
    private String mapperName;
    private String xmlName;
    private String serviceName;
    private String serviceImplName;
    private String controllerName;
    private final List<TableField> fields = new ArrayList<>();
    private boolean havePrimaryKey;
    /**
     * 公共字段
     */
    private final List<TableField> commonFields = new ArrayList<>();
    private String fieldNames;

    public String getEntityPath() {
        return entityName.substring(0, 1).toLowerCase() + entityName.substring(1);
    }

    /**
     * 添加字段
     *
     * @param field 字段
     * @since 3.5.0
     */
    public void addField(TableField field) {

        this.fields.add(field);

    }

    /**
     * @param pkgs 包空间
     * @return this
     * @since 3.5.0
     */
    public TableInfo addImportPackages(String... pkgs) {
        importPackages.addAll(Arrays.asList(pkgs));
        return this;
    }

    /**
     * 转换filed实体为 xml mapper 中的 base column 字符串信息
     */
    public String getFieldNames() {
        //TODO 感觉这个也啥必要,不打算公开set方法了
        if (StringUtils.isBlank(fieldNames)) {
            this.fieldNames = this.fields.stream().map(TableField::getColumnName).collect(Collectors.joining(", "));
        }
        return this.fieldNames;
    }

    public TableInfo setComment(String comment) {
        this.comment = comment;
        return this;
    }

    public TableInfo setHavePrimaryKey(boolean havePrimaryKey) {
        this.havePrimaryKey = havePrimaryKey;
        return this;
    }

    public Set<String> getImportPackages() {
        return importPackages;
    }

    public boolean isConvert() {
        return convert;
    }

    public TableInfo setConvert(boolean convert) {
        this.convert = convert;
        return this;
    }

    public String getName() {
        return name;
    }

    public String getComment() {
        return comment;
    }

    public String getEntityName() {
        return entityName;
    }

    public String getMapperName() {
        return mapperName;
    }

    public String getXmlName() {
        return xmlName;
    }

    public String getServiceName() {
        return serviceName;
    }

    public String getServiceImplName() {
        return serviceImplName;
    }

    public String getControllerName() {
        return controllerName;
    }

    public List<TableField> getFields() {
        return fields;
    }

    public boolean isHavePrimaryKey() {
        return havePrimaryKey;
    }

    public List<TableField> getCommonFields() {
        return commonFields;
    }
}
