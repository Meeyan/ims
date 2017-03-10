package com.mims.beans;

import java.util.Map;

/**
 * 实体接口
 *
 * @author zhaojy
 * @createTime 2017-03-03
 */
public interface BaseBean {

    /**
     * 存储数据类型
     *
     * @return
     */
    public Map<String, Integer> getTypeMap();

    /**
     * 获取表名
     * @return
     */
    public String getTableName();

    /**
     * 获取set的所有值
     * @return
     */
    public Map<String, Object> getValueMap();
}
