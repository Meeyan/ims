package com.mims.dao;

import com.mims.beans.BaseBean;
import com.mims.util.DataTypeUtil;
import org.apache.commons.lang.ArrayUtils;
import org.springframework.jdbc.core.ColumnMapRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * dao 基本方法
 * @author zhaojy
 * @createTime 2017-03-01
 */
public class BaseDao {

    /* ***** 共享常量 ***** */
    public static final String INSERT_SQL = "INSERT_SQL";
    public static final String VALUE_ARR = "VALUE_ARR";
    public static final String TYPE_ARR = "TYPE_ARR";

    @Resource
    private JdbcTemplate jdbcTemplate;

    protected List qryBySql(String sql) {
        List list = jdbcTemplate.query(sql, new ColumnMapRowMapper());
        return list;
    }

    /***
     * 保存数据-原生方法
     *
     * @param sql
     * @param values
     * @param types
     * @return
     */
    protected int saveData(String sql, Object[] values, int[] types) {
        return jdbcTemplate.update(sql, values, types);
    }

    /**
     * 生成查询sql
     *
     * @param map        类型map
     * @param tableName  表名
     * @param excludeArr 排除的字段数组
     * @return
     */
    protected String buildSelectSqlAll(Map<String, Integer> map, String tableName, String[] excludeArr) {
        if (null != map && map.size() > 0) {
            String sql = "SELECT ";
            int count = 0;
            int totalSize = map.size();
            for (Map.Entry<String, Integer> entry : map.entrySet()) {
                count++;
                String key = entry.getKey();
                // 排除的字段
                if (ArrayUtils.contains(excludeArr, key)) {
                    continue;
                }
                sql += key;
                if (totalSize > count) {
                    sql += " , ";
                }
            }
            sql += " FROM " + tableName;
            return sql;
        }
        return "";
    }

    /**
     * 保存bean，入库
     * @param baseBean
     * @return
     */
    protected int saveBaseBean(BaseBean baseBean) {
        Map retMap = this.buildInsertSql(baseBean.getValueMap(), baseBean.getTypeMap(), baseBean.getTableName());
        if (retMap.size() == 3) {
            String insertSql = DataTypeUtil.getDataAsString(retMap.get(BaseDao.INSERT_SQL));
            Object[] values = (Object[]) retMap.get(BaseDao.VALUE_ARR);
            int[] types = (int[]) retMap.get(BaseDao.TYPE_ARR);
            return this.saveData(insertSql, values, types);
        }
        return 0;
    }

    /**
     * 生成insert-sql方法
     * @param valueMap  值的map
     * @param typeMap   类型的Map
     * @param tableName 表名
     * @return Map<String, Object>
     */
    protected Map<String, Object> buildInsertSql(Map<String, Object> valueMap, Map<String, Integer> typeMap, String tableName) {
        StringBuilder preBuffer = new StringBuilder();
        StringBuilder valueBuff = new StringBuilder();
        Map<String, Object> retMap = new HashMap<String, Object>();
        if (null != valueMap && valueMap.size() > 0) {
            preBuffer.append(" INSERT INTO " + tableName + " ( ");
            valueBuff.append(" values ( ");
            List<Object> objList = new ArrayList<>();
            List<Integer> typeList = new ArrayList<>();
            int count = 0;
            int totalSize = valueMap.size();
            for (Map.Entry<String, Object> entry : valueMap.entrySet()) {
                count++;
                String key = entry.getKey();
                Object value = entry.getValue();
                objList.add(value); // 值
                typeList.add(DataTypeUtil.getDataAsInt(typeMap.get(key))); // 类型数组
                preBuffer.append(" " + key + " ");
                valueBuff.append(" ? ");
                if (count < totalSize) {
                    preBuffer.append(",");
                    valueBuff.append(",");
                }
            }
            preBuffer.append(") ").append(valueBuff).append(" )");  // 完整的sql
            Object[] valObjArr = objList.toArray(); // 值的集合
            int[] objects = ArrayUtils.toPrimitive(typeList.toArray(new Integer[]{}));

            retMap.put(INSERT_SQL, preBuffer.toString());
            retMap.put(VALUE_ARR, valObjArr);
            retMap.put(TYPE_ARR, objects);
        }
        return retMap;
    }

    protected Object[] buildTypesArr(Map map) {
        return null;
    }

}
