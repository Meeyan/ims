package com.mims.util;

import org.apache.log4j.Logger;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.regex.Pattern;

public class DataTypeUtil {

    private static final Logger log = Logger.getLogger(DataTypeUtil.class);

    /**
     * 将object转为timestamp，null还是null
     *
     * @param para
     * @return Timestamp or null
     * @author zhaojy
     * @createTime 2017-03-03
     */
    public static Timestamp getDataAsTimestamp(Object para) {
        if (null != para) {
            String objStr = para.toString();
            // yyyy-MM-dd hh:mm:ss格式
            String reg = "[0-9]{4}-[0-9]{2}-[0-9]{2} [0-9]{2}:[0-9]{2}:[0-9]{2}";
            if (Pattern.matches(reg, objStr)) {
                try {
                    return Timestamp.valueOf(para.toString());
                } catch (IllegalArgumentException e) {
                    log.error(e.getMessage());
                    return null;
                }
            }
        }
        return null;
    }

    /**
     * 转换为int
     *
     * @param obj
     * @return
     * @author zhaojy
     * @createTime 2017-03-03
     */
    public static int getDataAsInt(Object obj) {
        if (null != obj) {
            String objStr = obj.toString();
            String reg = "(-|\\+)?[0-9]{0,11}"; // 负数
            if (Pattern.matches(reg, objStr)) {
                try {
                    return Integer.parseInt(objStr.trim());
                } catch (IllegalArgumentException e) {
                    log.error(e.getMessage());
                }
            }
        }
        return 0;
    }

    /**
     * 数据转换 - Integer
     *
     * @param obj
     * @return
     * @author zhaojy
     * @createTime 2017-03-03
     */
    public static Integer getDataAsInteger(Object obj) {
        if (null != obj) {
            String objStr = obj.toString();
            String reg = "(-|\\+)?[0-9]{0,11}";
            if (Pattern.matches(reg, objStr)) {
                try {
                    return Integer.valueOf(objStr.trim());
                } catch (IllegalArgumentException e) {
                    log.error(e.getMessage());
                }
            }
        }
        return 0;
    }

    /**
     * 数据转换-String
     *
     * @param obj
     * @return
     * @author zhaojy
     * @createTime 2017-03-03
     */
    public static String getDataAsString(Object obj) {
        if (null != obj) {
            return obj.toString();
        }
        return null;
    }

    /**
     * 数据转换-BigDecimal
     *
     * @param obj
     * @return
     * @author zhaojy
     * @createTime 2017-03-03
     */
    public static BigDecimal getDataAsBigDecimal(Object obj) {
        if (null != obj) {
            try {
                return new BigDecimal(obj.toString().trim());
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }
        return null;
    }

    /**
     * 数据转换-Short
     *
     * @param obj
     * @return
     * @author zhaojy
     * @createTime 2017-03-03
     */
    public static Short getDataAsShort(Object obj) {
        if (null != obj) {
            try {
                return Short.parseShort(obj.toString().trim());
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }
        return null;
    }

    public static void main(String[] args) {
        Object obj = "   ";
        System.out.println(DataTypeUtil.getDataAsInteger(obj));
    }
}
