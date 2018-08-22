package cn.com.dove.helper;

import org.apache.poi.util.IOUtils;
import sun.nio.ch.IOUtil;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FileUpload {

    private HttpServletRequest request;
    private List pathList;
    private String pathOne;
    private String fileName;
    private String fileExtName;

    /**
     * 上传一个
     * @param filePath
     * @return
     */
    public  String upload(String filePath){
        List list = new ArrayList();
        try {
            Map<String,List> mapList =  FileHelper.uploadFileMethod(request,filePath);
            list = mapList.get("fileNameList");
            setPathList(mapList.get("pathList"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list==null?null:String.valueOf(list.get(0));
    }
    public FileInfo upload() throws IOException {
        return FileHelper.uploadFile(request);
    }

    public String rootPath(){
        return this.getRequest().getServletContext().getRealPath("//")+FileHelper.separator;
    }

    public String getPathOne() {
        return pathOne;
    }

    public void setPathOne(String pathOne) {
        this.pathOne = pathOne;
    }

    public List getPathList() {
        return pathList;
    }

    public void setPathList(List pathList) {
        this.pathList = pathList;
    }

    public HttpServletRequest getRequest() {
        return request;
    }
    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileExtName() {
        return fileExtName;
    }

    public void setFileExtName(String fileExtName) {
        this.fileExtName = fileExtName;
    }
}
