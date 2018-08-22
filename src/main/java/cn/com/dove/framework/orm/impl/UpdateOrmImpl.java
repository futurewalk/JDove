package cn.com.dove.framework.orm.impl;

import cn.com.dove.framework.em.CRUD;
import cn.com.dove.framework.orm.Api.IUpdateOrm;
import cn.com.dove.framework.orm.OrmHelper;
import cn.com.dove.framework.orm.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public  class UpdateOrmImpl extends QuerySelectImpl implements IUpdateOrm {
    private static final Logger logger = LoggerFactory.getLogger(UpdateOrmImpl.class);
    private static final String UPDATE_STATEMENT = "Update";
    private static final String DELETE_STATEMENT = "Delete";
    private static final String INSERT_STATEMENT = "Insert";

    public UpdateOrmImpl(SessionFactory.SessionHolder sessionHolder) {
        super(sessionHolder);
    }

    @Override
    public long save(String sql, Object... values) throws SQLException {
        return execute(sql,INSERT_STATEMENT,values);
    }


    @Override
    public long save(Object object) throws SQLException {
        List paraList = new ArrayList<>();
        String sql = OrmHelper.createSaveSQL(object,paraList, CRUD.SAVE);
        return execute(sql,INSERT_STATEMENT,paraList.toArray());
    }

    @Override
    public long delete(String sql, Object...values) throws SQLException {
        return this.update(sql,values);
    }

    @Override
    public long delete(Object object) throws SQLException {
        if (object == null){
            throw new SQLException("class object is null");
        }
        try {
            List paraList = new ArrayList<>();
            String sql = OrmHelper.createDeleteSQL(object,paraList);
            return execute(sql,DELETE_STATEMENT,paraList.toArray());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public long update(String sql, Object... values) throws SQLException {

        try{
            return execute(sql,DELETE_STATEMENT,values);
        }catch(Exception e){
            throw e;
        }
    }

    @Override
    public long update(Object object) throws Exception {
        try {
            List paraList = new ArrayList<>();
            String sql = OrmHelper.createUpdateQL(object,paraList);
            return execute(sql,UPDATE_STATEMENT,paraList.toArray());
        } catch (Exception e) {
            throw e;
        }
    }
}