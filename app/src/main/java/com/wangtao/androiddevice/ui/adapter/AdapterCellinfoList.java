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
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.wangtao.androiddevice.R;
import com.wangtao.androiddevice.utils.SignalOperatUtils;

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
        CellInfo cellInfo = list.get(position);
        if (cellInfo instanceof CellInfoLte) {
            String lacMnc = ((CellInfoLte) cellInfo).getCellIdentity().toString();
            String signnLength = (((CellInfoLte) cellInfo).getCellSignalStrength().toString());

            CellInfoLte lteInfo = (CellInfoLte) cellInfo;
            CellIdentityLte cellId = lteInfo.getCellIdentity();
            String crfcn = "--";
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                crfcn = "" + cellId.getEarfcn();
            }

            showCellTxt(lacMnc, signnLength, "LTE", tv, crfcn);

        }
        //获取所有的cdma网络信息
        if (cellInfo instanceof CellInfoCdma) {
            String lacMnc = ((CellInfoCdma) cellInfo).getCellIdentity().toString();
            String signnLength = (((CellInfoCdma) cellInfo).getCellSignalStrength().toString());

            showCellTxt(lacMnc, signnLength, "CDMA", tv, "--");
        }
        if (cellInfo instanceof CellInfoGsm) {
            String lacMnc = ((CellInfoGsm) cellInfo).getCellIdentity().toString();
            String signnLength = (((CellInfoGsm) cellInfo).getCellSignalStrength().toString());

            CellInfoGsm cdmaInfo = (CellInfoGsm) cellInfo;
            CellIdentityGsm cellId = cdmaInfo.getCellIdentity();
            String crfcn = "--";
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                crfcn = "" + cellId.getArfcn();
            }
            showCellTxt(lacMnc, signnLength, "GSM", tv, crfcn);
        }
        if (cellInfo instanceof CellInfoWcdma) {
            String lacMnc = ((CellInfoWcdma) cellInfo).getCellIdentity().toString();
            String signnLength = (((CellInfoWcdma) cellInfo).getCellSignalStrength().toString());

            CellInfoWcdma cdmaInfo = (CellInfoWcdma) cellInfo;
            CellIdentityWcdma cellId = cdmaInfo.getCellIdentity();
            String crfcn = "--";
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                crfcn = "" + cellId.getUarfcn();
            }
            showCellTxt(lacMnc, signnLength, "WCDMA", tv, crfcn);
        }

        return convertView;
    }

    private void showCellTxt(String lacMnc, String signLength, String tag, TextView tv, String arfcn) {
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
        int cid = SignalOperatUtils.getCellinfoFeilInt(lacMnc, 2);


        if (TextUtils.equals(tag, "GSM") || TextUtils.equals(tag, "CDMA")) {
            if (cid > 256 || cid < 0) {
                sb.append("--");
            } else {
                sb.append(cid);
            }

        } else {
            if (cid > 65535 || cid < 0) {
                sb.append("--");
            } else {
                sb.append(cid % 256);
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

        arfcn = TextUtils.equals("2147483647", arfcn) ? "--" : arfcn;
        sb.append(",ARFCN:").append(arfcn);
        sb.append("\n");
        sb.append("信号强度" + SignalOperatUtils.getSiglength(signLength));
        tv.setText(tag + ":" + sb.toString());
    }
}
