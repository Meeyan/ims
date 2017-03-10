package utils;


import com.mims.util.TimeUtil;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.*;

/**
 * 数据库表转换成javaBean对象小工具(已用了很长时间),
 * 1 bean属性按原始数据库字段经过去掉下划线,并大写处理首字母等等.
 * 2 生成的bean带了数据库的字段说明.
 * 3 各位自己可以修改此工具用到项目中去.
 */
public class MyEntityUtils {
    private String tablename = "";
    private String[] colnames;
    private String[] colTypes;
    private int[] colSizes; // 列名大小
    private int[] colScale; // 列名小数精度
    private boolean importUtil = false;
    private boolean importSql = false;
    private boolean importMath = false;
    private StringBuffer content = new StringBuffer();


    private List fieldList = new ArrayList();

    /**
     * @param tName
     */
    public void tableToEntity(String tName) {
        tablename = tName;
        Connection conn = null;
        String strsql = "SELECT * FROM " + tablename + " WHERE 1=2 ";//+" WHERE ROWNUM=1" 读一行记录;
        try {
            // 读取配置文件
            Resource resource = new ClassPathResource("configs/dbconfig.properties");
            Properties properties = PropertiesLoaderUtils.loadProperties(resource);

            // 加载驱动
            Class.forName(properties.getProperty("driverClassName"));
            conn = DriverManager.getConnection(properties.getProperty("url"), properties.getProperty("username"), properties.getProperty("password"));


            System.out.println(strsql);
            PreparedStatement pstmt = conn.prepareStatement(strsql);
            pstmt.executeQuery();
            ResultSetMetaData rsmd = pstmt.getMetaData();
            int size = rsmd.getColumnCount(); // 共有多少列
            colnames = new String[size];
            colTypes = new String[size];
            colSizes = new int[size];
            colScale = new int[size];
            for (int i = 1; i <= size; i++) {
                Map tmpMap = new HashMap();
                String columnLabel = rsmd.getColumnLabel(i);
                String columnClassName = rsmd.getColumnClassName(i);   // java中的属性类型
                String columnName = rsmd.getColumnName(i);  // 列名
                int columnType = rsmd.getColumnType(i);     // 类型，int
                String columnTypeName = rsmd.getColumnTypeName(i);  // 类型，对应数据库
                int columnSize = rsmd.getPrecision(i);

                tmpMap.put("JavaColumnType", columnClassName);
                tmpMap.put("JavaColumnName", columnName);
                tmpMap.put("JavaTypeInt", columnType);
                tmpMap.put("JavaTypeDb", columnTypeName);
                tmpMap.put("JavaColumnSize", columnSize);

                //                colnames[i] = rsmd.getColumnName(i).toLowerCase();
                //                colTypes[i] = rsmd.getColumnTypeName(i).toLowerCase();
                //                colScale[i] = rsmd.getScale(i);
                //                if ("datetime".equals(colTypes[i])) {
                //                    importUtil = true;
                //                }
                //                if ("image".equals(colTypes[i]) || "text".equals(colTypes[i])) {
                //                    importSql = true;
                //                }
                //                if (colScale[i] > 0) {
                //                    importMath = true;
                //                }
                fieldList.add(tmpMap);
            }

            // 第一步，生成导入头，即import部分
            genImportInfo(content);

            // 第二步，生成类名即，public class ......驼峰命名
            genClassName(content, tablename);

            // 第三步，生成共有常量字段名，是不是可以不要

            // 第四步，生成私有属性
            genPrivatePro(content);

            // 第5步，记录表名
            genGetTableMethod(content);

            // 第6步，生成所有的字段map，对应类型
            genFieldTypeMap(content);

            // 第7步，生成获取值数组
            genValueMap(content);

            // 第8步，生成get方法，
            genGetMethod(content);

            // 第9步，生成set方法
            genSetMethod(content);

            // 第10步，尾部
            genClassFooter(content);

            String contStr = content.toString();
            try {
                FileWriter fw = new FileWriter(getMethodName(convertTuoFeng(tablename)) + ".java");
                PrintWriter pw = new PrintWriter(fw);
                pw.println(contStr);
                pw.flush();
                pw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 生成导入信息
     *
     * @param sb
     */
    public void genImportInfo(StringBuffer sb) {
        sb.append("import java.io.Serializable ;\r\n");
        sb.append("import com.mims.util.DataTypeUtil ;\r\n");
        sb.append("import java.util.HashMap ;\r\n");
        sb.append("import java.util.Map ;\r\n");
        for (Object obj : fieldList) {
            Map tmpMap = (Map) obj;
            sb.append("import " + tmpMap.get("JavaColumnType").toString() + " ;\r\n");
        }
    }

    /**
     * 生成表名
     *
     * @param stringBuffer
     * @param tableName
     */
    public void genClassName(StringBuffer stringBuffer, String tableName) {
        String className = getMethodName(convertTuoFeng(tableName));
        String time = TimeUtil.getCurrentTimeStr();
        stringBuffer.append("\r\n/**\r\n");
        stringBuffer.append(" * 类生成以后，请执行类导入优化操作和格式化操作，保证代码格式统一 \r\n");
        stringBuffer.append(" * @author authorName\r\n");
        stringBuffer.append(" * @createTime " + time + "\r\n");
        stringBuffer.append(" */ \r\n");
        stringBuffer.append("public class " + className + " implements BaseBean, Serializable {\r\n");
    }

    /**
     * 生成私有属性
     *
     * @param stringBuffer
     */
    public void genPrivatePro(StringBuffer stringBuffer) {
        stringBuffer.append("\t private Map<String, Object> valueMap = new HashMap<>(); \r\n ");
        for (Object obj : fieldList) {
            Map tmpMap = (Map) obj;
            stringBuffer.append(" \t private  " + tmpMap.get("JavaColumnType").toString() + "  " + convertTuoFeng(tmpMap.get("JavaColumnName").toString()) + " ;\r\n");
        }
    }

    /**
     * 生成所有字段对应的db数据类型
     *
     * @param stringBuffer
     */
    public void genFieldTypeMap(StringBuffer stringBuffer) {

        stringBuffer.append("\t @Override \r\n ");
        stringBuffer.append("\t public Map<String, Integer> getTypeMap() { \r\n ");
        stringBuffer.append("\t\t Map<String, Integer> typeMap = new HashMap<>(); \r\n ");
        for (Object obj : fieldList) {
            Map tmpMap = (Map) obj;
            stringBuffer.append("\t\t typeMap.put(\"" + tmpMap.get("JavaColumnName").toString() + "\"," + tmpMap.get("JavaTypeInt") + "); \r\n");
        }
        stringBuffer.append("\t\t return typeMap; \r\n");
        stringBuffer.append("\t }");
        stringBuffer.append("\r\n");
    }

    /**
     * 生成valueMap
     *
     * @param stringBuffer
     */
    public void genValueMap(StringBuffer stringBuffer) {
        stringBuffer.append("\r\n\t @Override \r\n ");
        stringBuffer.append("\t public Map<String, Object> getValueMap() { \r\n ");
        stringBuffer.append("\t\t return this.valueMap; \r\n");
        stringBuffer.append("\t }");
        stringBuffer.append("\r\n");
    }


    /**
     * 生成获取表名的方法
     */
    public void genGetTableMethod(StringBuffer stringBuffer) {
        String time = TimeUtil.getCurrentTimeStr();
        stringBuffer.append("\r\n");
        stringBuffer.append("\r\n\t/**\r\n");
        stringBuffer.append("\t * get bean table name\r\n");
        stringBuffer.append("\t * @return String\r\n");
        stringBuffer.append("\t * @author authorName\r\n");
        stringBuffer.append("\t * @createTime " + time + "\r\n");
        stringBuffer.append("\t */ \r\n");
        stringBuffer.append("\t@Override\r\n ");
        stringBuffer.append("\tpublic String getTableName() {\r\n ");
        stringBuffer.append("\t\t return \"" + tablename + "\";\r\n ");
        stringBuffer.append("\t}\r\n ");
    }

    /**
     * 生成set方法
     *
     * @param stringBuffer
     */
    public void genSetMethod(StringBuffer stringBuffer) {
        String time = TimeUtil.getCurrentTimeStr();
        for (Object obj : fieldList) {
            stringBuffer.append("\r\n");
            Map tmpMap = (Map) obj;
            String colName = tmpMap.get("JavaColumnName").toString();   // 原始列名
            String javaColumnType = tmpMap.get("JavaColumnType").toString();
            String conColName = convertTuoFeng(colName);  // 转换成驼峰方式

            stringBuffer.append("\r\n");
            stringBuffer.append("\r\n\t/**\r\n");
            stringBuffer.append("\t * " + colName + " setter method \r\n");
            stringBuffer.append("\t * @author authorName\r\n");
            stringBuffer.append("\t * @createTime " + time + "\r\n");
            stringBuffer.append("\t */ \r\n");
            stringBuffer.append("\t public void set").append(getMethodName(conColName) + " (").append(javaColumnType + " " + conColName + ") { \r\n");
            stringBuffer.append("\t\t this." + conColName + " = " + conColName + "; \r\n");
            stringBuffer.append("\t\t this.valueMap.put(\"" + colName + "\"" + "," + conColName + ");\r\n");
            stringBuffer.append("\t }");
        }
    }

    /**
     * @param stringBuffer
     */
    public void genGetMethod(StringBuffer stringBuffer) {
        String time = TimeUtil.getCurrentTimeStr();
        for (Object obj : fieldList) {
            Map tmpMap = (Map) obj;
            String colName = tmpMap.get("JavaColumnName").toString();   // 原始列名
            String javaColumnType = tmpMap.get("JavaColumnType").toString(); // 类型-java
            int javaTypeInt = Integer.parseInt(tmpMap.get("JavaTypeInt").toString()); // 类型-java
            String conColName = convertTuoFeng(colName);  // 转换成驼峰方式

            stringBuffer.append("\r\n");
            stringBuffer.append("\r\n\t/**\r\n");
            stringBuffer.append("\t * " + colName + " getter method \r\n");
            stringBuffer.append("\t * @return " + javaColumnType + "\r\n");
            stringBuffer.append("\t * @author authorName\r\n");
            stringBuffer.append("\t * @createTime " + time + "\r\n");
            stringBuffer.append("\t */ \r\n");

            stringBuffer.append("\t public " + javaColumnType + " get").append(getMethodName(conColName) + " ( ) { \r\n");
            if (javaTypeInt == Types.DECIMAL || javaTypeInt == Types.FLOAT || javaTypeInt == Types.DOUBLE) {
                stringBuffer.append("\t\t return DataTypeUtil.getDataAsBigDecimal(this.valueMap.get(\"" + colName + "\"));\r\n");
            } else if (javaTypeInt == Types.INTEGER) {
                stringBuffer.append("\t\t return DataTypeUtil.getDataAsInteger(this.valueMap.get(\"" + colName + "\"));\r\n");
            } else if (javaTypeInt == Types.VARCHAR || javaTypeInt == Types.LONGVARCHAR) {
                stringBuffer.append("\t\t return DataTypeUtil.getDataAsString(this.valueMap.get(\"" + colName + "\"));\r\n");
            } else if (javaTypeInt == Types.TIMESTAMP) {
                stringBuffer.append("\t\t return DataTypeUtil.getDataAsTimestamp(this.valueMap.get(\"" + colName + "\"));\r\n");
            } else if (javaTypeInt == Types.TINYINT) {
                stringBuffer.append("\t\t return DataTypeUtil.getDataAsShort(this.valueMap.get(\"" + colName + "\"));\r\n");
            }
            stringBuffer.append("\t }");
        }
    }

    public void genClassFooter(StringBuffer stringBuffer) {
        stringBuffer.append("\r\n}");
    }

    /**
     * 生成方法名，也可以用来生成类名
     *
     * @param param
     * @return
     */
    public String getMethodName(String param) {
        return param.substring(0, 1).toUpperCase() + param.substring(1);
    }


    /**
     * 解析处理(生成实体类主体代码)
     */
    private String parse(String[] colNames, String[] colTypes, int[] colSizes) {
        StringBuffer sb = new StringBuffer();
        sb.append("\r\nimport java.io.Serializable;\r\n");
        if (importUtil) {
            sb.append("import java.util.Date;\r\n");
        }
        if (importSql) {
            sb.append("import java.sql.*;\r\n\r\n");
        }
        if (importMath) {
            sb.append("import java.math.*;\r\n\r\n");
        }
        //表注释
        processColnames(sb);
        sb.append("public class " + initcap(tablename) + " implements Serializable {\r\n");
        processAllAttrs(sb);
        processAllMethod(sb);
        sb.append("}\r\n");
        System.out.println(sb.toString());
        return sb.toString();
    }

    /**
     * 处理列名,把空格下划线'_'去掉,同时把下划线后的首字母大写
     * 要是整个列在3个字符及以内,则去掉'_'后,不把"_"后首字母大写.
     * 同时把数据库列名,列类型写到注释中以便查看,
     *
     * @param sb
     */
    private void processColnames(StringBuffer sb) {
        sb.append("\r\n/** " + tablename + "\r\n");
        String colsiz = "";
        String colsca = "";
        for (int i = 0; i < colnames.length; i++) {
            colsiz = colSizes[i] <= 0 ? "" : (colScale[i] <= 0 ? "(" + colSizes[i] + ")" : "(" + colSizes[i] + "," + colScale[i] + ")");
            sb.append("\t" + colnames[i].toUpperCase() + "	" + colTypes[i].toUpperCase() + colsiz + "\r\n");
            char[] ch = colnames[i].toCharArray();
            char c = 'a';
            if (ch.length > 3) {
                for (int j = 0; j < ch.length; j++) {
                    c = ch[j];
                    if (c == '_') {
                        if (ch[j + 1] >= 'a' && ch[j + 1] <= 'z') {
                            ch[j + 1] = (char) (ch[j + 1] - 32);
                        }
                    }
                }
            }
            String str = new String(ch);
            colnames[i] = str.replaceAll("_", "");
        }
        sb.append("*/\r\n");
    }

    /**
     * 生成所有的方法
     *
     * @param sb
     */
    private void processAllMethod(StringBuffer sb) {
        for (int i = 0; i < colnames.length; i++) {
            sb.append("\tpublic void set" + initcap(colnames[i]) + "("
                    + oracleSqlType2JavaType(colTypes[i], colScale[i], colSizes[i]) + " " + colnames[i]
                    + "){\r\n");
            sb.append("\t\tthis." + colnames[i] + "=" + colnames[i] + ";\r\n");
            sb.append("\t}\r\n");

            sb.append("\tpublic " + oracleSqlType2JavaType(colTypes[i], colScale[i], colSizes[i]) + " get"
                    + initcap(colnames[i]) + "(){\r\n");
            sb.append("\t\treturn " + colnames[i] + ";\r\n");
            sb.append("\t}\r\n");
        }
    }

    /**
     * 感谢网友wxiaobin 提供下面的这个方法.这个方法更为高效
     */
    public static String convertTuoFeng(String paraName) {
        StringBuilder result = new StringBuilder();
        // 快速检查
        if (paraName == null || paraName.isEmpty()) {
            // 没必要转换
            return "";
        } else if (!paraName.contains("_")) {
            // 不含下划线，仅将首字母小写
            return paraName.substring(0, 1).toLowerCase() + paraName.substring(1);
        } else {
            // 用下划线将原始字符串分割
            String[] columns = paraName.split("_");
            for (String columnSplit : columns) {
                // 跳过原始字符串中开头、结尾的下换线或双重下划线
                if (columnSplit.isEmpty()) {
                    continue;
                }
                // 处理真正的驼峰片段
                if (result.length() == 0) {
                    // 第一个驼峰片段，全部字母都小写
                    result.append(columnSplit.toLowerCase());
                } else {
                    // 其他的驼峰片段，首字母大写
                    result.append(columnSplit.substring(0, 1).toUpperCase()).append(columnSplit.substring(1).toLowerCase());
                }
            }
            return result.toString();
        }
    }

    /**
     * 解析输出属性
     *
     * @return
     */
    private void processAllAttrs(StringBuffer sb) {
        sb.append("\tprivate static final long serialVersionUID = 1L;\r\n");
        for (int i = 0; i < colnames.length; i++) {
            sb.append("\tprivate " + oracleSqlType2JavaType(colTypes[i], colScale[i], colSizes[i]) + " "
                    + colnames[i] + ";\r\n");
        }
        sb.append("\r\n");
    }

    /**
     * 把输入字符串的首字母改成大写
     *
     * @param str
     * @return
     */
    private String initcap(String str) {
        char[] ch = str.toCharArray();
        if (ch[0] >= 'a' && ch[0] <= 'z') {
            ch[0] = (char) (ch[0] - 32);
        }
        return new String(ch);
    }

    /**
     * Oracle
     *
     * @param sqlType
     * @param scale
     * @return
     */
    private String oracleSqlType2JavaType(String sqlType, int scale, int size) {
        if (sqlType.equals("integer")) {
            return "Integer";
        } else if (sqlType.equals("long")) {
            return "Long";
        } else if (sqlType.equals("float")
                || sqlType.equals("float precision")
                || sqlType.equals("double")
                || sqlType.equals("double precision")
                ) {
            return "BigDecimal";
        } else if (sqlType.equals("number")
                || sqlType.equals("decimal")
                || sqlType.equals("numeric")
                || sqlType.equals("real")) {
            return scale == 0 ? (size < 10 ? "Integer" : "Long") : "BigDecimal";
        } else if (sqlType.equals("varchar")
                || sqlType.equals("varchar2")
                || sqlType.equals("char")
                || sqlType.equals("nvarchar")
                || sqlType.equals("nchar")) {
            return "String";
        } else if (sqlType.equals("datetime")
                || sqlType.equals("date")
                || sqlType.equals("timestamp")) {
            return "Date";
        }
        return null;
    }

    /**
     * @param args
     */
    public static void main(String[] args) throws ClassNotFoundException {
        MyEntityUtils t = new MyEntityUtils();
        t.tableToEntity("ad_user");
    }

}
