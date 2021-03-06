/**
 * Copyright (c) 2012-2013, Michael Yang 杨福海 (www.yangfuhai.com).
 * Copyright (c) 2014,KJFrameForAndroid Open Source Project,张涛.
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kymjs.kjframe.database;

import android.text.TextUtils;

import org.kymjs.kjframe.database.utils.Id;
import org.kymjs.kjframe.database.utils.KeyValue;
import org.kymjs.kjframe.database.utils.ManyToOne;
import org.kymjs.kjframe.database.utils.Property;
import org.kymjs.kjframe.database.utils.TableInfo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * sql语句生产者<br>
 * <p/>
 * <b>创建时间</b> 2014-8-15
 *
 * @author kymjs (http://www.kymjs.com)
 * @author 杨福海 (http://www.yangfuhai.com)
 * @version 1.0
 */
public class SqlBuilder {

    /**
     * 获取插入的sql语句
     *
     * @param entity
     * @return
     */
    public static SqlInfo buildInsertSql(Object entity) {
        List<KeyValue> keyValueList = getSaveKeyValueListByEntity(entity);

        StringBuilder strSQL = new StringBuilder();
        SqlInfo sqlInfo = null;
        if (keyValueList != null && keyValueList.size() > 0) {

            sqlInfo = new SqlInfo();

            strSQL.append("INSERT INTO ");
            strSQL.append(TableInfo.get(entity.getClass()).getTableName());
            strSQL.append(" (");
            for (KeyValue kv : keyValueList) {
                strSQL.append(kv.getKey()).append(",");
                sqlInfo.addValue(kv.getValue());
            }
            strSQL.deleteCharAt(strSQL.length() - 1);
            strSQL.append(") VALUES ( ");

            int length = keyValueList.size();
            for (int i = 0; i < length; i++) {
                strSQL.append("?,");
            }
            strSQL.deleteCharAt(strSQL.length() - 1);
            strSQL.append(")");

            sqlInfo.setSql(strSQL.toString());
        }

        return sqlInfo;
    }

    public static List<KeyValue> getSaveKeyValueListByEntity(Object entity) {
        List<KeyValue> keyValueList = new ArrayList<KeyValue>();

        TableInfo table = TableInfo.get(entity.getClass());
        Object idvalue = table.getId().getValue(entity);

        // 用了非自增长,添加id , 采用自增长就不需要添加id了
        if (!(idvalue instanceof Integer)) {
            if (idvalue instanceof String) {
                KeyValue kv = new KeyValue(table.getId().getColumn(), idvalue);
                keyValueList.add(kv);
            }
        }

        // 添加属性
        Collection<Property> propertys = table.propertyMap.values();
        for (Property property : propertys) {
            KeyValue kv = property2KeyValue(property, entity);
            if (kv != null)
                keyValueList.add(kv);
        }

        // 添加外键（多对一）
        Collection<ManyToOne> manyToOnes = table.manyToOneMap.values();
        for (ManyToOne many : manyToOnes) {
            KeyValue kv = manyToOne2KeyValue(many, entity);
            if (kv != null)
                keyValueList.add(kv);
        }

        return keyValueList;
    }

    private static String getDeleteSqlBytableName(String tableName) {
        return "DELETE FROM " + tableName;
    }

    public static SqlInfo buildDeleteSql(Object entity) {
        TableInfo table = TableInfo.get(entity.getClass());
        Id id = table.getId();
        Object idvalue = id.getValue(entity);

        if (idvalue == null) {
            throw new RuntimeException("getDeleteSQL:" + entity.getClass()
                    + " id value is null");
        }

        SqlInfo sqlInfo = new SqlInfo();
        sqlInfo.setSql(getDeleteSqlBytableName(table.getTableName()) + " WHERE " + id.getColumn() + "=?");
        sqlInfo.addValue(idvalue);

        return sqlInfo;
    }

    public static SqlInfo buildDeleteSql(Class<?> clazz, Object idValue) {
        TableInfo table = TableInfo.get(clazz);
        Id id = table.getId();

        if (null == idValue) {
            throw new RuntimeException("getDeleteSQL:idValue is null");
        }

        SqlInfo sqlInfo = new SqlInfo();
        sqlInfo.setSql(getDeleteSqlBytableName(table.getTableName()) + " WHERE " + id.getColumn() + "=?");
        sqlInfo.addValue(idValue);

        return sqlInfo;
    }

    /**
     * 根据条件删除数据 ，条件为空的时候将会删除所有的数据
     *
     * @param clazz
     * @param strWhere
     * @return
     */
    public static String buildDeleteSql(Class<?> clazz, String strWhere) {
        TableInfo table = TableInfo.get(clazz);
        StringBuilder strSQL = new StringBuilder(
                getDeleteSqlBytableName(table.getTableName()));

        if (!TextUtils.isEmpty(strWhere)) {
            strSQL.append(" WHERE ");
            strSQL.append(strWhere);
        }

        return strSQL.toString();
    }

    // //////////////////////////select sql
    // start///////////////////////////////////////

    private static String getSelectSqlByTableName(String tableName) {
        return "SELECT * FROM " + tableName;
    }

    public static String getSelectSQL(Class<?> clazz, Object idValue) {
        TableInfo table = TableInfo.get(clazz);

        return getSelectSqlByTableName(table.getTableName()) + " WHERE " +
                getPropertyStrSql(table.getId().getColumn(), idValue);
    }

    public static SqlInfo getSelectSqlAsSqlInfo(Class<?> clazz, Object idValue) {
        TableInfo table = TableInfo.get(clazz);

        SqlInfo sqlInfo = new SqlInfo();
        sqlInfo.setSql(getSelectSqlByTableName(table.getTableName()) + " WHERE " + table.getId().getColumn() + "=?");
        sqlInfo.addValue(idValue);

        return sqlInfo;
    }

    public static String getSelectSQL(Class<?> clazz) {
        return getSelectSqlByTableName(TableInfo.get(clazz).getTableName());
    }

    public static String getSelectSQLByWhere(Class<?> clazz, String strWhere) {
        TableInfo table = TableInfo.get(clazz);

        StringBuilder strSQL = new StringBuilder(
                getSelectSqlByTableName(table.getTableName()));

        if (!TextUtils.isEmpty(strWhere)) {
            strSQL.append(" WHERE ").append(strWhere);
        }

        return strSQL.toString();
    }

    // ////////////////////////////update sql
    // start/////////////////////////////////////////////

    public static SqlInfo getUpdateSqlAsSqlInfo(Object entity) {

        TableInfo table = TableInfo.get(entity.getClass());
        Object idvalue = table.getId().getValue(entity);

        if (null == idvalue) {// 主键值不能为null，否则不能更新
            throw new RuntimeException("this entity[" + entity.getClass()
                    + "]'s id value is null");
        }

        List<KeyValue> keyValueList = new ArrayList<KeyValue>();
        // 添加属性
        Collection<Property> propertys = table.propertyMap.values();
        for (Property property : propertys) {
            KeyValue kv = property2KeyValue(property, entity);
            if (kv != null)
                keyValueList.add(kv);
        }

        // 添加外键（多对一）
        Collection<ManyToOne> manyToOnes = table.manyToOneMap.values();
        for (ManyToOne many : manyToOnes) {
            KeyValue kv = manyToOne2KeyValue(many, entity);
            if (kv != null)
                keyValueList.add(kv);
        }

        if (keyValueList.size() == 0)
            return null;

        SqlInfo sqlInfo = new SqlInfo();
        StringBuilder strSQL = new StringBuilder("UPDATE ");
        strSQL.append(table.getTableName());
        strSQL.append(" SET ");
        for (KeyValue kv : keyValueList) {
            strSQL.append(kv.getKey()).append("=?,");
            sqlInfo.addValue(kv.getValue());
        }
        strSQL.deleteCharAt(strSQL.length() - 1);
        strSQL.append(" WHERE ").append(table.getId().getColumn()).append("=?");
        sqlInfo.addValue(idvalue);
        sqlInfo.setSql(strSQL.toString());
        return sqlInfo;
    }

    public static SqlInfo getUpdateSqlAsSqlInfo(Object entity, String strWhere) {

        TableInfo table = TableInfo.get(entity.getClass());

        List<KeyValue> keyValueList = new ArrayList<KeyValue>();

        // 添加属性
        Collection<Property> propertys = table.propertyMap.values();
        for (Property property : propertys) {
            KeyValue kv = property2KeyValue(property, entity);
            if (kv != null)
                keyValueList.add(kv);
        }

        // 添加外键（多对一）
        Collection<ManyToOne> manyToOnes = table.manyToOneMap.values();
        for (ManyToOne many : manyToOnes) {
            KeyValue kv = manyToOne2KeyValue(many, entity);
            if (kv != null)
                keyValueList.add(kv);
        }

        if (keyValueList.size() == 0) {
            throw new RuntimeException("this entity[" + entity.getClass()
                    + "] has no property");
        }

        SqlInfo sqlInfo = new SqlInfo();
        StringBuilder strSQL = new StringBuilder("UPDATE ");
        strSQL.append(table.getTableName());
        strSQL.append(" SET ");
        for (KeyValue kv : keyValueList) {
            strSQL.append(kv.getKey()).append("=?,");
            sqlInfo.addValue(kv.getValue());
        }
        strSQL.deleteCharAt(strSQL.length() - 1);
        if (!TextUtils.isEmpty(strWhere)) {
            strSQL.append(" WHERE ").append(strWhere);
        }
        sqlInfo.setSql(strSQL.toString());
        return sqlInfo;
    }

    public static SqlInfo getUpdateSqlAsSqlInfo(Object entity, String strWhere, String properties) {

        TableInfo table = TableInfo.get(entity.getClass());

        List<KeyValue> keyValueList = new ArrayList<KeyValue>();

        // 添加属性
        Collection<Property> propertys = table.propertyMap.values();
        for (Property property : propertys) {
            if (property.getFieldName().equals(properties)) {
                KeyValue kv = property2KeyValue(property, entity);
                if (kv != null)
                    keyValueList.add(kv);
                break;
            }
        }
        // 添加外键（多对一）
        Collection<ManyToOne> manyToOnes = table.manyToOneMap.values();
        for (ManyToOne many : manyToOnes) {
            KeyValue kv = manyToOne2KeyValue(many, entity);
            if (kv != null)
                keyValueList.add(kv);
        }

        if (keyValueList.size() == 0) {
            throw new RuntimeException("this entity[" + entity.getClass()
                    + "] has no property");
        }

        SqlInfo sqlInfo = new SqlInfo();
        StringBuilder strSQL = new StringBuilder("UPDATE ");
        strSQL.append(table.getTableName());
        strSQL.append(" SET ");
        for (KeyValue kv : keyValueList) {
            strSQL.append(kv.getKey()).append("=?,");
            sqlInfo.addValue(kv.getValue());
        }
        strSQL.deleteCharAt(strSQL.length() - 1);
        if (!TextUtils.isEmpty(strWhere)) {
            strSQL.append(" WHERE ").append(strWhere);
        } else {
            Object idvalue = table.getId().getValue(entity);
            strSQL.append(" WHERE ").append(table.getId().getColumn()).append("=?");
            sqlInfo.addValue(idvalue);
        }

        sqlInfo.setSql(strSQL.toString());
        return sqlInfo;
    }

    public static SqlInfo getUpdateSqlAsSqlInfo(Object entity, String strWhere, String[] properties) {

        TableInfo table = TableInfo.get(entity.getClass());

        List<KeyValue> keyValueList = new ArrayList<KeyValue>();
        List<String> list = Arrays.asList(properties);
        // 添加属性
        Collection<Property> propertys = table.propertyMap.values();
        for (Property property : propertys) {
            if (list.contains(property.getFieldName())) {
                KeyValue kv = property2KeyValue(property, entity);
                if (kv != null)
                    keyValueList.add(kv);
            }
        }
        // 添加外键（多对一）
        Collection<ManyToOne> manyToOnes = table.manyToOneMap.values();
        for (ManyToOne many : manyToOnes) {
            KeyValue kv = manyToOne2KeyValue(many, entity);
            if (kv != null)
                keyValueList.add(kv);
        }

        if (keyValueList.size() == 0) {
            throw new RuntimeException("this entity[" + entity.getClass()
                    + "] has no property");
        }

        SqlInfo sqlInfo = new SqlInfo();
        StringBuilder strSQL = new StringBuilder("UPDATE ");
        strSQL.append(table.getTableName());
        strSQL.append(" SET ");
        for (KeyValue kv : keyValueList) {
            strSQL.append(kv.getKey()).append("=?,");
            sqlInfo.addValue(kv.getValue());
        }
        strSQL.deleteCharAt(strSQL.length() - 1);
        if (!TextUtils.isEmpty(strWhere)) {
            strSQL.append(" WHERE ").append(strWhere);
        } else {
            Object idvalue = table.getId().getValue(entity);
            strSQL.append(" WHERE ").append(table.getId().getColumn()).append("=?");
            sqlInfo.addValue(idvalue);
        }

        sqlInfo.setSql(strSQL.toString());
        return sqlInfo;
    }

    public static String getCreatTableSQL(Class<?> clazz) {
        TableInfo table = TableInfo.get(clazz);

        Id id = table.getId();
        StringBuilder strSQL = new StringBuilder();
        strSQL.append("CREATE TABLE IF NOT EXISTS ");
        strSQL.append(table.getTableName());
        strSQL.append(" ( ");

        Class<?> primaryClazz = id.getDataType();
        if (primaryClazz == int.class || primaryClazz == Integer.class)
            strSQL.append("\"").append(id.getColumn()).append("\"    ")
                    .append("INTEGER PRIMARY KEY AUTOINCREMENT,");
        else
            strSQL.append("\"").append(id.getColumn()).append("\"    ")
                    .append("TEXT PRIMARY KEY,");

        Collection<Property> propertys = table.propertyMap.values();
        for (Property property : propertys) {
            strSQL.append("\"").append(property.getColumn());
            strSQL.append("\",");
        }

        Collection<ManyToOne> manyToOnes = table.manyToOneMap.values();
        for (ManyToOne manyToOne : manyToOnes) {
            strSQL.append("\"").append(manyToOne.getColumn()).append("\",");
        }
        strSQL.deleteCharAt(strSQL.length() - 1);
        strSQL.append(" )");
        return strSQL.toString();
    }

    public static List<String> getIndexTableSQL(Class<?> clazz) {
        TableInfo table = TableInfo.get(clazz);


        List<String> sqls = new ArrayList<>();

        Collection<Property> propertys = table.propertyMap.values();
        for (Property property : propertys) {
            if (property.isIndex()) {

                StringBuilder strSQL = new StringBuilder();
                strSQL.append("CREATE INDEX INDEX_").append(property.getFieldName()).append(" ON");
                strSQL.append(table.getTableName());
                strSQL.append(" ( ").append(property.getFieldName()).append(")");
                sqls.add(strSQL.toString());
            }
        }
        return sqls;
    }

    /**
     * @param key
     * @param value
     * @return eg1: name='afinal' eg2: id=100
     */
    private static String getPropertyStrSql(String key, Object value) {
        StringBuilder sbSQL = new StringBuilder(key).append("=");
        if (value instanceof String) {
            sbSQL.append("'").append(value).append("'");
        } else if (value instanceof java.util.Date) {
            sbSQL.append(((Date) value).getTime());
        } else {
            sbSQL.append(value);
        }
        return sbSQL.toString();
    }

    private static KeyValue property2KeyValue(Property property, Object entity) {
        KeyValue kv = null;
        String pcolumn = property.getColumn();
        Object value = property.getValue(entity);
//        if (value != null) {
            kv = new KeyValue(pcolumn, value);
//        } else {
//            if (property.getDefaultValue() != null
//                    && property.getDefaultValue().trim().length() != 0)
//                kv = new KeyValue(pcolumn, property.getDefaultValue());
//        }
        return kv;
    }

    private static KeyValue manyToOne2KeyValue(ManyToOne many, Object entity) {
        KeyValue kv = null;
        String manycolumn = many.getColumn();
        Object manyobject = many.getValue(entity);
        if (manyobject != null) {
            Object manyvalue;
            if (manyobject.getClass() == ManyToOneLazyLoader.class) {
                manyvalue = TableInfo.get(many.getManyClass()).getId()
                        .getValue(manyobject);
            } else {
                manyvalue = TableInfo.get(manyobject.getClass()).getId()
                        .getValue(manyobject);
            }
            if (manycolumn != null && manyvalue != null) {
                kv = new KeyValue(manycolumn, manyvalue);
            }
        }

        return kv;
    }

}
