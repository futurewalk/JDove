package cn.com.dove.utils;

import org.apache.commons.collections.map.HashedMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/4/21.
 * 获取总页数
 * author:JackWang
 */
public class QueryUtil {
    private static final Logger logger = LoggerFactory.getLogger(QueryUtil.class);

    /**
     * 获取总页数
     *
     * @param rowCount
     * @param pageSize
     * @return
     */
    public static int getPageCount(int rowCount, int pageSize) {
        return rowCount % pageSize == 0 ? rowCount / pageSize : rowCount / pageSize + 1;
    }

    /**
     * 获取当前页
     *
     * @param rowCount
     * @param pageSize
     * @param currentPage
     * @return
     */
    public static int getPageCurrent(int rowCount, int pageSize, int currentPage) {
        if (currentPage - getPageCount(rowCount, pageSize) == 1) {
            currentPage--;
        }
        if (currentPage - getPageCount(rowCount, pageSize) > 1
                || currentPage < 1) {
            currentPage = 1;
        }
        if (rowCount == 0)
            currentPage = 1;
        return currentPage;
    }
    public static void setPageObjectValue(PageObject po, int rowCount){
        int pageCount = QueryUtil.getPageCount(rowCount, po.getPageSize());//获取总页数
        int pageCurrent = QueryUtil.getPageCurrent(rowCount, po .getPageSize(), po.getPageNum());//获取当前页
        po.setPageCount(pageCount);
        po.setPageNum(pageCurrent);
        po.setRowCount(rowCount);
    }
    public static Map<String, Object> setResult(PageObject po, List<?> objList) {
        Map<String, Object> result = new HashMap<>(2);
        result.put("list", objList);
        result.put("rowCount", po.getRowCount());
        result.put("pageCount",po.getPageCount());
        return result;
    }
    /**
     * 获取查询总页数的sql
     * @param sqlStr
     * @return
     */
    public static String getSQLCount(String sqlStr) {
        StringBuffer sb = new StringBuffer();
        sb.append("SELECT COUNT(1) FROM (");
        sb.append(sqlStr);
        sb.append(" ) as total");
        return sb.toString();
    }
    /**
     * 获取占位符相对应参数封装成Object数组
     * @param paramList
     * @return
     */
    public static Object[] getQueryParams(List paramList) {
        if (paramList == null) {
            return new Object[]{};
        }
        Object[] obj = new Object[paramList.size()];
        for (int i = 0; i < paramList.size(); i++) {
            obj[i] = paramList.get(i);
        }
        return obj;
    }

    /**
     * 生成mySQL分页语句
     *
     * @param sqlStr
     * @param po
     * @return
     */
    public static String getLimitSql(String sqlStr, PageObject po) {
        StringBuilder sb = new StringBuilder(sqlStr);
        sb.append(" LIMIT ").append((po.getPageNum() - 1) * po.getPageSize()).append(",").append(po.getPageSize());
        return sb.toString();
    }

    public static String getFormSQl(String sql) {
        String[] str = sql.split(" ");
        String fromIndex = "";
        int count = 0;
        for (int i = 0; i < str.length; i++) {
            if (str[i].indexOf("FROM") > -1 || str[i].indexOf("from") > -1) {
                fromIndex += i + ",";
                count++;
            }
        }
        if (count == 1) {
            return sql;
        }
        return "";
    }

}
