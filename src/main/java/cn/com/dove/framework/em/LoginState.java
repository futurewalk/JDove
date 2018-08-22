package cn.com.dove.framework.em;


import cn.com.dove.utils.StringTool;

/**
 * Created by wangdequan on 2018/1/11.
 */
public enum LoginState {

   /**
    * must login then you can invoke the url
    */
   MUST_LOGIN("MUST_LOGIN"),
   /**
    * must not login
    */
   MUST_NOT_LOGIN("MUST_NOT_LOGIN"),

    /**
     * allow visit this url when you are not login
     */
   ALLOW_NOT_LOGIN("ALLOW_NOT_LOGIN");



   protected final String chineseName;

   private LoginState(String chineseName){
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
