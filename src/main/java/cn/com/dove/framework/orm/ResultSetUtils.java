package cn.com.dove.framework.orm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResultSetUtils {
   private static final Logger logger = LoggerFactory.getLogger(ResultSetUtils.class);

   public static List<Map<String,Object>> handlerList(ResultSet rs,List<Map<String,Object>> listMap) throws SQLException {

      ResultSetMetaData meta = rs.getMetaData();
      int columnCount = meta.getColumnCount();
      while (rs.next()){
         listMap.add(dealMap(rs,columnCount,meta));
      }
      return listMap;
   }
   public static Map<String,Object> map(ResultSet rs) throws SQLException {

      ResultSetMetaData meta = rs.getMetaData();
      int columnCount = meta.getColumnCount();
      if(rs.next()) {
         return dealMap(rs,columnCount,meta);
      }
      return new HashMap<>();
   }
   private static Map<String,Object> dealMap(ResultSet rs,int columnCount,ResultSetMetaData meta) throws SQLException {
      Map<String,Object> map = new HashMap();
      for (int i = 1; i <= columnCount; i++) {
         Object value = rs.getObject(meta.getColumnLabel(i));
         map.put(meta.getColumnLabel(i), value);
      }
      return map;
   }
}