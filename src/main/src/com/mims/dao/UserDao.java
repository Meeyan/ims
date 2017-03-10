package com.mims.dao;

import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public class UserDao extends BaseDao {
    public List getUserById() {
        String sql = " SELECT id, account, password, name_eng, name_zh, phone, email, ctime, state FROM dbo.ad_user ";
        List list = this.qryBySql(sql);
        return list;
    }
}
