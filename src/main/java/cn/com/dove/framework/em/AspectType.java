package cn.com.dove.framework.em;


import cn.com.dove.utils.StringTool;

/**
 * Created by wangdequan on 2018/1/11.
 */
public enum AspectType {

   BEFORE("before"),AFTER("after"),AROUND("around"),THROWAFTER("throewAfter");



   protected final String chineseName;

   private AspectType(String chineseName){
      this.chineseName = chineseName;
   }

   /**
    * 获取中文名称.
    * @return {@link String}
    */
   public String getChineseName() {
      return chineseName;
   }

   /**
    * 解析字符串.
    * @return {@link PKST}
    */
   public static final String parse(String value) {
      if(StringTool.isEmpty(value)){
         return null;
      }
      try{
         return PKST.valueOf(value).chineseName;
      }catch(Throwable t){
         return null;
      }
   }
}
