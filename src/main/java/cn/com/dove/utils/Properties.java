package cn.com.dove.utils;

/**
 * Created by wangdequan on 2018/1/12.
 */
public class Properties {

   private String dataBaseName;
   private String driver;
   private String url;
   private String characterEncoding;
   private String username;
   private String password;
   private int initialSize;
   private int maxActive;
   private String maxWait;
   private int timeBetweenEvictionRunsMillis;
   private int minEvictableIdleTimeMillis;
   private String validationQuery;
   private boolean testWhileIdle;
   private boolean testOnBorrow;
   private boolean testOnReturn;
   private boolean poolPreparedStatements;
   private int maxPoolPreparedStatementPerConnectionSize;

   public String getDataBaseName() {
      return dataBaseName;
   }

   public void setDataBaseName(String dataBaseName) {
      this.dataBaseName = dataBaseName;
   }

   public String getDriver() {
      return driver;
   }

   public void setDriver(String driver) {
      this.driver = driver;
   }

   public String getUrl() {
      return url;
   }

   public void setUrl(String url) {
      this.url = url;
   }

   public String getCharacterEncoding() {
      return characterEncoding;
   }

   public void setCharacterEncoding(String characterEncoding) {
      this.characterEncoding = characterEncoding;
   }

   public String getUsername() {
      return username;
   }

   public void setUsername(String username) {
      this.username = username;
   }

   public String getPassword() {
      return password;
   }

   public void setPassword(String password) {
      this.password = password;
   }

   public int getInitialSize() {
      return initialSize;
   }

   public void setInitialSize(int initialSize) {
      this.initialSize = initialSize;
   }

   public int getMaxActive() {
      return maxActive;
   }

   public void setMaxActive(int maxActive) {
      this.maxActive = maxActive;
   }

   public String getMaxWait() {
      return maxWait;
   }

   public void setMaxWait(String maxWait) {
      this.maxWait = maxWait;
   }

   public int getTimeBetweenEvictionRunsMillis() {
      return timeBetweenEvictionRunsMillis;
   }

   public void setTimeBetweenEvictionRunsMillis(int timeBetweenEvictionRunsMillis) {
      this.timeBetweenEvictionRunsMillis = timeBetweenEvictionRunsMillis;
   }

   public int getMinEvictableIdleTimeMillis() {
      return minEvictableIdleTimeMillis;
   }

   public void setMinEvictableIdleTimeMillis(int minEvictableIdleTimeMillis) {
      this.minEvictableIdleTimeMillis = minEvictableIdleTimeMillis;
   }

   public String getValidationQuery() {
      return validationQuery;
   }

   public void setValidationQuery(String validationQuery) {
      this.validationQuery = validationQuery;
   }

   public boolean isTestWhileIdle() {
      return testWhileIdle;
   }

   public void setTestWhileIdle(boolean testWhileIdle) {
      this.testWhileIdle = testWhileIdle;
   }

   public boolean isTestOnBorrow() {
      return testOnBorrow;
   }

   public void setTestOnBorrow(boolean testOnBorrow) {
      this.testOnBorrow = testOnBorrow;
   }

   public boolean isTestOnReturn() {
      return testOnReturn;
   }

   public void setTestOnReturn(boolean testOnReturn) {
      this.testOnReturn = testOnReturn;
   }

   public boolean isPoolPreparedStatements() {
      return poolPreparedStatements;
   }

   public void setPoolPreparedStatements(boolean poolPreparedStatements) {
      this.poolPreparedStatements = poolPreparedStatements;
   }

   public int getMaxPoolPreparedStatementPerConnectionSize() {
      return maxPoolPreparedStatementPerConnectionSize;
   }

   public void setMaxPoolPreparedStatementPerConnectionSize(int maxPoolPreparedStatementPerConnectionSize) {
      this.maxPoolPreparedStatementPerConnectionSize = maxPoolPreparedStatementPerConnectionSize;
   }
}
