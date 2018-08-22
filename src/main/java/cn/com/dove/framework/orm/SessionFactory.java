package cn.com.dove.framework.orm;

import cn.com.dove.exception.DBAccessException;
import cn.com.dove.framework.orm.Api.Orm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class SessionFactory {
   private static final Logger logger = LoggerFactory.getLogger(SessionFactory.class);
   private static ThreadLocal<Map<String,SessionHolder>> SESSION_HOLDER = new ThreadLocal<>();

    public static Orm newOrm(){
        try {
            return newOrm("default");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static Orm newOrm(String sourName) throws SQLException {

         Map<String,SessionHolder> sessionHolderMap = SESSION_HOLDER.get();

         if (sessionHolderMap == null){
            sessionHolderMap = new HashMap<>();
         }
         SessionHolder sessionHolder = sessionHolderMap.get(sourName);
         if (sessionHolder == null){
            DataSource dataSource = DataSourceManager.getDataSource(sourName);

            if (dataSource == null){
               throw new DBAccessException(sourName + " dataSource is null");
            }
            sessionHolder = new SessionHolder(sourName,dataSource.getConnection());

         }
         Orm orm = new Ormer(sessionHolder);

         sessionHolder.orm = orm;
         sessionHolderMap.put(sourName,sessionHolder);
         SESSION_HOLDER.set(sessionHolderMap);

         return orm;
    }

    public static void release(){
        Map<String,SessionHolder> sessionHolderMap = SESSION_HOLDER.get();
        try {
            if(sessionHolderMap!=null){
                for(String key:sessionHolderMap.keySet()){
                    SessionHolder sessionHolder = sessionHolderMap.get(key);
                    sessionHolder.orm.release();
                }
                sessionHolderMap.clear();
                SESSION_HOLDER.remove();
            }
        } catch (Exception e) {
           e.printStackTrace();
        }
    }
    public static class SessionHolder {
        Orm orm;
        String sourceName;
        Connection connection;

        public SessionHolder(String sourceName,Connection connection){
            this.sourceName = sourceName;
            this.connection = connection;
        }
        public Connection getConnection(){
            return this.connection;
        }
    }
}
