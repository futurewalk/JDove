package cn.com.dove.framework.orm;

/**
 * Created by wangdequan on 2018/1/11.
 */
public class DbHelp {

   private String fieldType;
   private String metaType;
   private int length;
   private String primaryKey;
   private boolean isPk;
   private boolean notNull;

   private String defaultType;

   public DbHelp(String fieldType,String metaType,int length,String primaryKey,boolean isPk,boolean notNull){
      this.fieldType = fieldType;
      this.metaType = metaType;
      this.length = length;
      this.primaryKey = primaryKey;
      this.isPk = isPk;
      this.notNull = notNull;
   }

   public String getFieldType() {
      return fieldType;
   }

   public void setFieldType(String fieldType) {
      this.fieldType = fieldType;
   }

   public String getMetaType() {
      return metaType;
   }

   public int getLength() {
      return length;
   }

   public void setLength(int length) {
      this.length = length;
   }

   public String getPrimaryKey() {
      return primaryKey;
   }

   public void setPrimaryKey(String primaryKey) {
      this.primaryKey = primaryKey;
   }

   public boolean isPk() {
      return isPk;
   }

   public void setPk(boolean pk) {
      isPk = pk;
   }

   public void setMetaType(String metaType) {
      this.metaType = metaType;
   }

   public boolean isNotNull() {
      return notNull;
   }

   public void setNotNull(boolean notNull) {
      this.notNull = notNull;
   }

   public String getDefaultType() {
      return defaultType;
   }

   public void setDefaultType(String defaultType) {
      this.defaultType = defaultType;
   }

   @Override
   public String toString() {
      return "DbHelp{" +
              "fieldType=" + fieldType +
              ", metaType='" + metaType + '\'' +
              ", length=" + length +
              ", primaryKey='" + primaryKey + '\'' +
              ", isPk=" + isPk +
              ", notNull=" + notNull +
              '}';
   }
}
