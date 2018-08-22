package cn.com.dove.framework.orm.impl;

import cn.com.dove.framework.orm.Api.IQuerySelect;
import cn.com.dove.framework.orm.OrmHelper;
import cn.com.dove.framework.orm.ResultSetUtils;
import cn.com.dove.framework.orm.SessionFactory;
import cn.com.dove.utils.PageObject;
import cn.com.dove.utils.QueryUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class QuerySelectImpl implements IQuerySelect {
    private static final Logger logger = LoggerFactory.getLogger(QuerySelectImpl.class);

    private  Connection conn = null;
    private static boolean isClose = false;
    private static boolean beginTransaction = false;
    private static boolean autoEndTransaction = true;
    private SessionFactory.SessionHolder sessionHolder = null;
    private static final String QUERY_STATEMENT = "Query";

    public QuerySelectImpl (SessionFactory.SessionHolder sessionHolder){
        this.conn = sessionHolder.getConnection();
        this.sessionHolder = sessionHolder;
    }
    protected SessionFactory.SessionHolder getSessionHolder(){
        return this.sessionHolder;
    }

    public boolean  transactionStatus(){
        return this.beginTransaction;
    }

    @Override
    public void commit() throws SQLException {

        if (this.beginTransaction && this.autoEndTransaction){
            this.getConnect().commit();
            beginTransaction = false;
        }
    }
    @Override
    public  void rollBack() throws SQLException {
        if (this.beginTransaction){
            this.getConnect().rollback();
            beginTransaction = false;
        }
    }
    @Override
    public void beginTransaction() throws SQLException {
        try{
            Connection connection = this.getConnect();
            connection.setAutoCommit(false);
            this.beginTransaction = true;

        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @Override
    public void beginTransaction(boolean autoEnd) throws SQLException {
        try{
            Connection connection = this.getConnect();
            connection.setAutoCommit(false);
            this.beginTransaction = true;

        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @Override
    public void endTx() throws SQLException{
		beginTransaction = false;
		if (this.beginTransaction){
			beginTransaction = false;
		}
    }

    @Override
    public void close() throws SQLException {
        if (!isClose){
            this.getConnect().close();
            isClose = false;
        }
    }

    @Override
    public boolean isClose() {
        return this.isClose;
    }

    @Override
    public void release() throws SQLException {
        commit();
        close();
    }
    @Override
    public long execute(String sql,String statment,Object...values) throws SQLException {
        PreparedStatement statement = null;
        ResultSet rs = null;

        long start = System.currentTimeMillis();
        long retNum = 0L;
        try{
            statement = this.getConnect().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            OrmHelper.prepareParameters(statement,values);
            statement.executeUpdate();
            rs = statement.getGeneratedKeys();

            OrmHelper.writeLog(start,statment,sql,true,values);
            retNum = rs.next()?rs.getLong(1):0L;
        }catch(Exception e){
            OrmHelper.writeLog(start,statment,sql,false,values);
            throw e;
        }finally {
            try {
                OrmHelper.close(statement,rs);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return retNum;
    }


    @Override
    public Connection getConnect() throws SQLException {
        if (this.conn == null){
            throw new SQLException("connection is null");
        }
        return this.conn;
    }
    @Override
    public Object value(String sql,Object...values) throws SQLException {
        PreparedStatement statement = null;
        ResultSet rs = null;
        long start = System.currentTimeMillis();
        try{
            statement = this.getConnect().prepareStatement(sql);
            OrmHelper.prepareParameters(statement,values);
            rs = statement.executeQuery();

            Object obj = rs.next()?rs.getObject(1):null;
            OrmHelper.writeLog(start,QUERY_STATEMENT,sql,true,values);
            return obj;
        }catch(Exception e){
            OrmHelper.writeLog(start,QUERY_STATEMENT,sql,false,values);
            throw e;
        }finally {
            try {
                OrmHelper.close(statement,rs);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public ResultSet resultSet(String sql, Object... values) throws SQLException {
        PreparedStatement statement = null;
        ResultSet rs = null;

        long start = System.currentTimeMillis();
        try{
            statement = this.getConnect().prepareStatement(sql);
            OrmHelper.prepareParameters(statement,values);
            rs = statement.executeQuery();

            OrmHelper.writeLog(start,QUERY_STATEMENT,sql,true,values);
        }catch(Exception e){
            OrmHelper.writeLog(start,QUERY_STATEMENT,sql,true,values);
            throw e;
        }finally {
            try {
                OrmHelper.close(statement,rs);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return rs;
    }

    @Override
    public <T> T rowToEntity(String sql, Class<T> klass, Object... values) throws Exception {

        PreparedStatement statement = null;
        ResultSet rs = null;

        long start = System.currentTimeMillis();
        try{
            statement = this.getConnect().prepareStatement(sql);
            OrmHelper.prepareParameters(statement,values);
            rs = statement.executeQuery();

            T instance  = null;

            Map<String,Field> filedMap = OrmHelper.classFiledMap(klass);
            ResultSetMetaData meta = rs.getMetaData();
            int columnCount = meta.getColumnCount();
            while (rs.next()){
                instance  = klass.newInstance();
                for (int i = 1;i <= columnCount;i++){
                    Object value = rs.getObject(meta.getColumnLabel(i));
                    Field field = filedMap.get(meta.getColumnLabel(i));
                    instance = OrmHelper.setValue(instance,value,field.getName());
                }
            }
            OrmHelper.writeLog(start,QUERY_STATEMENT,sql,true,values);
            return instance;

        }catch(Exception e){
            OrmHelper.writeLog(start,QUERY_STATEMENT,sql,false,values);
            throw e;
        }finally {
            try {
                OrmHelper.close(statement,rs);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public Object[] row(String sql, Object... values) throws SQLException {
        PreparedStatement statement = null;
        ResultSet rs = null;
        Object[] objects = new Object[]{};

        long start = System.currentTimeMillis();
        try{
            statement = this.getConnect().prepareStatement(sql);
            OrmHelper.prepareParameters(statement,values);
            rs = statement.executeQuery();

            for (int i = 0;rs.next();i++){
                objects[i] = rs.getObject(i);
            }
            OrmHelper.writeLog(start,QUERY_STATEMENT,sql,true,values);
        }catch(Exception e){
            OrmHelper.writeLog(start,QUERY_STATEMENT,sql,false,values);
            throw e;
        }finally {
            try {
                OrmHelper.close(statement,rs);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return objects;
    }

    @Override
    public <T> List<T> valuesObject(String sql, Class<T> clazz, Object... values) throws Exception {
        PreparedStatement statement = null;
        ResultSet rs = null;

        List<T> listObject = new ArrayList<T>();
        long start = System.currentTimeMillis();
        try{
            statement = this.getConnect().prepareStatement(sql);
            OrmHelper.prepareParameters(statement,values);
            rs = statement.executeQuery();

            Map<String,Field> filedMap = OrmHelper.classFiledMap(clazz);
            ResultSetMetaData meta = rs.getMetaData();
            int columnCount = meta.getColumnCount();

            while (rs.next()){
                T instance  = clazz.newInstance();
                for (int i = 1;i <= columnCount;i++){
                    String columnName = meta.getColumnLabel(i);
                    Object value = rs.getObject(columnName);
                    Field field =  filedMap.get(columnName);
                    if(field == null){
                        throw new NoSuchFieldException("can't find " + columnName + " field in "+ clazz);
                    }
                    instance = OrmHelper.setValue(instance,value,field.getName());
                }
                listObject.add(instance);
            }
            OrmHelper.writeLog(start,QUERY_STATEMENT,sql,true,values);
        }catch(Exception e){
            OrmHelper.writeLog(start,QUERY_STATEMENT,sql,false,values);
            throw e;
        }finally {
            try {
                OrmHelper.close(statement,rs);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return listObject;
    }

    @Override
    public List<Object[]> valuesList(String sql, Object... values) {
        PreparedStatement statement = null;
        ResultSet rs = null;
        List<Object[]> objects = new ArrayList<>();

        long start = System.currentTimeMillis();
        try{
            statement = this.getConnect().prepareStatement(sql);
            OrmHelper.prepareParameters(statement,values);
            rs = statement.executeQuery();

            ResultSetMetaData meta = rs.getMetaData();
            int columnCount = meta.getColumnCount();

            while (rs.next()){
                Object[] obj = new Object[columnCount];
                for (int i = 1;i <= columnCount;i++){
                    Object value = rs.getObject(i);
                    obj[i-1] = value;
                }
                objects.add(obj);
            }
            OrmHelper.writeLog(start,QUERY_STATEMENT,sql,true,values);
        }catch(Exception e){
            OrmHelper.writeLog(start,QUERY_STATEMENT,sql,false,values);
            e.printStackTrace();
        }finally {
            try {
                OrmHelper.close(statement,rs);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return objects;
    }

    @Override
    public List<Map<String, Object>> rowsToMap(String sql, Object... values) {
        PreparedStatement statement = null;
        ResultSet rs = null;

        List<Map<String,Object>> listMap = new ArrayList<>();
        long start = System.currentTimeMillis();
        try{
            statement = this.getConnect().prepareStatement(sql);
            OrmHelper.prepareParameters(statement,values);
            rs = statement.executeQuery();
            listMap = ResultSetUtils.handlerList(rs,listMap);

            OrmHelper.writeLog(start,QUERY_STATEMENT,sql,true,values);
        }catch(Exception e){
            OrmHelper.writeLog(start,QUERY_STATEMENT,sql,false,values);
            e.printStackTrace();
        }finally {
            try {
                OrmHelper.close(statement,rs);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return listMap;
    }

    @Override
    public Map<String, Object> map(String sql, Object... values) throws SQLException {
        PreparedStatement statement = null;
        ResultSet rs = null;

        Map<String,Object> map = null;
        long start = System.currentTimeMillis();
        try{
            statement = this.getConnect().prepareStatement(sql);
            OrmHelper.prepareParameters(statement,values);
            rs = statement.executeQuery();

            map = ResultSetUtils.map(rs);

            OrmHelper.writeLog(start,QUERY_STATEMENT,sql,true,values);
        }catch(Exception e){
            OrmHelper.writeLog(start,QUERY_STATEMENT,sql,false,values);
            throw e;
        }finally {
            try {
                OrmHelper.close(statement,rs);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return map;
    }
    @Override
    public List<Object[]> queryPageList(String sql, PageObject po,Object... values) throws SQLException {
        return this.valuesList(generatePageSQl(sql,po,values),values);
    }

    @Override
    public <T> List<T> pageListObject(String sql, PageObject po,Class<T> klass,Object... values) throws Exception {
        return  this.valuesObject(generatePageSQl(sql,po,values),klass,values);
    }

    @Override
    public List<Map<String, Object>> getPageMap(String sql, PageObject po, Object... values) throws SQLException {
        String pageSQl = generatePageSQl(sql,po,values);
        return this.rowsToMap(pageSQl,values);
    }
    private String generatePageSQl(String sql,PageObject po,Object...values) throws SQLException {
        Object rowCount = value(QueryUtil.getSQLCount(sql),values);
        QueryUtil.setPageObjectValue(po,Integer.valueOf(String.valueOf(rowCount)).intValue());
        return QueryUtil.getLimitSql(sql,po);
    }
}