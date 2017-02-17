package com.wangtao.androiddevice.utils;

import android.telephony.SignalStrength;
import android.text.TextUtils;

import java.lang.reflect.Method;

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
        if (position >= array.length) {
            return null;
        }
        return array[position];
    }

    public static String getCellinfoFeil(String str, int index) {
        if (TextUtils.isEmpty(str)) {
            return "--";
        }
        if (str.indexOf("mMcc") == -1) {
            return "--";
        }
        str = str.substring(str.indexOf("mMcc"), str.length() - 1);
        System.out.println(str);
        String[] array = str.split(" ");
        if (index >= array.length) {
            return "--";
        }
        String resoult = array[index].substring(array[index].indexOf("=") + 1);
        if (TextUtils.equals("2147483647", resoult)) {
            return "--";
        }
        return resoult;
    }

    public static String getSiglength(String signLength) {
        // CellSignalStrengthLte: ss=31 rsrp=-76 rsrq=-6 rssnr=2147483647 cqi=2147483647 ta=2147483647
        if (TextUtils.isEmpty(signLength)) {
            return "-";
        }
        signLength = signLength.substring(signLength.indexOf(":"));

        return signLength.replaceAll("2147483647","--");
    }
}
