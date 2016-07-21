package com.app.data;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by ningd on 2016/1/4.
 */
public final class Utils {
    /**
     *
     * @description 去除空格、回车、换行符、制表符
     * @param str
     * @return
     */
    public static String replaceBlank(String str) {
        String dest = "";
        if (str!=null) {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(str);
            dest = m.replaceAll("");
        }
        return dest;
    }

}
