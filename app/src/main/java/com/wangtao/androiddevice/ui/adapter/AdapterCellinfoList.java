package com.wangtao.androiddevice.ui.adapter;

import android.content.Context;
import android.os.Build;
import android.telephony.CellIdentityGsm;
import android.telephony.CellIdentityLte;
import android.telephony.CellIdentityWcdma;
import android.telephony.CellInfo;
import android.telephony.CellInfoCdma;
import android.telephony.CellInfoGsm;
import android.telephony.CellInfoLte;
import android.telephony.CellInfoWcdma;
import android.telephony.CellSignalStrengthCdma;
import android.telephony.CellSignalStrengthLte;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.wangtao.androiddevice.R;
import com.wangtao.androiddevice.utils.SignalOperatUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

/**
 * Created by AdapterCellinfoList on 2016/10/18.
 * QQ：751190264
 */

public class AdapterCellinfoList extends BaseAdapter {

    private final Context context;
    private final List<CellInfo> list;

    public AdapterCellinfoList(Context context, List<CellInfo> cellInfo) {
        this.context = context;
        this.list = cellInfo;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView tv = null;
        if (tv == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.view_item_cell_list, null);
            tv = (TextView) convertView.findViewById(R.id.cell_info_list_tv);
        }
        tv.setTag(position);
        CellInfo cellInfo = list.get(position);
        if (cellInfo instanceof CellInfoLte) {
            String lacMnc = ((CellInfoLte) cellInfo).getCellIdentity().toString();
            String signnLength = (((CellInfoLte) cellInfo).getCellSignalStrength().toString());

            CellInfoLte lteInfo = (CellInfoLte) cellInfo;
            CellIdentityLte cellId = lteInfo.getCellIdentity();
            int crfcn = -1;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                crfcn = cellId.getEarfcn();
            }

            showCellTxt(lacMnc, signnLength, "LTE", tv, crfcn, lteInfo.getCellSignalStrength());

        }
        //获取所有的cdma网络信息
        if (cellInfo instanceof CellInfoCdma) {
            String lacMnc = ((CellInfoCdma) cellInfo).getCellIdentity().toString();
            CellSignalStrengthCdma length = ((CellInfoCdma) cellInfo).getCellSignalStrength();

            String signnLength = (((CellInfoCdma) cellInfo).getCellSignalStrength().toString());
            //cdma模式取的是dbm
            showCellTxt(lacMnc, signnLength, "CDMA", tv, length.getCdmaDbm(), null);
        }
        if (cellInfo instanceof CellInfoGsm) {
            String lacMnc = ((CellInfoGsm) cellInfo).getCellIdentity().toString();
            String signnLength = (((CellInfoGsm) cellInfo).getCellSignalStrength().toString());

            CellInfoGsm cdmaInfo = (CellInfoGsm) cellInfo;
            CellIdentityGsm cellId = cdmaInfo.getCellIdentity();
            int crfcn = -1;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                crfcn = cellId.getArfcn();
            }
            showCellTxt(lacMnc, signnLength, "GSM", tv, crfcn, null);
        }
        if (cellInfo instanceof CellInfoWcdma) {
            String lacMnc = ((CellInfoWcdma) cellInfo).getCellIdentity().toString();
            String signnLength = (((CellInfoWcdma) cellInfo).getCellSignalStrength().toString());

            CellInfoWcdma cdmaInfo = (CellInfoWcdma) cellInfo;
            CellIdentityWcdma cellId = cdmaInfo.getCellIdentity();
            int crfcn = -1;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                crfcn = cellId.getUarfcn();
            }
            showCellTxt(lacMnc, signnLength, "WCDMA", tv, crfcn, null);
        }

        return convertView;
    }

    private void showCellTxt(String lacMnc, String signLength, String tag, TextView tv, int arfcn, CellSignalStrengthLte lteCell) {
        StringBuffer sb = new StringBuffer();
        if (tag.equals("WCDMA")) {
            int mcc = SignalOperatUtils.getCellinfoFeilInt(lacMnc, 0);


            sb.append("MCC:").append(mcc == 460 ? 460 : 460);
            sb.append(",MNC:");
            String mnc = SignalOperatUtils.getCellinfoFeilString(lacMnc, 1);

            if (TextUtils.equals(mnc, "00") || TextUtils.equals(mnc, "01") || TextUtils.equals(mnc, "02") || TextUtils.equals(mnc, "03") || TextUtils.equals(mnc, "11")) {
                sb.append(mnc);
            } else {
                sb.append("--");
            }

        }
        sb.append(",CID:");
        int ciLong = SignalOperatUtils.getCellinfoFeilInt(lacMnc, 2);

        System.out.println("1111-ciLong:" + ciLong + "," + tv.getTag());

        if (TextUtils.equals(tag, "GSM") || TextUtils.equals(tag, "CDMA")) {
            if (ciLong >= 65535 || ciLong <= 0) {
                sb.append("--");
            } else {
                sb.append(ciLong);
            }

        } else {
            //46394213
            if (ciLong >= Integer.MAX_VALUE || ciLong == 0) {
                sb.append("--");
            } else {
                sb.append(ciLong % 256);
            }
        }
        sb.append(",PCI:");
        int pci = SignalOperatUtils.getCellinfoFeilInt(lacMnc, 3);
        if (pci > 504 || pci < 0) {
            sb.append("--");
        } else {
            sb.append(pci);

        }

        sb.append(",LAC:");
        int lac = SignalOperatUtils.getCellinfoFeilInt(lacMnc, 4);
        if (lac < 0 || lac > 65535) {
            sb.append("--");
        } else {
            sb.append(lac);
        }
        int asu = 0;

        sb.append(",ARFCN:");
        if (TextUtils.equals("CDMA", tag)) {
            sb.append("--");
            sb.append("\n");
            asu = (arfcn + 113) / 2;
        } else {
            if (arfcn < 0 || arfcn > 99999) {
                sb.append("--");
            } else {
                sb.append(arfcn);

            }
            asu = SignalOperatUtils.getAsu(signLength);

        }
        sb.append("\n");
        sb.append("asu:" + asu);

        sb.append("，[接收功率]");


        if (tag.equals("GSM")) {//2G
            sb.append("RXL:");
            sb.append(-113 + 2 * asu);
        } else if (tag.equals("WCDMA") || tag.equals("CDMA")) {//3G
            sb.append("RSCP:");
            sb.append(asu - 116);
        } else {
            sb.append("RSRP:");
            sb.append(asu - 140);
        }
        if (lteCell != null) {
            sb.append(",[信噪比]SINR:");
            int sinr = +getLteFeild("getRssnr", lteCell);
            if (sinr < -10 || sinr > 30) {
                sb.append("--");
            } else {
                sb.append(sinr);
            }
            int rsrq = getLteFeild("getRsrq", lteCell);
            if (rsrq < -30 || rsrq > 0) {
                sb.append(",RSRQ:--");
            } else {
                sb.append(",RSRQ:" + rsrq);
            }

            sb.append(",eNB:" + ciLong / 256);

        }

        if (TextUtils.equals("WCDMA", tag)) {
            sb.append(",RNC:" + ciLong / 65536);
        }

        tv.setText(tag + ":" + sb.toString());
    }

    public int getLteFeild(String feildName, CellSignalStrengthLte lte) {
        try {
            Method getMethod = CellSignalStrengthLte.class.getDeclaredMethod(feildName);
            getMethod.setAccessible(true);
            Object resoult = getMethod.invoke(lte);
            return Integer.parseInt("" + resoult);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return -1;
    }

}
