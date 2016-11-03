package com.wangtao.androiddevice.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

import static android.telephony.TelephonyManager.NETWORK_TYPE_1xRTT;
import static android.telephony.TelephonyManager.NETWORK_TYPE_CDMA;
import static android.telephony.TelephonyManager.NETWORK_TYPE_EDGE;
import static android.telephony.TelephonyManager.NETWORK_TYPE_EHRPD;
import static android.telephony.TelephonyManager.NETWORK_TYPE_EVDO_0;
import static android.telephony.TelephonyManager.NETWORK_TYPE_EVDO_A;
import static android.telephony.TelephonyManager.NETWORK_TYPE_EVDO_B;
import static android.telephony.TelephonyManager.NETWORK_TYPE_HSDPA;
import static android.telephony.TelephonyManager.NETWORK_TYPE_HSPA;
import static android.telephony.TelephonyManager.NETWORK_TYPE_HSPAP;
import static android.telephony.TelephonyManager.NETWORK_TYPE_HSUPA;
import static android.telephony.TelephonyManager.NETWORK_TYPE_IDEN;
import static android.telephony.TelephonyManager.NETWORK_TYPE_LTE;
import static android.telephony.TelephonyManager.NETWORK_TYPE_UMTS;
import static android.telephony.TelephonyManager.NETWORK_TYPE_UNKNOWN;

/**
 * Created by SignMath on 2016/10/13.
 * QQ：751190264
 */

public class SignMath {
    private static final int NETWORK_TYPE_UNAVAILABLE = -1;
    // private static final int NETWORK_TYPE_MOBILE = -100;
    private static final int NETWORK_TYPE_WIFI = -101;
    private static final int NETWORK_CLASS_WIFI = -101;
    private static final int NETWORK_CLASS_UNAVAILABLE = -1;
    /**
     * Unknown network class.
     */
    public static final int NETWORK_CLASS_UNKNOWN = 0;
    /**
     * Class of broadly defined "2G" networks.
     */
    public static final int NETWORK_CLASS_2_G = 1;
    /**
     * Class of broadly defined "3G" networks.
     */
    public static final int NETWORK_CLASS_3_G = 2;
    /**
     * Class of broadly defined "4G" networks.
     */
    public static final int NETWORK_CLASS_4_G = 3;

    public static String getCurrentNetworkType234G(Context context, int networkType) {
        int networkClass = getNetworkClass(context, networkType);
        String type = "未知";
        switch (networkClass) {
            case NETWORK_CLASS_UNAVAILABLE:
                type = "无";
                break;
            case NETWORK_CLASS_WIFI:
                type = "Wi-Fi";
                break;
            case NETWORK_CLASS_2_G:
                type = "2G";
                break;
            case NETWORK_CLASS_3_G:
                type = "3G";
                break;
            case NETWORK_CLASS_4_G:
                type = "4G";
                break;
            case NETWORK_CLASS_UNKNOWN:
                type = "未知";
                break;
        }
        return type;
    }

    private static int getNetworkClass(Context context, int myNetworkType) {
        int networkType = NETWORK_TYPE_UNKNOWN;
        try {
            final NetworkInfo network = ((ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE))
                    .getActiveNetworkInfo();
            if (network != null && network.isAvailable()
                    && network.isConnected()) {
                int type = network.getType();
                if (type == ConnectivityManager.TYPE_WIFI) {
                    networkType = NETWORK_TYPE_WIFI;
                } else if (type == ConnectivityManager.TYPE_MOBILE) {
                    networkType = myNetworkType;
                }
            } else {
                networkType = NETWORK_TYPE_UNAVAILABLE;
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return getNetworkClassByType(networkType);

    }

    public static int getNetworkClassByType(int networkType) {
        switch (networkType) {
            case NETWORK_TYPE_UNAVAILABLE:
                return NETWORK_CLASS_UNAVAILABLE;
            case NETWORK_TYPE_WIFI:
                return NETWORK_CLASS_WIFI;
            case TelephonyManager.NETWORK_TYPE_GPRS:
            case 16:
            case NETWORK_TYPE_EDGE:
            case NETWORK_TYPE_CDMA:
            case NETWORK_TYPE_1xRTT:
            case NETWORK_TYPE_IDEN:
                return NETWORK_CLASS_2_G;
            case NETWORK_TYPE_UMTS:
            case NETWORK_TYPE_EVDO_0:
            case NETWORK_TYPE_EVDO_A:
            case NETWORK_TYPE_HSDPA:
            case NETWORK_TYPE_HSUPA:
            case NETWORK_TYPE_HSPA:
            case NETWORK_TYPE_EVDO_B:
            case NETWORK_TYPE_EHRPD:
            case NETWORK_TYPE_HSPAP:
            case 17:
                return NETWORK_CLASS_3_G;
            case NETWORK_TYPE_LTE:
            case 18:
                return NETWORK_CLASS_4_G;
            default:
                return NETWORK_CLASS_UNKNOWN;
        }
    }
    public static String getNetworkClassByTypeName(int networkType) {
        switch (networkType) {
            case NETWORK_TYPE_UNAVAILABLE:
                return "unknow";
            case TelephonyManager.NETWORK_TYPE_GPRS:
            case 16:
            case NETWORK_TYPE_EDGE:
            case NETWORK_TYPE_CDMA:
            case NETWORK_TYPE_1xRTT:
            case NETWORK_TYPE_IDEN:
                return "2G";
            case NETWORK_TYPE_UMTS:
            case NETWORK_TYPE_EVDO_0:
            case NETWORK_TYPE_EVDO_A:
            case NETWORK_TYPE_HSDPA:
            case NETWORK_TYPE_HSUPA:
            case NETWORK_TYPE_HSPA:
            case NETWORK_TYPE_EVDO_B:

            case NETWORK_TYPE_HSPAP:
            case 17:
                return "3G";
            case NETWORK_TYPE_EHRPD:
            case NETWORK_TYPE_LTE:
            case 18:
                return "4G";
            default:
                return "unknow";
        }
    }

    public static String getNetorkTypeName(int rt) {
        String rtString;

        switch(rt) {
            case RIL_RADIO_TECHNOLOGY_UNKNOWN:
                rtString = "Unknown";
                break;
            case RIL_RADIO_TECHNOLOGY_GPRS:
                rtString = "GPRS";
                break;
            case RIL_RADIO_TECHNOLOGY_EDGE:
                rtString = "EDGE";
                break;
            case RIL_RADIO_TECHNOLOGY_UMTS:
                rtString = "UMTS";
                break;
            case RIL_RADIO_TECHNOLOGY_IS95A:
                rtString = "CDMA-IS95A";
                break;
            case RIL_RADIO_TECHNOLOGY_IS95B:
                rtString = "CDMA-IS95B";
                break;
            case RIL_RADIO_TECHNOLOGY_1xRTT:
                rtString = "1xRTT";
                break;
            case RIL_RADIO_TECHNOLOGY_EVDO_0:
                rtString = "EvDo-rev.0";
                break;
            case RIL_RADIO_TECHNOLOGY_EVDO_A:
                rtString = "EvDo-rev.A";
                break;
            case RIL_RADIO_TECHNOLOGY_HSDPA:
                rtString = "HSDPA";
                break;
            case RIL_RADIO_TECHNOLOGY_HSUPA:
                rtString = "HSUPA";
                break;
            case RIL_RADIO_TECHNOLOGY_HSPA:
                rtString = "HSPA";
                break;
            case RIL_RADIO_TECHNOLOGY_EVDO_B:
                rtString = "EvDo-rev.B";
                break;
            case RIL_RADIO_TECHNOLOGY_EHRPD:
                rtString = "eHRPD";
                break;
            case RIL_RADIO_TECHNOLOGY_LTE:
                rtString = "LTE";
                break;
            case RIL_RADIO_TECHNOLOGY_HSPAP:
                rtString = "HSPAP";
                break;
            case RIL_RADIO_TECHNOLOGY_GSM:
                rtString = "GSM";
                break;
            case RIL_RADIO_TECHNOLOGY_IWLAN:
                rtString = "IWLAN";
                break;
            case RIL_RADIO_TECHNOLOGY_TD_SCDMA:
                rtString = "TD-SCDMA";
                break;
            default:
                rtString = "Unexpected";
                break;
        }
        return rtString;
    }
    public static final int RIL_RADIO_TECHNOLOGY_UNKNOWN = 0;
    
    public static final int RIL_RADIO_TECHNOLOGY_GPRS = 1;
    
    public static final int RIL_RADIO_TECHNOLOGY_EDGE = 2;
    
    public static final int RIL_RADIO_TECHNOLOGY_UMTS = 3;
    
    public static final int RIL_RADIO_TECHNOLOGY_IS95A = 4;
    
    public static final int RIL_RADIO_TECHNOLOGY_IS95B = 5;
    
    public static final int RIL_RADIO_TECHNOLOGY_1xRTT = 6;
    
    public static final int RIL_RADIO_TECHNOLOGY_EVDO_0 = 7;
    
    public static final int RIL_RADIO_TECHNOLOGY_EVDO_A = 8;
    
    public static final int RIL_RADIO_TECHNOLOGY_HSDPA = 9;
    
    public static final int RIL_RADIO_TECHNOLOGY_HSUPA = 10;
    
    public static final int RIL_RADIO_TECHNOLOGY_HSPA = 11;
    
    public static final int RIL_RADIO_TECHNOLOGY_EVDO_B = 12;
    
    public static final int RIL_RADIO_TECHNOLOGY_EHRPD = 13;
    
    public static final int RIL_RADIO_TECHNOLOGY_LTE = 14;
    
    public static final int RIL_RADIO_TECHNOLOGY_HSPAP = 15;
    /**
     * GSM radio technology only supports voice. It does not support data.
     * 
     */
    public static final int RIL_RADIO_TECHNOLOGY_GSM = 16;
    
    public static final int RIL_RADIO_TECHNOLOGY_TD_SCDMA = 17;
    /**
     * IWLAN
     * 
     */
    public static final int RIL_RADIO_TECHNOLOGY_IWLAN = 18;

}
