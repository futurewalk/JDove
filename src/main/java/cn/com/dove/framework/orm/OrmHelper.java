package cn.com.dove.framework.orm;

import cn.com.dove.anotation.Column;
import cn.com.dove.framework.em.CRUD;
import cn.com.dove.framework.em.PKST;
import cn.com.dove.framework.orm.Api.Orm;
import cn.com.dove.utils.BaseType;
import cn.com.dove.utils.DateTool;
import cn.com.dove.utils.ReflectUtils;
import cn.com.dove.utils.StringTool;
import org.apache.commons.collections.map.HashedMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by wangdequan on 2018/3/14.
 */
public class OrmHelper{

   private static final Logger logger = LoggerFactory.getLogger(OrmHelper.class);
   private static Map<String,Object> scheme = null;

   public static Map<String, Object> getScheme() {
      return scheme;
   }

   public static void setScheme(Map<String, Object> scheme) {
      OrmHelper.scheme = scheme;
   }

   public static void queryScheme() throws SQLException {
      Orm orm = SessionFactory.newOrm();
      Map<String,Object> schemeMap = orm.map("SELECT * FROM scheme LIMIT 1");

      for(String key:schemeMap.keySet()){
         logger.info("load dbCreate config from dataBase:{},{}",key,schemeMap.get(key));
      }

      OrmHelper.setScheme(schemeMap);

      SessionFactory.release();
   }
   /**
    * prepareParameters for sql placeholder
    * @param pstmt
    * @param parameters
    * @throws SQLException
    */
   public static void prepareParameters(PreparedStatement pstmt,Object... parameters) throws SQLException {

      for(int i = 0; i < parameters.length; ++i) {
         Object parameter = parameters[i];
         if(parameter == null) {
            pstmt.setNull(i+1, 0);
         } else if(parameter.getClass() == Date.class) {
            pstmt.setTimestamp(i+1, new Timestamp(((Date)parameter).getTime()));
         } else {
            pstmt.setObject(i+1, parameter);
         }
      }
   }

   /**
    * close prepareStatement and ResultSet
    * @param statement
    * @param rs
    * @throws SQLException
    */
   public static void close(PreparedStatement statement, ResultSet rs) throws SQLException {
      if (rs !=null ){
         rs.close();
      }
      if (statement != null){
         statement.close();
      }
   }

   /**
    * get class field and dataBaseColumn,
    * @param klass
    * @param <T>
    * @return
    * @throws Exception
    */
   public static <T> Map<String,Field> classFiledMap(Class<T> klass) throws Exception {

      Field[] fields = klass.getDeclaredFields();
      Map<String,Field> fieldMap = new HashedMap();

      for (Field field: fields) {
         String fieldName = null;

         Column meta = field.getAnnotation(Column.class);

         if (meta != null) {
            if (StringTool.isNotNull(meta.colName())) {
               fieldName = meta.colName();
            } else {
               fieldName = SchemeExport.fieldChar(meta.colName(), field.getName());
            }

         } else {
            if (!"ignore".equals(SchemeExport.getDbConfig().get("db_st"))) {
               continue;
            }
            fieldName = SchemeExport.fieldChar(field.getName(), null);
         }

         if (StringTool.isNotNull(fieldName)) {
            fieldMap.put(fieldName,field);
         }
      }
      return fieldMap;
   }

   /**
    * set field value of a class
    * @param object
    * @param value
    * @param fieldName
    * @param <T>
    * @return
    * @throws Exception
    */
   public  static <T> T setValue(Object object,Object value ,String fieldName) throws NoSuchFieldException, IllegalAccessException {
      Field field = object.getClass().getDeclaredField(fieldName);
      field.setAccessible(true);
      if (BaseType.BOOLEAN_STR.equals(StringTool.upperCase(field.getType().getSimpleName()))){
         field.set(object, Boolean.valueOf(String.valueOf(value)));
      }else {
         field.set(object,value);
      }
      return (T) object;
   }

   /**
    * get the tableName
    * @param klass
    * @param <T>
    * @return
    */
   public static <T> String tableName(Class<T> klass){
      try {
         return SchemeExport.getDBTableName(klass);
      } catch (Exception e) {
         e.printStackTrace();
      }
      return null;
   }
   public static boolean contains(String sql,String[] conditions){
      for(int i = 0;i < conditions.length;i++){
         if(sql.indexOf(conditions[i])>-1){
            return true;
         }
      }
      return false;
   }
   /**
    * prepare create save SQL
    * @param object
    * @param parList
    * @return
    * @throws Exception
    */
   public static String createSaveSQL(Object object,List parList,CRUD crud) throws SQLException {
      try{
         OrmPkField ormField = ormpkField(object);

         if(ormField == null){
            throw  new SQLException("not set annotation on primary key in "+object.getClass());
         }
         if(!PKST.UUID.equals(ormField.getPkst())){//如果不是自定义的uuid，则设置不设置为空
            Object value = getBlankValue(ormField.pkType);
            setValue(object, value,ormField.getClassField());
         }
         return realSQl(object,parList,ormField,crud);
      }catch(Exception e){
		 throw new SQLException(e.toString());
      }
   }
   /**
    * prepare create update SQL
    * @param object
    * @param parList
    * @return
    * @throws Exception
    */
   public static  String createUpdateQL(Object object,List parList) throws Exception {
      OrmPkField ormField = ormpkField(object);
      if(ormField == null){
         throw  new SQLException("not set annotation on primary key in "+object.getClass());
      }
      return realSQl(object,parList,ormField,CRUD.UPDATE);
   }

   /**
    * createDelete sql by entity
    * @param object
    * @param parList
    * @return
    */
   public static String createDeleteSQL(Object object,List parList) throws Exception {

      OrmPkField ormpkField = ormpkField(object);
      Object primaryValue = getFiledValue(object,ormpkField.getClassField());

      StringBuilder sb = new StringBuilder("DELETE FROM ");
      sb.append(tableName(object.getClass()));

      sb.append(" WHERE ").append(ormpkField.columnField).append(" = ?");
      parList.add(primaryValue);

      return sb.toString();
   }

   /**
    * get a blank value by field type
    * @param fieldType
    * @return
    */
   public static Object getBlankValue(String fieldType){

      if (BaseType.INT_STR.equals(fieldType) || BaseType.INTEGER_STR.equals(fieldType)) {
      } else if (BaseType.STRING_STR.equals(fieldType)) {
         return null;
      } else if (BaseType.BIGDECIMAL_STR.equals(fieldType)) {
      } else if (BaseType.FLOAT_STR.equals(fieldType)) {
         return 0;
      } else if (BaseType.DOUBLE_STR.equals(fieldType)) {
         return 0;
      } else if (BaseType.LONG_STR.equals(fieldType)) {
         return 0L;
      }
      throw new IllegalAccessError("primary key is illegal .. ");
   }
   /**
    * jude primarykey is blank
    * @param fieldType
    * @param value
    * @return
    */
   public static boolean judePkBlank(String fieldType, String value){
      if (BaseType.INT_STR.equals(fieldType) || BaseType.INTEGER_STR.equals(fieldType)) {
         return Integer.valueOf(value)>0?true:false;
      } else if (BaseType.STRING_STR.equals(fieldType)) {
         return StringTool.isNotNull(value)?true:false;
      } else if (BaseType.BIGDECIMAL_STR.equals(fieldType)) {
      } else if (BaseType.FLOAT_STR.equals(fieldType)) {
         return Float.valueOf(value)>0?true:false;
      } else if (BaseType.DOUBLE_STR.equals(fieldType)) {
         return Double.valueOf(value)>0?true:false;
      } else if (BaseType.LONG_STR.equals(fieldType)) {
         return Long.valueOf(value)>0?true:false;
      }
      throw new IllegalAccessError("primary key is illegal .. ");
   }

   /**
    * start create realSQl
    * @param object
    * @param parList
    * @param ormpkField
    * @return
    * @throws Exception
    */
   public static String realSQl(Object object ,List parList,OrmPkField ormpkField,CRUD crud) throws Exception {
      Map<String,Field> filedMap = OrmHelper.classFiledMap(object.getClass());
      StringBuilder sb = null;

      Object primaryValue = getFiledValue(object,ormpkField.getClassField());

      if (CRUD.UPDATE.equals(crud)){
         sb = new StringBuilder(" UPDATE ");
      }else {
         sb = new StringBuilder("INSERT INTO ");
      }

      sb.append(tableName(object.getClass())).append(" SET ");

      Iterator it = filedMap.keySet().iterator();

      for(int index = 0;it.hasNext();index++){
          String key = (String) it.next();
          Field field = filedMap.get(key);
          if(!CRUD.UPDATE.equals(crud) && ormpkField.getClassField().equals(field.getName())) {
              Column column = field.getAnnotation(Column.class);
              if(column == null || !PKST.UUID.equals(column.pkst())) {
                  continue;
              }
          }
          if(!CRUD.UPDATE.equals(crud) || !ormpkField.getClassField().equals(field.getName())) {
              if(index < filedMap.size()) {
                  sb.append(key).append(" = ? ,");
              } else {
                  sb.append(key).append(" = ? ");
              }

              parList.add(getFiledValue(object, field.getName()));
          }
      }
      String sql = sb.toString();
      if(sql.endsWith(",")){
         sql = sql.substring(0,sql.lastIndexOf(","));
      }

//      logger.info("####:{},{}",sql,ormpkField.pkst.name());

    //  logger.info("查看操作方式，{}，{},{}",new Object[]{CRUD.UPDATE,crud,sb.toString()});
      if (CRUD.UPDATE.name().equals(crud.name())){
         sql += " WHERE " + ormpkField.getColumnField() + " = ?";
         parList.add(primaryValue);
      }
      return sql;
   }

   public static OrmPkField ormpkField(Object object) throws Exception {
       Field[] fields = object.getClass().getDeclaredFields();
       for (int i = 0;i < fields.length;i++){
            Field field = fields[i];
            Column column = field.getAnnotation(Column.class);

            if (column == null)continue;

            PKST pkst = column.pkst();
            String pkType = StringTool.upperCase(field.getType().getSimpleName());

            if (StringTool.isNotNull(column.colName())){
                String colName = SchemeExport.fieldChar(column.colName(),null);
                return new OrmPkField(colName,field.getName(),pkType,pkst);
            }

            return new OrmPkField(field.getName(),field.getName(),pkType,pkst);
       }
       return null;
   }

   public static Object getFiledValue(Object object,String field) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, NoSuchFieldException {
      Method method = object.getClass().getMethod(ReflectUtils.createGetMethod(object,field));
      return method.invoke(object);
   }
   public static void writeLog(long start,String title,String sql ,boolean ok,Object...values){
      StringBuilder sb = new StringBuilder();
      sb.append(DateTool.getString(new Date(),"yyyy-MM-dd HH:mm:ss,SS"));
      sb.append(":[ORM-INFO]").append(" -[").append(title).append(ok?" / OK ":" / failed ")
              .append(System.currentTimeMillis()-start).append("/ms Done ]");
      sb.append(" -[ ").append(sql).append("]");

      if (values.length > 0){
         sb.append(" -[");
      }

      for(int i = 0 ;i < values.length;i++){
         if (i==values.length-1){
            sb.append(values[i]);
         }else {
            sb.append(values[i]).append(",");
         }
      }
      if (values.length > 0){
         sb.append(" ]");
      }

      System.out.println(sb.toString());
   }
   //[ORM]2018/03/20 21:27:18  -[Queries/default] - [  OK /    db.Query /    15.1ms] - [select Id, taskid, userid, taskname, tasknotes, taskreward, redflowercount, totalredflowercount, taskcreatetime, taskfinishtime from red_flower_task_info where userid = ? and  Redflowercount < Totalredflowercount and taskFinishTime < operationTime] - `we64960d0dacc22`

   private static class OrmPkField{
      String columnField;
      String classField;
      String pkType;
      PKST pkst;

      public OrmPkField(String columnField,String classField,String pkType,PKST pkst){
         this.columnField = columnField;
         this.classField = classField;
         this.pkType = pkType;
         this.pkst = pkst;
      }

      public String getColumnField() {
         return columnField;
      }

      public void setColumnField(String columnField) {
         this.columnField = columnField;
      }

      public String getClassField() {
         return classField;
      }

      public void setClassField(String classField) {
         this.classField = classField;
      }

      public String getPkType() {
         return pkType;
      }

      public void setPkType(String pkType) {
         this.pkType = pkType;
      }

      public PKST getPkst() {
         return pkst;
      }

      public void setPkst(PKST pkst) {
         this.pkst = pkst;
      }
   }
   public static void main(String argsp[]) throws Exception {
      File file = new File(Thread.currentThread().getContextClassLoader().getResource("orm.xml").getPath());
      DataSourceManager.start(file);

       Orm orm = SessionFactory.newOrm();
       String sql = "select * from goods where id = '2018-03-26-23-29-4279c7nt97jmt9auv641dc6bbyz7j332hpj75f'";
       Map<String,Object> map = orm.map(sql);

       Iterator it = map.keySet().iterator();
       while (it.hasNext()){
          String key = (String) it.next();
          logger.info("value:{}",map.get(key));
       }
       //queryScheme();


   /*   Goods goods = new Goods();
      goods.setAddTime(DateTool.getString(new Date()));
      goods.setBrandId(2);
      goods.setCategoryId(3);
      goods.setDescription("商品描述的测试");
      goods.setEnterStoreId(45);
      goods.setGoodsFrom("英国");
      goods.setGoodsStatus("上架");
      goods.setName("测试事务的大萨达所");
      goods.setNumber(12121);
      goods.setOriginalPrice(new BigDecimal(45));
      goods.setProducer("嘟嘟啦");
      goods.setRemark("我的衣服我做主");
      goods.setPrice(new BigDecimal(789));
      goods.setUpdateTime(DateTool.getString(new Date()));
      goods.setId(StringTool.uuId());

      Orm orm = SessionFactory.newOrm();
      orm.save(goods);*/
   }
}
