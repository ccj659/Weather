package com.example.utils;

import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * Created by ccj on 2016/1/4.
 */
public class URLEncoderUtils {


    public static String encode(String paramString) {
        if (paramString == null || paramString.equals("")) {
            return "";
        }

        try {
            String str = new String(paramString.getBytes(), "UTF-8");
            str = URLEncoder.encode(str, "UTF-8");
            return str;
        } catch (Exception localException) {

        }

        return "";
    }

    public static String decode(String paramString) {
        if (paramString == null || paramString.equals("")) {
            return "";
        }

        try {
            String str = new String(paramString.getBytes(), "UTF-8");
            str = URLDecoder.decode(str, "UTF-8");
            return str;
        } catch (Exception localException) {
        }

        return "";
    }


}
