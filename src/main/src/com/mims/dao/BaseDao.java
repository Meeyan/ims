package com.mims.dao;

import org.springframework.jdbc.core.ColumnMapRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.annotation.Resource;
import java.util.List;

/**
 *
 */
public class BaseDao {
    @Resource
    private JdbcTemplate jdbcTemplate;

    protected List qryBySql(String sql) {
        Object[] objArr = new Object[1];
        List list = jdbcTemplate.query(sql, new ColumnMapRowMapper());
        return list;
    }
}
