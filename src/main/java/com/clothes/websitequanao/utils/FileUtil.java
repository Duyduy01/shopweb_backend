package com.clothes.websitequanao.utils;

public class FileUtil {
    public static String getImgName(String linkImg) {
        String[] list = linkImg.split("/");
        linkImg = list[list.length - 1];
        String result = linkImg.replace("?alt=media", "");
        return result;
    }
}
