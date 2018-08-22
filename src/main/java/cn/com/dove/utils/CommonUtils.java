//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package cn.com.dove.utils;

import java.io.InputStream;
import java.net.URL;

public class CommonUtils {
    public static final String UTF8 = "UTF-8";

    public static InputStream getInputStreamFromClassPath(String filename) {
        if(isEmpty(filename)) {
            return null;
        } else {
            URL url = Thread.currentThread().getContextClassLoader().getResource(filename);
            System.out.println("[load file from classpath]:name={" + filename + "}, locator={" + url + "}");
            return url == null?null:Thread.currentThread().getContextClassLoader().getResourceAsStream(filename);
        }
    }
    public static boolean isEmpty(String input) {
        return input == null || input.trim().isEmpty();
    }
    public static int parseInt(Object data, int def) {
        if(data != null) {
            try {
                if(data instanceof Number) {
                    return ((Number)data).intValue();
                }

                return Integer.valueOf(String.valueOf(data)).intValue();
            } catch (Exception var3) {
                ;
            }
        }

        return def;
    }
}
