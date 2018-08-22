package cn.com.dove.helper;

import com.alibaba.fastjson.JSON;

import java.util.Map;

public  class MapValue{
    private Class value;
    private Map map;

    public MapValue(Class value,Map m){
        this.value = value;
        this.map = m;
    }
    public MapValue(){

    }

    public void add(Class value ,Map m){
        this.value = value;
        this.map = m;
    }

    public Class getValue() {
        return value;
    }

    public void setValue(Class value) {
        this.value = value;
    }

    public Map getMap() {
        return map;
    }

    public void setMap(Map map) {
        this.map = map;
    }

    public String toString(){
        return JSON.toJSONString(this);
    }

}