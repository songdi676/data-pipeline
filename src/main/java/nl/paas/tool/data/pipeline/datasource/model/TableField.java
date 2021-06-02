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

import java.util.Map;

import org.apache.ibatis.type.JdbcType;

/**
 * 表字段信息
 *
 * @author YangHu
 * @since 2016-12-03
 */
public class TableField {
    private boolean convert;
    private boolean keyFlag;
    /**
     * 主键是否为自增类型
     */
    private boolean keyIdentityFlag;
    private String name;
    private String type;
    private String propertyName;
    private IColumnType columnType;
    private String comment;
    private String fill;
    /**
     * 是否关键字
     *
     * @since 3.3.2
     */
    private boolean keyWords;
    /**
     * 数据库字段（关键字含转义符号）
     *
     * @since 3.3.2
     */
    private String columnName;
    /**
     * 自定义查询字段列表
     */
    private Map<String, Object> customMap;

    /**
     * 字段元数据信息
     *
     * @since 3.5.0
     */
    private MetaInfo metaInfo;

    /**
     * 设置属性名称
     *
     * @param propertyName 属性名
     * @param columnType   字段类型
     * @return this
     * @since 3.5.0
     */
    //TODO 待调整.
    public TableField setPropertyName(String propertyName, IColumnType columnType) {
        this.columnType = columnType;
        this.propertyName = propertyName;
        return this;
    }

    public String getPropertyType() {
        if (null != columnType) {
            return columnType.getType();
        }
        return null;
    }

    /**
     * 按 JavaBean 规则来生成 get 和 set 方法后面的属性名称
     * 需要处理一下特殊情况：
     * <p>
     * 1、如果只有一位，转换为大写形式
     * 2、如果多于 1 位，只有在第二位是小写的情况下，才会把第一位转为小写
     * <p>
     * 我们并不建议在数据库对应的对象中使用基本类型，因此这里不会考虑基本类型的情况
     */
    public String getCapitalName() {
        if (propertyName.length() == 1) {
            return propertyName.toUpperCase();
        }
        if (Character.isLowerCase(propertyName.charAt(1))) {
            return Character.toUpperCase(propertyName.charAt(0)) + propertyName.substring(1);
        }
        return propertyName;
    }

    /**
     * 获取注解字段名称
     *
     * @return 字段
     * @since 3.3.2
     */
    public String getAnnotationColumnName() {
        if (keyWords) {
            if (columnName.startsWith("\"")) {
                return String.format("\\\"%s\\\"", name);
            }
        }
        return columnName;
    }

    /**
     * 设置主键
     *
     * @param autoIncrement 自增标识
     * @return this
     * @since 3.5.0
     */
    public TableField primaryKey(boolean autoIncrement) {
        this.keyFlag = true;
        this.keyIdentityFlag = autoIncrement;
        return this;
    }

    public TableField setType(String type) {
        this.type = type;
        return this;
    }

    public TableField setComment(String comment) {
        this.comment = comment;
        return this;
    }

    public TableField setColumnName(String columnName) {
        this.columnName = columnName;

        return this;
    }

    public TableField setCustomMap(Map<String, Object> customMap) {
        this.customMap = customMap;
        return this;
    }

    public boolean isConvert() {
        return convert;
    }

    public boolean isKeyFlag() {
        return keyFlag;
    }

    public boolean isKeyIdentityFlag() {
        return keyIdentityFlag;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public IColumnType getColumnType() {
        return columnType;
    }

    public String getComment() {
        return comment;
    }

    public String getFill() {

        return fill;
    }

    public boolean isKeyWords() {
        return keyWords;
    }

    public String getColumnName() {
        return columnName;
    }

    public Map<String, Object> getCustomMap() {
        return customMap;
    }

    public MetaInfo getMetaInfo() {
        return metaInfo;
    }

    public void setMetaInfo(MetaInfo metaInfo) {
        this.metaInfo = metaInfo;
    }

    /**
     * 元数据信息
     *
     * @author nieqiurong 2021/2/8
     * @since 3.5.0
     */
    public static class MetaInfo {

        private int length;

        private boolean nullable;

        private String remarks;

        private String defaultValue;

        private int scale;

        private JdbcType jdbcType;

        public MetaInfo(DatabaseMetaDataWrapper.ColumnsInfo columnsInfo) {
            if (columnsInfo != null) {
                this.length = columnsInfo.getLength();
                this.nullable = columnsInfo.isNullable();
                this.remarks = columnsInfo.getRemarks();
                this.defaultValue = columnsInfo.getDefaultValue();
                this.scale = columnsInfo.getScale();
                this.jdbcType = columnsInfo.getJdbcType();
            }
        }

        public int getLength() {
            return length;
        }

        public boolean isNullable() {
            return nullable;
        }

        public String getRemarks() {
            return remarks;
        }

        public String getDefaultValue() {
            return defaultValue;
        }

        public int getScale() {
            return scale;
        }

        public JdbcType getJdbcType() {
            return jdbcType;
        }

        @Override
        public String toString() {
            return "MetaInfo{" + "length=" + length + ", nullable=" + nullable + ", remarks='" + remarks + '\''
                + ", defaultValue='" + defaultValue + '\'' + ", scale=" + scale + ", jdbcType=" + jdbcType + '}';
        }
    }
}
