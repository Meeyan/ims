package com.mims.dao;

import org.springframework.stereotype.Repository;

/**
 * Created by zhaojy on 2017/2/27.
 */
@Repository
public class UserDao extends BaseDao {
    /*public List getUserById() {
        String sql = " SELECT id, account, password, name_eng, name_zh, phone, email, ctime, state FROM dbo.ad_user ";
        List list = this.qryBySql(sql);
        return list;
    }*/
}
