package cn.com.dove.helper;

/**
 * Created by lenovo on 2018/6/16.
 */
public class FileInfo {
    private byte[] bytes;
    private String fileName;
    private String fileExtName;

    public byte[] getBytes() {
        return bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
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