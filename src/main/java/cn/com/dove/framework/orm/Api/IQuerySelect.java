package cn.com.dove.framework.orm.Api;

import cn.com.dove.utils.PageObject;
import org.apache.poi.ss.formula.functions.T;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * Created by wangdequan on 2018/3/13.
 */
public interface IQuerySelect extends BaseormApi {



   /**
    * query return resultSet
    * @param sql
    * @param values
    * @return
    */
    ResultSet resultSet(String sql, Object... values) throws SQLException;

    /**
     * query a value
     * @param sql
     * @param values
     * @return
     */
    Object value(String sql,Object...values) throws SQLException;
   /**
    * query by sql and return entity
    * @param sql
    * @param values
    * @param klass
    * @param <T>
    * @return
    */
    <T> T rowToEntity(String sql, Class<T> klass, Object... values) throws Exception;

   /**
    * query by sql return Object
    * @param sql
    * @param values
    * @return
    */
    Object[] row(String sql, Object... values) throws SQLException;

   /**
    *
    * @param sql
    * @param values
    * @param <T>
    * @return
    */
    <T> List<T> valuesObject(String sql, Class<T> klass, Object... values) throws Exception;

   /**
    * query by sql return array
    * @param sql
    * @param values
    * @return
    */
    List<Object[]> valuesList(String sql, Object... values);

   /**
    * query by sql return map
    * @param sql
    * @param values
    * @return
    */
    List<Map<String,Object>> rowsToMap(String sql, Object... values);

   /**
    * query map
    * @param sql
    * @param values
    * @return
    */
    Map<String,Object> map(String sql, Object... values) throws SQLException;

    /**
     * query list object array
     * @param sql
     * @param po
     * @param values
     * @return
     */
    List<Object[]> queryPageList(String sql, PageObject po,Object... values) throws SQLException;
    /**
     * query page result
     * @param sql
     * @param po
     * @param klass
     * @param values
     * @param <T>
     * @return
     */
    <T> List<T> pageListObject(String sql, PageObject po,Class<T> klass, Object... values) throws Exception;

   /**
    * query page map
    * @param sql
    * @param po
    * @param values
    * @return
    */
    List<Map<String,Object>> getPageMap(String sql, PageObject po, Object... values) throws SQLException;

}
