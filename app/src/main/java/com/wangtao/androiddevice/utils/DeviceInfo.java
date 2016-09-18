package com.wangtao.androiddevice.utils;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created by DeviceInfo on 2016/9/9.
 * QQ：751190264
 */
public class DeviceInfo {
    public static boolean initMtkDoubleSim(Context mContext) {
        try {
            TelephonyManager tm = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
            Class<?> c = Class.forName("com.android.internal.telephony.Phone");
            Field fields1 = c.getField("GEMINI_SIM_1");
            fields1.setAccessible(true);
            Integer simId_1 = (Integer) fields1.get(null);
            Field fields2 = c.getField("GEMINI_SIM_2");
            fields2.setAccessible(true);
            Integer simId_2 = (Integer) fields2.get(null);

            Method m = TelephonyManager.class.getDeclaredMethod(
                    "getSubscriberIdGemini", int.class);
            String imsi_1 = (String) m.invoke(tm, simId_1);
            String imsi_2 = (String) m.invoke(tm, simId_2);

            Method m1 = TelephonyManager.class.getDeclaredMethod(
                    "getDeviceIdGemini", int.class);
            String imei_1 = (String) m1.invoke(tm, simId_1);
            String imei_2 = (String) m1.invoke(tm, simId_2);

            Method mx = TelephonyManager.class.getDeclaredMethod(
                    "getPhoneTypeGemini", int.class);
            Integer phoneType_1 = (Integer) mx.invoke(tm, simId_1);
            Integer phoneType_2 = (Integer) mx.invoke(tm, simId_2);

            if (TextUtils.isEmpty(imsi_1) && (!TextUtils.isEmpty(imsi_2))) {
                return true;
            }
            if (TextUtils.isEmpty(imsi_2) && (!TextUtils.isEmpty(imsi_1))) {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * 获取手机服务商信息
     */
    public static String getProvidersName(TelephonyManager telephonyManager) {
        String ProvidersName = "N/A";
        try {
            String IMSI = telephonyManager.getSubscriberId();
            // IMSI号前面3位460是国家，紧接着后面2位00 02是中国移动，01是中国联通，03是中国电信。
            System.out.println(IMSI);
            if (IMSI.startsWith("46000") || IMSI.startsWith("46002")) {
                ProvidersName = "中国移动";
            } else if (IMSI.startsWith("46001")) {
                ProvidersName = "中国联通";
            } else if (IMSI.startsWith("46003")) {
                ProvidersName = "中国电信";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ProvidersName;
    }

    public static String getSimUsim(TelephonyManager tm) {
        String simType = "None";
        int type = tm.getNetworkType();
        //判断类型值，并且命名 　
        //我的手机卡是联通USIM卡，在这儿取出来的值为10NETWORK_TYPE_HSPA
        //所以取出来的是ＵＩＭ其实就是未知

        switch (type) {
            case TelephonyManager.NETWORK_TYPE_UNKNOWN://0
                simType= "UNKOWN";
                break;
            case TelephonyManager.NETWORK_TYPE_GPRS://1
                simType= "SIM";
                break;
            case TelephonyManager.NETWORK_TYPE_EDGE://2
                simType= "SIM";
                break;
            case TelephonyManager.NETWORK_TYPE_UMTS://3
                simType= "USIM";
                break;
            case TelephonyManager.NETWORK_TYPE_CDMA://4
                simType= "Either IS95A or IS95B Card";
                break;
            case TelephonyManager.NETWORK_TYPE_EVDO_0://5
                simType= "EVDO revision 0 Card";
                break;
            case TelephonyManager.NETWORK_TYPE_EVDO_A://6
                simType= "EVDO revision A Card";
                break;
            case TelephonyManager.NETWORK_TYPE_1xRTT://7
                simType= "1xRTT Card";
                break;
            case TelephonyManager.NETWORK_TYPE_HSDPA://8
                simType= "HSDPA Card";
                break;
            case TelephonyManager.NETWORK_TYPE_HSUPA://9
                simType= "HSUPA Card";
                break;
            case TelephonyManager.NETWORK_TYPE_HSPA://10
                simType= "USIM";
                break;

        }
        return simType;
    }
}
