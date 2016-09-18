package com.wangtao.androiddevice.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * Created by StaticMthod on 2016/9/9.
 * QQ：751190264
 */
public class StaticMthod {
    public static String getRandomSID() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        sb.append((char) (random.nextInt(26) + 'A'));
        sb.append(random.nextInt(8999) + 1000);
        sb.append((char) (random.nextInt(26) + 'A'));
        sb.append(random.nextInt(8999) + 1000);
        return sb.toString();
    }

    private static SimpleDateFormat format = new SimpleDateFormat(
            "yyyy-MM-dd HH:mm");

    public static String getFormDate(long time) {
        return format.format(new Date(time));
    }
}
