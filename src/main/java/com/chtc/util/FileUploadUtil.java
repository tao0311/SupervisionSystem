package com.chtc.util;

import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Random;

/**
 * Created by admin on 2017/2/15.
 */
public class FileUploadUtil {
    private  static  final org.slf4j.Logger logger = LoggerFactory.getLogger(FileUploadUtil.class);

    private static final String contextPath = "/file";

//    @Value("#{configProperties['fileUpLoadDisk']}")
//    private static String fileUpLoadDisk;

    private static File[] roots = File.listRoots();
    private static String docBase = null;
    static {
        if (roots.length == 1) {
            docBase = roots[0].getPath();
        }
        if (roots.length > 1) {
            docBase = roots[1].getPath();
        }
    }

    public static String uploadFile(InputStream is, String suffix) {
        String fileUpLoadDisk = PropertiesUtils.getProperty("fileUpLoadDisk");
        logger.info("upload file directory:" + fileUpLoadDisk);
        // windows tomcat file path config:
        // <Context path="/RoadShowScoring/file" privileged="true" docBase="D:\RoadShowScoring\file"/>
        try {
            Random random = new Random(10);
            String number = System.currentTimeMillis() + "-" + random.nextLong();
            logger.info("[FileUploadUtil]uploadFile->number:"+number);
//            String newPath = fileUpLoadDisk + File.separator + "RoadShowScoring/file";
            String newPath = fileUpLoadDisk;
            logger.info("[FileUploadUtil]uploadFile->newPath:"+newPath);
            String newFile = newPath + File.separator + number + suffix;
            logger.info("[FileUploadUtil]uploadFile->newFile:"+newFile);
            File path = new File(newPath);
            if (!path.exists()) {
                path.mkdirs();
            }
            FileOutputStream fs = new FileOutputStream(newFile);
            byte[] buffer = new byte[8900];
            int length;
            while ( (length = is.read(buffer)) != -1) {
                fs.write(buffer, 0, length);
            }
            fs.flush();
            is.close();
//            is.close();
            return contextPath + "/" + number + suffix;
        } catch (Exception e) {
            e.printStackTrace();

        }
        return null;
    }

    public static void main(String[] args) {
        System.out.println(docBase);
    }
}
