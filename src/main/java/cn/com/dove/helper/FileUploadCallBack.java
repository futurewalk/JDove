package cn.com.dove.helper;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

/**
 * Created by lenovo on 2018/6/16.
 */
public interface FileUploadCallBack {

    void callBack(InputStream in,String fileName) throws IOException;
}