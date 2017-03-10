package com.mims.beans;

import com.mims.util.DataTypeUtil;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 类生成以后，请执行类导入优化操作和格式化操作，保证代码格式统一
 *
 * @author authorName
 * @createTime 2017-03-03 15:50:16
 */
public class AdUser implements BaseBean, Serializable {

    private Map<String, Object> valueMap = new HashMap<>();

    private java.lang.Integer id;
    private java.lang.String account;
    private java.lang.String password;
    private java.lang.String nameEng;
    private java.lang.String nameZh;
    private java.lang.String phone;
    private java.lang.String email;
    private java.sql.Timestamp ctime;
    private java.lang.Short state;


    /**
     * get bean table name
     *
     * @return String
     * @author authorName
     * @createTime 2017-03-03 15:50:16
     */
    @Override
    public String getTableName() {
        return "ad_user";
    }

    @Override
    public Map<String, Integer> getTypeMap() {
        Map<String, Integer> typeMap = new HashMap<>();
        typeMap.put("id", 4);
        typeMap.put("account", 12);
        typeMap.put("password", 12);
        typeMap.put("name_eng", 12);
        typeMap.put("name_zh", 12);
        typeMap.put("phone", 12);
        typeMap.put("email", 12);
        typeMap.put("ctime", 93);
        typeMap.put("state", -6);
        return typeMap;
    }

    @Override
    public Map<String, Object> getValueMap() {
        return this.valueMap;
    }


    /**
     * id getter method
     *
     * @return java.lang.Integer
     * @author authorName
     * @createTime 2017-03-03 15:50:16
     */
    public java.lang.Integer getId() {
        return DataTypeUtil.getDataAsInteger(this.valueMap.get("id"));
    }

    /**
     * account getter method
     *
     * @return java.lang.String
     * @author authorName
     * @createTime 2017-03-03 15:50:16
     */
    public java.lang.String getAccount() {
        return DataTypeUtil.getDataAsString(this.valueMap.get("account"));
    }

    /**
     * password getter method
     *
     * @return java.lang.String
     * @author authorName
     * @createTime 2017-03-03 15:50:16
     */
    public java.lang.String getPassword() {
        return DataTypeUtil.getDataAsString(this.valueMap.get("password"));
    }

    /**
     * name_eng getter method
     *
     * @return java.lang.String
     * @author authorName
     * @createTime 2017-03-03 15:50:16
     */
    public java.lang.String getNameEng() {
        return DataTypeUtil.getDataAsString(this.valueMap.get("name_eng"));
    }

    /**
     * name_zh getter method
     *
     * @return java.lang.String
     * @author authorName
     * @createTime 2017-03-03 15:50:16
     */
    public java.lang.String getNameZh() {
        return DataTypeUtil.getDataAsString(this.valueMap.get("name_zh"));
    }

    /**
     * phone getter method
     *
     * @return java.lang.String
     * @author authorName
     * @createTime 2017-03-03 15:50:16
     */
    public java.lang.String getPhone() {
        return DataTypeUtil.getDataAsString(this.valueMap.get("phone"));
    }

    /**
     * email getter method
     *
     * @return java.lang.String
     * @author authorName
     * @createTime 2017-03-03 15:50:16
     */
    public java.lang.String getEmail() {
        return DataTypeUtil.getDataAsString(this.valueMap.get("email"));
    }

    /**
     * ctime getter method
     *
     * @return java.sql.Timestamp
     * @author authorName
     * @createTime 2017-03-03 15:50:16
     */
    public java.sql.Timestamp getCtime() {
        return DataTypeUtil.getDataAsTimestamp(this.valueMap.get("ctime"));
    }

    /**
     * state getter method
     *
     * @return java.lang.Short
     * @author authorName
     * @createTime 2017-03-03 15:50:16
     */
    public java.lang.Short getState() {
        return DataTypeUtil.getDataAsShort(this.valueMap.get("state"));
    }


    /**
     * id setter method
     *
     * @author authorName
     * @createTime 2017-03-03 15:50:16
     */
    public void setId(java.lang.Integer id) {
        this.id = id;
        this.valueMap.put("id", id);
    }


    /**
     * account setter method
     *
     * @author authorName
     * @createTime 2017-03-03 15:50:16
     */
    public void setAccount(java.lang.String account) {
        this.account = account;
        this.valueMap.put("account", account);
    }


    /**
     * password setter method
     *
     * @author authorName
     * @createTime 2017-03-03 15:50:16
     */
    public void setPassword(java.lang.String password) {
        this.password = password;
        this.valueMap.put("password", password);
    }


    /**
     * name_eng setter method
     *
     * @author authorName
     * @createTime 2017-03-03 15:50:16
     */
    public void setNameEng(java.lang.String nameEng) {
        this.nameEng = nameEng;
        this.valueMap.put("name_eng", nameEng);
    }


    /**
     * name_zh setter method
     *
     * @author authorName
     * @createTime 2017-03-03 15:50:16
     */
    public void setNameZh(java.lang.String nameZh) {
        this.nameZh = nameZh;
        this.valueMap.put("name_zh", nameZh);
    }


    /**
     * phone setter method
     *
     * @author authorName
     * @createTime 2017-03-03 15:50:16
     */
    public void setPhone(java.lang.String phone) {
        this.phone = phone;
        this.valueMap.put("phone", phone);
    }


    /**
     * email setter method
     *
     * @author authorName
     * @createTime 2017-03-03 15:50:16
     */
    public void setEmail(java.lang.String email) {
        this.email = email;
        this.valueMap.put("email", email);
    }


    /**
     * ctime setter method
     *
     * @author authorName
     * @createTime 2017-03-03 15:50:16
     */
    public void setCtime(java.sql.Timestamp ctime) {
        this.ctime = ctime;
        this.valueMap.put("ctime", ctime);
    }


    /**
     * state setter method
     *
     * @author authorName
     * @createTime 2017-03-03 15:50:16
     */
    public void setState(java.lang.Short state) {
        this.state = state;
        this.valueMap.put("state", state);
    }
}
