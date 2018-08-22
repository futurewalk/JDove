package cn.com.dove.framework.orm;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wangdequan on 2018/1/12.
 */
public class DataSourceManager {
    private static Logger logger = LoggerFactory.getLogger(DataSourceManager.class);
    private static Map<String,Map<String,Object>> PROP_CONTAINER = new HashMap<>();
    private static Map<String,DataSource> dataSourceContainer = new HashMap<>();
    private static String SQL_DRIVER = null;

    public static Map<String,Map<String,Object>> start(String path) throws Exception {
        return doStart(new File(path));
    }
    public static Map<String,Map<String,Object>> start(File file) throws Exception {
        return doStart(file);
    }
    public static Map<String,Map<String,Object>> doStart(File file) throws Exception {
        SAXReader reader = new SAXReader();
        Document document = reader.read(file);
        startAnalysisDom(document);
        SchemeExport.loadDbCreateConfig();
        return PROP_CONTAINER;
    }

    private static void startAnalysisDom(Document document) throws Exception {

        Element root = document.getRootElement();

        listNodes(root.elements());
    }
    private static void listNodes(List<Element> list) throws Exception {

        Map<String, Map<String, Object>> propMap = new HashMap<String, Map<String, Object>>();

        for (Element element : list) {
            List<Element> childEles = element.elements();
            List<Attribute> attributes = element.attributes();

            Map<String, Object> dbMap = new HashMap<String, Object>();
            for (int i = 0; i < attributes.size(); i++) {
                Attribute attr = attributes.get(i);
                propMap.put(attr.getValue(), dbMap);
            }

            for (Element ele : childEles) {
                List<Attribute> attrs = ele.attributes();
                listAttributes(attrs, dbMap, ele.getText());
            }
        }

        PROP_CONTAINER = propMap;

        for (String key : propMap.keySet()) {
            Map<String, Object> dbMap = propMap.get(key);
            initDataSource(key,dbMap);
        }
    }
    public static void  initDataSource(String dataSourceName,Map<String,Object> properties) throws Exception {
        DataSource dataSource = DruidDataSourceFactory.createDataSource(properties);
        logger.info("started init dataSource = {}",dataSourceName);
        dataSourceContainer.put(dataSourceName,dataSource);

    }


    public static void listAttributes(List<Attribute> attributes, Map<String, Object> propMap, String text) {
        for (Attribute attr : attributes) {
            if ("driver".equals(attr.getValue())){
                SQL_DRIVER = text;
            }
            propMap.put(attr.getValue(), text);
        }
    }

    public static String  driver(){
        return SQL_DRIVER;
    }

    public static DataSource getDataSource(String sourceName){
        return dataSourceContainer.get(sourceName);
    }

    public static void main(String[] args) {
        try {
            start("D:\\scarfworkspace\\JDove\\src\\main\\resources\\orm.xml");

        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

