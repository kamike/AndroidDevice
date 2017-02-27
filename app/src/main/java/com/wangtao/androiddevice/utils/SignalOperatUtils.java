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

    public static int getCellinfoFeilInt(String str, int index) {
        //CellIdentityGsm:{ mMcc=460 mMnc=0 mLac=10147 mCid=5341 mArfcn=46 mBsic=0x10} CellSignalStrengthGsm: ss=31 ber=99 mTa=0}
        if (TextUtils.isEmpty(str)) {
            return 0;
        }
        if (str.indexOf("mMcc") == -1) {
            return 0;
        }
        str = str.substring(str.indexOf("mMcc"), str.length() - 1);
        System.out.println(str);
        String[] array = str.split(" ");
        if (index >= array.length) {
            return 0;
        }
        String resoult = array[index].substring(array[index].indexOf("=") + 1);
        if (TextUtils.equals("2147483647", resoult)) {
            return 0;
        }
        int r = 0;
        try {
            r = Integer.parseInt(resoult);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        return r;
    }

    public static String getCellinfoFeilString(String str, int index) {
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

        return signLength.replaceAll("2147483647", "--");
    }

    public static int getAsu(String signLength) {
        // CellSignalStrengthLte: ss=31 rsrp=-76 rsrq=-6 rssnr=2147483647 cqi=2147483647 ta=2147483647
        System.out.println("====signLength======" + signLength);
        if (TextUtils.isEmpty(signLength)) {
            return -1;
        }
        signLength = signLength.substring(signLength.indexOf(":"));

        String str1 = signLength.split(" ")[1];
        System.out.println(str1);
        if (str1.indexOf("=") == -1) {
            return -1;
        }
        String str2 = str1.split("=")[1];
        System.out.println(str2);
        int asu = -1;
        try {
            asu = Integer.parseInt(str2);
        } catch (NumberFormatException e) {
            e.getMessage();
        }



        return asu;
    }
}
