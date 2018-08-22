package cn.com.dove.framework.orm.Api;

import java.util.List;
import java.util.Map;

public interface Query extends BaseormApi {

    /**
     * query by set where statement
     */
    Query and(Object object);
    Query gt(Object object);
    Query lt(Object object);
    Query gte(Object object);
    Query lte(Object object);
    Query in(Object object);
    Query eq(Object object);
    <T> List<T> AllList(Class<T> klass);
    List<Map<String,Object>> AllMap();
    Query limit(int start,int end);
    Query raw(String sql,Object...values);
}