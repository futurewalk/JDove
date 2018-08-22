package cn.com.dove.framework.orm.Api;

import java.sql.SQLException;

/**
 * Created by wangdequan on 2018/3/13.
 */
public interface IUpdateOrm extends BaseormApi {

   /**
    * save by sql
    * @param sql
    * @param values
    * @return
    */
   long save(String sql, Object... values) throws SQLException;

   /**
    * save by Class
    * @param object
    * @return
    */
   long save(Object object) throws Exception;

   /**
    * saveorUpdate Class
    * @param object
    * @return
    */
//   long saveOrUpdate(Object object);

   /**
    * delete by sql
    * @param sql
    * @param values
    * @return
    */
   long delete(String sql, Object... values) throws SQLException;

   /**
    * delete by object
    * @param object
    * @return
    */
   long delete(Object object) throws SQLException;

   /**
    * update by sql
    * @param sql
    * @param values
    * @return
    */
   long update(String sql, Object... values) throws SQLException;


   /**
    * update by Class
    * @param object
    * @return
    */
   long update(Object object) throws Exception;


}
