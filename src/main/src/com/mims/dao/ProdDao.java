package com.mims.dao;

import com.mims.beans.AdUser;
import com.mims.beans.ProdItems;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author zhaojy
 * @createTime 2017-03-01
 */
@Repository
public class ProdDao extends BaseDao {

    /**
     * 分页实现方法-示例
     *
     * @param pageNumber
     * @param pageSize
     * @return
     * @author zhaojy
     * @createTime 2017-03-01
     */
    public List getProdListWithPage(int pageNumber, int pageSize) {
        String start = "0";
        if (pageNumber > 0) {
            int tmp = (pageNumber - 1) * pageSize;
            start = String.valueOf(tmp);
        }
        if (pageSize <= 0) {
            pageSize = 10;
        }
        ProdItems items = new ProdItems();
        String[] strArr = {"content"};
        String sql = this.buildSelectSqlAll(items.getTypeMap(), items.getTableName(), strArr);
        sql += " ORDER BY `id` DESC LIMIT " + start + " , " + pageSize;
        List list = this.qryBySql(sql);
        return list;
    }

    /**
     * 保存 ProdItems
     *
     * @param prodItems
     * @return
     * @author zhaojy
     * @createTime 2017-03-01
     */
    public int saveProdItem(ProdItems prodItems) {
        return this.saveBaseBean(prodItems);
    }

    /**
     * 保存 AdUser
     *
     * @param adUser
     * @return
     * @author zhaojy
     * @createTime 2017-03-01
     */
    public int saveAdUser(AdUser adUser) {
        return this.saveBaseBean(adUser);
    }
}
