package com.bytetube.util;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class FileUtil {
    private static String seperator = System.getProperty("file.separator");
    private static final SimpleDateFormat sDateFormat = new SimpleDateFormat(
            "yyyyMMddHHmmss"); // 时间格式化的格式
    private static final Random r = new Random();

    //返回项目图片的根路径
    public static String getImgBasePath() {
        String os = System.getProperty("os.name");
        String basePath = "";
        if (os.toLowerCase().startsWith("win")) {
            basePath = "D:/projectdev/image/";
        } else {
            basePath = "/Users/dalluo/Desktop/image/";
        }
        basePath = basePath.replace("/", seperator);
        return basePath;
    }

    //	返回项目图片的子路径
    public static String getShopImagePath(long shopId) {
        String imagePath = "/upload/images/item/shop/" + shopId + "/";
        return imagePath.replace("/", seperator);
    }

    public static String getHeadLineImagePath() {
        String imagePath = "/upload/images/item/headtitle/";
        return imagePath.replace("/", seperator);
    }

    public static String getShopCategoryPath() {
        String imagePath = "/upload/images/item/shopcategory/";
        return imagePath.replace("/", seperator);
    }


    public static String getRandomFileName() {
        // 生成随机文件名：当前年月日时分秒+五位随机数（为了在实际项目中防止文件同名而进行的处理）
        int rannum = (int) (r.nextDouble() * (99999 - 10000 + 1)) + 10000; // 获取随机数
        String nowTimeStr = sDateFormat.format(new Date()); // 当前时间
        return nowTimeStr + rannum;
    }

    public static void deleteFile(String storePath) {
        File file = new File(getImgBasePath() + storePath);
        if (file.exists()) {
            if (file.isDirectory()) {
                File files[] = file.listFiles();
                for (int i = 0; i < files.length; i++) {
                    files[i].delete();
                }
            }
            file.delete();
        }
    }
}
