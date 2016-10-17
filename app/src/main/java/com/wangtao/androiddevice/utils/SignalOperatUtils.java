package com.wangtao.androiddevice.utils;

import android.telephony.SignalStrength;

import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * Created by Administrator on 2016/10/2.
 */

public class SignalOperatUtils {


    public static String getFeil(String method) {
        try {
            Class c = SignalStrength.class;
            Method m = c.getMethod(method);
            int value = (int) m.invoke(c.newInstance());
            return value + "";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getAllParams(String signalStrength, int position) {
        String[] array = signalStrength.toString().split(" ");
        System.out.println("array:" + Arrays.toString(array));
        System.out.println("array长度:" + array.length);
        if (position >= array.length) {
            return null;
        }
        return array[position];
    }
}
