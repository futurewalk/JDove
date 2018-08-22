package cn.com.dove.framework.orm;

import cn.com.dove.anotation.Column;
import cn.com.dove.anotation.Table;
import cn.com.dove.framework.em.PKST;
import cn.com.dove.framework.orm.Api.Orm;
import cn.com.dove.utils.BaseType;
import cn.com.dove.utils.ReflectUtils;
import cn.com.dove.utils.StringTool;
import cn.gfurox.common.CommonUtils;
import cn.gfurox.common.MessageException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.*;

public class SchemeExport {
   private static final Logger logger = LoggerFactory.getLogger(SchemeExport.class);

   private static Map<String, Object> DB_CONFIG = new HashMap();
   private static List<String> CLASSPATHS = new ArrayList<>();
   private static Map<String, String> DBIGNORES = new HashMap();
   private static Map<String, String> DBAPPOINTS = new HashMap();
   private static Map<String,String>  EXISTING_TABLES = new HashMap();

   private static Map<String, List<String>> TB_MAP = new HashMap<>();
   private static final String TAB_KEYWORD = "\t";
   private static final String LINE_FEED = "\n";
   private static final String DBFIELD_SUFFX = "_DBFIELD";
   private static final String FIEDTYPE_SUFFX = "_FIEDTYPE";


   public static Map<String,Object> getDbConfig() throws IOException {
      if(DB_CONFIG == null || DB_CONFIG.size() == 0){
         return loadDbConfig();
      }
      return DB_CONFIG;
   }
   private static Map<String,Object> loadDbConfig() throws IOException {

      Properties properties = new Properties();
      properties.load(new InputStreamReader(CommonUtils.getInputStreamFromClassPath("dbConf.properties"),CommonUtils.UTF8));

      DB_CONFIG.put("dbSt",properties.get("dbSt"));
      DB_CONFIG.put("dbCp",properties.get("dbCp"));
      DB_CONFIG.put("dbEngine",properties.get("dbEngine"));
      DB_CONFIG.put("dbCover",properties.get("dbCover")==null?false:properties.get("dbCover"));
      DB_CONFIG.put("dbIgnore",properties.get("dbIgnore"));
      DB_CONFIG.put("dbAppoint",properties.get("dbAppoint"));
      DB_CONFIG.put("dbPackage",properties.get("dbPackage"));
      DB_CONFIG.put("dbCreateOpen",properties.get("dbCreateOpen"));

      return DB_CONFIG;

   }

   public static void loadDbCreateConfig(){
      try{

         queryExistingTables();

         loadDbConfig();

         SchemeExport.loadDbConfig(DB_CONFIG.get("dbIgnore"),DB_CONFIG.get("dbAppoint"));
         String packPath = String.valueOf(DB_CONFIG.get("dbPackage"));

         if(!StringTool.isNotNull(packPath)){
            throw new SQLException(" have not assign entity package");
         }

         boolean isOpen = Boolean.valueOf(String.valueOf(DB_CONFIG.get("dbCreateOpen")));

         //logger.info("opened the dbCreate tool,it will recreate the table {},{}",isOpen,String.valueOf(DB_CONFIG.get("dbCreateOpen")));
         if(!isOpen){
            return;
         }

         SchemeExport.scanDomainByPackage(packPath);


      }catch (Exception e){
         e.printStackTrace();
      }
   }
   /**
    * 加载配置文件，
    * @param ignoreValues 忽略扫描的配置
    * @param appointValues 只扫描特定实体类的配置
    */
   public static void loadDbConfig(Object ignoreValues, Object appointValues) {
      if (!StringTool.isNotNull(String.valueOf(ignoreValues))) {
         return;
      }
      String[] ignoreArr = String.valueOf(ignoreValues).split(",");
      for (String value : ignoreArr) {
//         LOGGER.info("get the ignore values:{}", value);
         DBIGNORES.put(value, value);
      }
      if (!StringTool.isNotNull(String.valueOf(appointValues))) {
         return;
      }
      String[] appointArr = String.valueOf(appointValues).split(",");
      for (String value : appointArr) {
        // LOGGER.info("get the appoint values:{}", value);
         DBAPPOINTS.put(value, value);
      }

   }

   /**
    * 扫描配置包下的所有的类
    * @param basePack
    * @throws ClassNotFoundException
    */
   private static void scanDomainByPackage(String basePack) throws Exception {

      System.out.print(LINE_FEED);
      //System.out.println("****************** start Create table by automation tool *****************");

      String classpath = SchemeExport.class.getResource("/").getPath();

      basePack = basePack.replace(".", File.separator);

      String searchPath = classpath + basePack;

//      logger.info("get the load package {},{}",searchPath,classpath);
      doPath(new File(searchPath));

      for (String s : CLASSPATHS) {
         s = s.replace(classpath.replace("/", "\\").replaceFirst("\\\\", ""), "").replace("\\", ".").replace(".class", "");
         Class cls = Class.forName(s);

         //if  dbAppoint has config ,then only scan the dbAppoint config values
         if (StringTool.isNotNull(DBAPPOINTS.get(cls.getSimpleName()))) {
            SchemeExport.createTable(getDBTableName(cls),createTableSQl(cls));
            continue;
         }

         //if  dbAppoint has not config , enter dbIgnore config scan and ignore config values
         if (!cls.getSimpleName().equals(DBIGNORES.get(cls.getSimpleName()))) {
            SchemeExport.createTable(getDBTableName(cls),createTableSQl(cls));
         }
      }
   }

   /**
    * 递归出实体类的路径
    * @param file
    */
   private static void doPath(File file) {

      if (file.isDirectory()) {
         File[] files = file.listFiles();
         for (File f : files) {
            doPath(f);
         }
      } else {
         if (file.getName().endsWith(".class")) {
            CLASSPATHS.add(file.getPath());
         }
      }
   }

   /**
    * 生成建表语句的入口
    * @param tClass
    * @param <T>
    */
   public static <T> String  createTableSQl(Class<T> tClass) throws SQLException {
      try {
         synchronized (tClass){

            StringBuilder sb = new StringBuilder();

            dbColumn(tClass);//load the domain class to the map

            List<String> fieldList = TB_MAP.get(tClass.getSimpleName() + FIEDTYPE_SUFFX);
            List<String> dbFieldList = TB_MAP.get(tClass.getSimpleName() + DBFIELD_SUFFX);

            if (fieldList == null || fieldList.size() == 0) {
               throw new SQLException("not fount any entity setting");
            }
            sb.append(LINE_FEED).append(" CREATE TABLE ").append(getDBTableName(tClass)).append(" ( ").append(LINE_FEED);

            for (int i = 0; i < fieldList.size(); i++) {
               sb.append(TAB_KEYWORD).append(dbFieldList.get(i)).append(" ").append(fieldList.get(i));
               if (i != fieldList.size() - 1) {
                  sb.append(",");
               }
               sb.append(LINE_FEED);
            }

            sb.append(" )").append(LINE_FEED);
            String dbEngine = String.valueOf(DB_CONFIG.get("dbEngine"));
            if(StringTool.isNotNull(dbEngine)){
               sb.append(" ENGINE = ").append(dbEngine).append(";");
            }else {
               sb.append(" ENGINE = InnoDB ;");
            }
            sb.append(LINE_FEED);

            return sb.toString();

         }
      } catch (Exception e) {
         e.printStackTrace();
      }
      throw new SQLException("not fount any entity setting");
   }

   /**
    * 获取实体类对应的表名
    *
    * @param cls
    * @param <T>
    * @return
    */
   public static <T> String getDBTableName(Class<T> cls) throws Exception {

      if (cls == null) {
         throw MessageException.newMessageException("class must be not null");
      }
      Table confTable = cls.getAnnotation(Table.class);

      return confTable == null ? filedLowerCase(cls.getSimpleName(),null) : confTable.tableName();
   }

   /**
    * 获取实体类每列的注解名，长度，和对应数据库类型，如果不设置长度，数据库类型，则取对应工具的相对应类型
    *
    * @param klass
    * @param <T>
    * @return
    */
   public static <T> void dbColumn(Class<T> klass) throws Exception {

      Field[] fields = klass.getDeclaredFields();

      List<String> fieldList = new ArrayList<>(fields.length);
      List<String> dbTypeList = new ArrayList<>(fields.length);


      for (Field field : fields) {
         String fieldName = null;
         String dataColumn = null;

         Column meta = field.getAnnotation(Column.class);

         if (meta != null) {

            DbHelp dbHelp = new DbHelp(
                 upperCase(field.getType().getSimpleName()),
                 upperCase(meta.type()),
                 CommonUtils.parseInt(meta.length(), 0),
                 meta.pkst().name(),meta.pK(),
                 meta.notNull()
            );

            if (StringTool.isNotNull(meta.type())) {
               dataColumn = getDataColumnByFieldType(dbHelp);
            } else {
               dataColumn = getDataColumnByFieldType(dbHelp);
            }

            if (StringTool.isNotNull(meta.colName())) {
               fieldName = meta.colName();
            } else {
               fieldName = fieldChar(meta.colName(), field.getName());
            }
         } else {
            // if dbSt equals ignore then create all tableField by domain field
            if (!"ignore".equals(String.valueOf(DB_CONFIG.get("dbSt")))) {
               continue;
            }
            fieldName = fieldChar(field.getName(), null);
            dataColumn = getDataColumnByFieldType(new DbHelp(upperCase(field.getType().getSimpleName()),null,0,null,false,false));

         }

         if (StringTool.isNotNull(fieldName)) {
            fieldList.add(fieldName);
         }
         if (StringTool.isNotNull(dataColumn)) {
            dbTypeList.add(dataColumn);
         }
      }
      //LOGGER.info("==================>,{},{}",fieldList.size(),dbTypeList.size());
      TB_MAP.put(klass.getSimpleName() + DBFIELD_SUFFX, fieldList);
      TB_MAP.put(klass.getSimpleName() + FIEDTYPE_SUFFX, dbTypeList);
   }

   /**
    * 转换大写
    *
    * @param word
    * @return
    * @throws Exception
    */
   private static String upperCase(String word) throws Exception {
      if (!StringTool.isNotNull(word)) {
         return null;
      }
      char[] cword = word.toCharArray();

      StringBuilder sb = new StringBuilder();

      for (Character wd : cword) {
         sb.append(Character.toUpperCase(wd));
      }
      return sb.toString();
   }

   /**
    * 针对大写字母加下划线，并转换大写字母为小写字母
    *
    * @param field
    * @return
    */
   public static String fieldChar(String field, String defaultField) throws Exception {
      return !"lowerCase".equals(String.valueOf(DB_CONFIG.get("dbCp")))? filedUpperCase(field, defaultField): filedLowerCase(field, defaultField);
   }

   private static String filedUpperCase(String field, String defaultField) throws Exception {

      char[] c = null;
      StringBuilder sb = new StringBuilder();

      if (!StringTool.isNotNull(field) && !StringTool.isNotNull(defaultField)) {
         throw MessageException.newMessageException("filed must not blank ........");
      }

      if (StringTool.isNotNull(field)) {
         c = field.toCharArray();
      } else {
         c = defaultField.toCharArray();
      }

      for (int i = 0; i < c.length; i++) {

       /*  if (Character.isUpperCase(c[i])) {
            sb.append("_").append(Character.toUpperCase(c[i]));
         } else {
         }*/
         sb.append(Character.toUpperCase(c[i]));
      }
      return sb.toString();
   }

   private static String filedLowerCase(String field, String defaultField) throws Exception {
      char[] c = null;
      StringBuffer sb = new StringBuffer();


      if (!StringTool.isNotNull(field) && !StringTool.isNotNull(defaultField)) {
         throw MessageException.newMessageException("field must not blank ........");
      }
      if (StringTool.isNotNull(field)) {
         c = field.toCharArray();
      } else {
         c = defaultField.toCharArray();
      }
      for (int i = 0; i < c.length; i++) {

         /*if (Character.isUpperCase(c[i])) {
            sb.append("_").append(Character.toLowerCase(c[i]));
         } else {
         }*/
         sb.append(c[i]);
      }
      return sb.toString();
   }

   public static  String getDataColumnByFieldType(DbHelp dbHelp) throws Exception{

      String fieldType = dbHelp.getFieldType();
      int length = dbHelp.getLength();

      if (BaseType.INT_STR.equals(fieldType)||BaseType.INTEGER_STR.equals(fieldType)) {

         //LOGGER.info("get the ready field: {}",fieldType);
         dbHelp.setDefaultType("INT");
         dbHelp.setLength(length > 0?length:0);

      } else if (BaseType.STRING_STR.equals(fieldType)) {

         dbHelp.setDefaultType("VARCHAR");
         dbHelp.setLength(length > 0?length:255);

      } else if (BaseType.BIGDECIMAL_STR.equals(fieldType)) {

         dbHelp.setDefaultType("DECIMAL");
         dbHelp.setLength(0);

      } else if (BaseType.FLOAT_STR.equals(fieldType)) {

         dbHelp.setDefaultType("DOUBLE");
         dbHelp.setLength(0);

      } else if (BaseType.DOUBLE_STR.equals(fieldType)) {

         dbHelp.setDefaultType("DOUBLE");
         dbHelp.setLength(0);

      }else if (BaseType.TIMESTAMP_STR.equals(fieldType)) {

         dbHelp.setDefaultType("TIMESTAMP");
         dbHelp.setLength(0);

      }else if (BaseType.LONG_STR.equals(fieldType)) {
         if(BaseType.INT_STR.equals(dbHelp.getMetaType())){
            dbHelp.setDefaultType("INT");
            dbHelp.setLength(0);
         }else {
            dbHelp.setDefaultType("BIGINT");
            dbHelp.setLength(length > 0?length:20);
         }
      }else if(BaseType.BOOLEAN_STR.equals(fieldType)){

         dbHelp.setDefaultType("TINYINT");
         dbHelp.setLength(0);

      }else {
         return "";
      }
      return SQlBuilder(dbHelp);
   }

  /* private static String SQlBuilder(String type, int length, String defaultType,String pkst,boolean pk) {*/
   private static String SQlBuilder(DbHelp dbHelp) {
      StringBuilder sb = new  StringBuilder();

      int length = dbHelp.getLength();
      String type = dbHelp.getMetaType();
      String pkst = dbHelp.getPrimaryKey();
      boolean pk = dbHelp.isPk();
      boolean notNull = dbHelp.isNotNull();

      if (!StringTool.isNotNull(type)) {
         type = dbHelp.getDefaultType();
      }
      if(length > 0){

         if("TEXT".equals(type)){
            sb.append(type);
         }else {
            sb.append(type).append("(").append(length).append(") ");
         }

         if(StringTool.isNotNull(PKST.parse(pkst))){
            sb.append(PKST.parse(pkst));
         }
         if(pk){
            sb.append(" PRIMARY KEY UNIQUE");
         }
         if(notNull){
            sb.append(" NOT NULL");
         }
         return sb.toString();
      }

      sb.append(type).append(" ");
      if(StringTool.isNotNull(pkst)){
         sb.append(PKST.parse(pkst));
      }
      if(notNull){

         sb.append(" NOT NULL ");

      }
      return sb.toString();
   }
   /**
    * 获取存在数据库中的表
    * @throws Exception
    */
   private static void queryExistingTables() throws Exception {
      Orm orm = SessionFactory.newOrm();
      String sql = "show TABLES ";
      List<Object[]> tableList = orm.valuesList(sql);
      for (Object[] table: tableList) {
         EXISTING_TABLES.put(upperCase(String.valueOf(table[0])),upperCase(String.valueOf(table[0])));
      }
   }

  private static void createTable(String tableName,String tableSql) throws Exception {
      boolean dbCover = Boolean.valueOf(String.valueOf(DB_CONFIG.get("dbCover")));
      Orm orm = SessionFactory.newOrm();

      orm.beginTransaction();
      try{
         if(dbCover){
            if(StringTool.isNotNull(EXISTING_TABLES.get(upperCase(tableName)))){
               StringBuilder tableStr = new StringBuilder(" DROP TABLE ");
               tableStr.append(tableName);

               orm.execute(tableStr.toString(),"CREATE");
            }
            orm.execute(tableSql,"CREATE");
            return;
         }
         if(StringTool.isNotNull(EXISTING_TABLES.get(upperCase(tableName)))){
            return;
         }
         orm.execute(tableSql,"CREATE");
      }catch (Exception e){
          e.printStackTrace();
          orm.rollBack();

      }finally {
         SessionFactory.release();
      }
  }
  public static void export(Class... clazz){
      try{
         File file = new File(Thread.currentThread().getContextClassLoader().getResource("orm.xml").getPath());
         DataSourceManager.start(file);
         loadDbConfig();
         for(int i = 0;i < clazz.length;i++){
            SchemeExport.createTable(getDBTableName(clazz[i]),createTableSQl(clazz[i]));
         }

      }catch (Exception e){
          e.printStackTrace();

      }
  }
  public static void  export(){
      try{
         File file = new File(Thread.currentThread().getContextClassLoader().getResource("orm.xml").getPath());
         DataSourceManager.start(file);
         SchemeExport.loadDbCreateConfig();

         SchemeExport.createTable(getDBTableName(Scheme.class),createTableSQl(Scheme.class));

         Scheme scheme = ReflectUtils.map2Bean(DB_CONFIG,Scheme.class);
         Orm orm = SessionFactory.newOrm();
         orm.save(scheme);

         SessionFactory.release();
      }catch(Exception e){
          e.printStackTrace();
      }
  }
  public static void main(String args[]){
      export();
   }
}