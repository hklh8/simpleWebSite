package com.hklh8.utils;

import java.util.UUID;

/**
 * Created by GouBo on 2018/1/28.
 */
public class FileUtils {

    /**
     * 检测文件扩展名
     */
    public static boolean checkFile(String fileName) {
        //设置允许上传文件类型
        String suffixList = "jpg,gif,png,ico,bmp,jpeg";
        // 获取文件后缀
        String suffix = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());
        if (suffixList.contains(suffix.trim().toLowerCase())) {
            return true;
        }
        return false;
    }


    /**
     * 获取文件扩展名
     */
    public static String getExtensionName(String filename) {
        if (filename != null && filename.length() > 0) {
            int dot = filename.lastIndexOf(".");
            if (dot > -1 && dot < filename.length() - 1) {
                return filename.substring(dot + 1);
            }
        }
        return "";
    }

    /**
     * 取得GUID
     */
    public static String getGUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
}
