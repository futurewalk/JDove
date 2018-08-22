package cn.com.dove.helper;

import cn.com.dove.utils.DateTool;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadBase;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.ProgressListener;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.poi.util.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class FileHelper {
    private static final Logger logger = LoggerFactory.getLogger(FileHelper.class);
    private static char[] charArray = "abcdefghijklmnopqstuvwxyz1234567890".toCharArray();
    public static final String separator = File.separator;

    public static FileInfo uploadFile(HttpServletRequest req) {
        FileInfo fileInfo = new FileInfo();
        try {
            upload(req, new FileUploadCallBack() {
                @Override
                public void callBack(InputStream in, String fileName) throws IOException {
                    String fileExtName = fileName.substring(fileName.lastIndexOf(".") + 1);
                    byte[] bytes = IOUtils.toByteArray(in);
                    fileInfo.setBytes(bytes);
                    fileInfo.setFileName(fileName);
                    fileInfo.setFileExtName(fileExtName);
                }
            });

        } catch (FileUploadException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileInfo;
    }

    private static InputStream[] upload(HttpServletRequest req, FileUploadCallBack cb) throws FileUploadException, IOException {

        InputStream[] inputStreams = null;
        try {

            DiskFileItemFactory factory = new DiskFileItemFactory();

            factory.setSizeThreshold(102400);

            ServletFileUpload upload = new ServletFileUpload(factory);

     /*       upload.setProgressListener(new ProgressListener() {
                public void update(long pBytesRead, long pContentLength, int arg2) {

                }
            });*/
            upload.setHeaderEncoding("UTF-8");

            if (!ServletFileUpload.isMultipartContent(req)) {
                return null;
            }
            upload.setFileSizeMax(209715200L);
            upload.setSizeMax(209715200L);

            List<FileItem> list = upload.parseRequest(req);

            inputStreams = new InputStream[list.size()];
            for (int i = 0 ;i < list.size();i++) {
                FileItem item  = list.get(i);
                if (!item.isFormField()) {
                    String filename = item.getName();
                    if ((filename != null) && (!filename.trim().equals(""))) {
                        InputStream in = item.getInputStream();
                        inputStreams[i] = in;
                        if (cb != null){
                            cb.callBack(in, filename);
                        }
                    }
                }
            }
        } catch (FileUploadBase.FileSizeLimitExceededException e) {
            throw e;
        } catch (FileUploadBase.SizeLimitExceededException e) {
            throw e;
        } catch (Exception e) {
            throw e;
        }
        return inputStreams;
    }
    public static Map<String, List> uploadFileMethod(HttpServletRequest req, String filePath) throws FileUploadException, IOException {

        Map<String, List> mapList = new HashMap<>();
        List pathList = new ArrayList();
        List fileNameList = new ArrayList();

        upload(req, new FileUploadCallBack() {
            @Override
            public void callBack(InputStream in, String fileName) throws IOException {
                if ((fileName != null) && (!fileName.trim().equals(""))) {
                    fileName = fileName.substring(fileName.lastIndexOf("\\") + 1);
                    String fileExtName = fileName.substring(fileName.lastIndexOf(".") + 1);

                    String saveFilename = makeFileName(fileName);
                    StringBuilder realPath = new StringBuilder();
                    realPath.append(filePath).append(separator);
                    realPath.append(saveFilename).append(".").append(fileExtName);

                    FileOutputStream out = new FileOutputStream(realPath.toString());
                    pathList.add(realPath.toString());
                    fileNameList.add(saveFilename + "." + fileExtName);

                    byte[] buffer = new byte[1024];
                    int len = 0;
                    while ((len = in.read(buffer)) > 0) {
                        out.write(buffer, 0, len);
                    }
                    closeAll(in, out);
                }
                mapList.put("fileNameList",fileNameList);
                mapList.put("pathList",pathList);
            }
        });

        return mapList;
    }

    private static String makeFileName(String filename) {
        Random r = new Random();
        StringBuilder sb = new StringBuilder(DateTool.getString(new Date(), "yyyyMMdd")).append(System.currentTimeMillis());
        synchronized (new Object()) {
            for (int i = 0; i < 10; i++) {
                char c = charArray[r.nextInt(10)];
                sb.append(c);
            }
        }
        return sb.toString();
    }

    /**
     * 关闭流
     *
     * @param closeables
     */
    public static void closeAll(Closeable... closeables) throws IOException {
        for (Closeable closeable : closeables) {
            try {
                closeable.close();
            } catch (IOException e) {
                logger.error("关闭流出现异常:{}", e);
                throw e;
            }
        }
    }
}
