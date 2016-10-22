package com.wangtao.androiddevice.ui.adapter;

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
import com.wangtao.androiddevice.utils.SignMath;
import com.wangtao.androiddevice.utils.SignalOperatUtils;

import java.util.List;

/**
 * Created by AdapterCellinfoList on 2016/10/18.
 * QQ：751190264
 */

public class AdapterCellinfoList extends BaseAdapter {

    private final Context context;
    private final List<CellInfo> list;
    private final int networkType;
    private final String OperationName;

    public AdapterCellinfoList(Context context, List<CellInfo> cellInfo, int networkName, String operName) {
        this.context = context;
        this.list = cellInfo;
        this.networkType = networkName;
        this.OperationName = operName;
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
        getLte(tv, cellInfo);
        //获取所有的cdma网络信息
        getCdma(tv, cellInfo);
        getGsm(tv, cellInfo);
        getWcdma(tv, cellInfo);

        return convertView;
    }

    private void getLte(TextView tv, CellInfo cellInfo) {
        if (cellInfo instanceof CellInfoLte && SignMath.getNetworkClassByType(networkType) == SignMath.NETWORK_CLASS_4_G) {

            String lacMnc = ((CellInfoLte) cellInfo).getCellIdentity().toString();
            String signnLength = (((CellInfoLte) cellInfo).getCellSignalStrength().toString());
            showCellTxt(lacMnc, signnLength, "LTE", tv);
        }
    }

    private void getGsm(TextView tv, CellInfo cellInfo) {
        if (cellInfo instanceof CellInfoGsm) {
            if (SignMath.getNetworkClassByType(networkType) != SignMath.NETWORK_CLASS_2_G) {
                return;
            }
            String lacMnc = ((CellInfoGsm) cellInfo).getCellIdentity().toString();
            String signnLength = (((CellInfoGsm) cellInfo).getCellSignalStrength().toString());
            showCellTxt(lacMnc, signnLength, "GSM", tv);
        }
    }

    private void getWcdma(TextView tv, CellInfo cellInfo) {
        if (cellInfo instanceof CellInfoWcdma) {
            if (OperationName.equals("46003")) {
                return;
            }
            if (SignMath.getNetworkClassByType(networkType) != SignMath.NETWORK_CLASS_3_G) {
                return;
            }
            String lacMnc = ((CellInfoWcdma) cellInfo).getCellIdentity().toString();
            String signnLength = (((CellInfoWcdma) cellInfo).getCellSignalStrength().toString());
            showCellTxt(lacMnc, signnLength, "WCDMA", tv);
        }
    }

    private void getCdma(TextView tv, CellInfo cellInfo) {
        if (cellInfo instanceof CellInfoCdma) {
            if (OperationName.equals("46003")) {
                if (SignMath.getNetworkClassByType(networkType) == SignMath.NETWORK_CLASS_4_G) {
                    return;
                }
            } else {
                if (SignMath.getNetworkClassByType(networkType) != SignMath.NETWORK_CLASS_4_G) {
                    return;
                }
            }
            String lacMnc = ((CellInfoCdma) cellInfo).getCellIdentity().toString();
            String signnLength = (((CellInfoCdma) cellInfo).getCellSignalStrength().toString());
            showCellTxt(lacMnc, signnLength, "CDMA", tv);
        }
    }

    private void showCellTxt(String lacMnc, String signLength, String tag, TextView tv) {
        if (tv == null) {
            return;
        }
        StringBuffer sb = new StringBuffer();
        if (tag.equals("WCDMA")) {
            sb.append("MCC:").append(SignalOperatUtils.getCellinfoFeil(lacMnc, 0));
            sb.append(",MNC:").append(SignalOperatUtils.getCellinfoFeil(lacMnc, 1));
        }
        sb.append(",LAC:").append(SignalOperatUtils.getCellinfoFeil(lacMnc, 2));
        sb.append(",CID:").append(SignalOperatUtils.getCellinfoFeil(lacMnc, 3));
        sb.append(",PSC:").append(SignalOperatUtils.getCellinfoFeil(lacMnc, 4));
        sb.append("\n");
        sb.append("信号强度" + SignalOperatUtils.getSiglength(signLength));
        tv.setText(tag + ":" + sb.toString());
    }
}
