package cn.com.dove.framework.em;

/**
 * Created by wangdequan on 2018/2/5.
 */
public enum ReqType {
   POST("POST"),GET("GET");

   protected final String chineseName;

   private ReqType(String chineseName){
      this.chineseName = chineseName;
   }

   /**
    * 获取中文名称.
    * @return {@link String}
    */
   public String getChineseName() {
      return chineseName;
   }

}
