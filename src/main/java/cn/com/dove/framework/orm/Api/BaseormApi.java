package cn.com.dove.framework.orm.Api;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by wangdequan on 2018/3/13.
 */
public interface BaseormApi {
   /**
    * commit transation
    */
   void commit() throws SQLException;

   /**
    * rollBack transation
    */
   void rollBack() throws SQLException;

   /**
    * start transaction
    */
   void beginTransaction() throws SQLException;

   void beginTransaction(boolean autoEnd) throws SQLException;

   /**
    * end transaction
    * @throws SQLException
    */
   void endTx() throws SQLException;

   /**
    * close a connection
    */
   void close() throws SQLException;

   /**
    * judge connect is close
    * @return
    */
   boolean isClose();

   /**
    * afford a execute method to do something not like save ,update,delete
    * @param sql
    * @param values
    * @return
    * @throws SQLException
    */
   long execute(String sql,String statment,Object...values) throws SQLException;

   void release() throws SQLException;

   boolean transactionStatus();
   /**
    * get a connection
    * @return
    */
   Connection getConnect() throws SQLException;

   //Query query(Object object);

}
