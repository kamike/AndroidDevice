package com.wangtao.androiddevice.ui;

import android.content.Context;
import android.telephony.CellInfo;
import android.telephony.CellInfoCdma;
import android.telephony.CellInfoGsm;
import android.telephony.CellInfoLte;
import android.telephony.CellInfoWcdma;
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
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.view_item_cell_list, null);
            tv = (TextView) convertView.findViewById(R.id.cell_info_list_tv);
        }
        CellInfo cellInfo = list.get(position);
        if (cellInfo instanceof CellInfoLte) {
            String lacMnc = ((CellInfoLte) cellInfo).getCellIdentity().toString();
            String signnLength = (((CellInfoLte) cellInfo).getCellSignalStrength().toString());
            showCellTxt(lacMnc, signnLength, "LTE", tv);
        }
        //获取所有的cdma网络信息
        if (cellInfo instanceof CellInfoCdma) {
            String lacMnc = ((CellInfoCdma) cellInfo).getCellIdentity().toString();
            String signnLength = (((CellInfoCdma) cellInfo).getCellSignalStrength().toString());
            showCellTxt(lacMnc, signnLength, "CDMA", tv);
        }
        if (cellInfo instanceof CellInfoGsm) {
            String lacMnc = ((CellInfoGsm) cellInfo).getCellIdentity().toString();
            String signnLength = (((CellInfoGsm) cellInfo).getCellSignalStrength().toString());
            showCellTxt(lacMnc, signnLength, "GSM", tv);
        }
        if (cellInfo instanceof CellInfoWcdma) {
            String lacMnc = ((CellInfoWcdma) cellInfo).getCellIdentity().toString();
            String signnLength = (((CellInfoWcdma) cellInfo).getCellSignalStrength().toString());
            showCellTxt(lacMnc, signnLength, "WCDMA", tv);
        }

        return convertView;
    }

    private void showCellTxt(String lacMnc, String signLength, String tag, TextView tv) {
        StringBuffer sb = new StringBuffer();
        if (tag.equals("WCDMA")) {
            sb.append("MCC:").append(SignalOperatUtils.getCellinfoFeil(lacMnc, 0));
            sb.append(",MNC:").append(SignalOperatUtils.getCellinfoFeil(lacMnc, 1));
        }
        sb.append(",LAC:").append(SignalOperatUtils.getCellinfoFeil(lacMnc, 2));
        sb.append(",CID:").append(SignalOperatUtils.getCellinfoFeil(lacMnc, 3));
        sb.append(",PSC:").append(SignalOperatUtils.getCellinfoFeil(lacMnc, 4));
        sb.append("\n");
        sb.append("信号强度:" + SignalOperatUtils.getSiglength(signLength));
        tv.setText(tag + ":" + sb.toString());
    }
}
