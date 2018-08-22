package cn.com.dove.framework.orm;

import com.alibaba.fastjson.JSON;

public class Scheme {

    private long id;

    private String dbSt;

    private String dbCreateOpen;

    private String dbCp;

    private String dbEngine;

    private boolean dbCover;

    private String dbPackage;

    private String dbIgnore;

    private String dbAppoint;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDbSt() {
        return dbSt;
    }

    public void setDbSt(String dbSt) {
        this.dbSt = dbSt;
    }

    public String getDbCp() {
        return dbCp;
    }

    public void setDbCp(String dbCp) {
        this.dbCp = dbCp;
    }

    public String getDbEngine() {
        return dbEngine;
    }

    public void setDbEngine(String dbEngine) {
        this.dbEngine = dbEngine;
    }

    public boolean isDbCover() {
        return dbCover;
    }

    public void setDbCover(boolean dbCover) {
        this.dbCover = dbCover;
    }

    public String getDbPackage() {
        return dbPackage;
    }

    public void setDbPackage(String dbPackage) {
        this.dbPackage = dbPackage;
    }

    public String getDbIgnore() {
        return dbIgnore;
    }

    public void setDbIgnore(String dbIgnore) {
        this.dbIgnore = dbIgnore;
    }

    public String getDbAppoint() {
        return dbAppoint;
    }

    public void setDbAppoint(String dbAppoint) {
        this.dbAppoint = dbAppoint;
    }

    public String getDbCreateOpen() {
        return dbCreateOpen;
    }

    public void setDbCreateOpen(String dbCreateOpen) {
        this.dbCreateOpen = dbCreateOpen;
    }

    public String toString(){
        return JSON.toJSONString(this);
    }
}