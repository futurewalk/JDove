package cn.com.dove.framework.orm.impl;

import cn.com.dove.framework.orm.Api.Query;
import cn.com.dove.framework.orm.OrmHelper;
import cn.com.dove.framework.orm.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

public abstract class QueryImpl extends QuerySelectImpl implements Query {
    private static final Logger logger = LoggerFactory.getLogger(QueryImpl.class);
    private static final String[] WHER_STATMENT = {"WHERE","Where","where",""};

    private transient /*volatile*/ StringBuilder  SQL_BUILDER = null;

    public QueryImpl(SessionFactory.SessionHolder sessionHolder) {
        super(sessionHolder);
    }

  /*  @Override
    public Query query(Object object) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT * FROM ").append(OrmHelper.tableName(object.getClass()));
        SQL_BUILDER = sb;
        return this;
    }*/
    @Override
    public Query and(Object object){
        return this;
    }
    @Override
    public Query gt(Object object){
        return this;
    }
    @Override
    public Query lt(Object object){
        return this;
    }
    @Override
    public Query gte(Object object){
        return this;
    }
    @Override
    public Query lte(Object object){
        return this;
    }
    @Override
    public Query in(Object object){
        return this;
    }

    public Query eq(Object object){
        if (!OrmHelper.contains(SQL_BUILDER.toString(),WHER_STATMENT)){
            SQL_BUILDER.append(" WHERE ");
        }
        return this;
    }

    @Override
    public Query limit(int start,int end){
        return this;
    }
    @Override
    public Query raw(String sql, Object...values){
        return this;
    }
    public <T> List<T> AllList(Class<T> klass){
        return null;
    }
    public List<Map<String,Object>> AllMap(){
        return null;
    }

}