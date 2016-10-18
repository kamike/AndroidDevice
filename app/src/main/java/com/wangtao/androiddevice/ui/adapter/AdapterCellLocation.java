package com.wangtao.androiddevice.ui.adapter;

import android.content.Context;
import android.telephony.CellLocation;
import android.telephony.cdma.CdmaCellLocation;
import android.telephony.gsm.GsmCellLocation;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/10/18.
 */

public class AdapterCellLocation extends BaseAdapter {
    private final Context context;
    private final ArrayList<CellLocation> cellInfo;

    public AdapterCellLocation(Context mContext, ArrayList<CellLocation> location) {
        this.context = mContext;
        this.cellInfo = location;
    }

    @Override
    public int getCount() {
        return cellInfo.size();
    }

    @Override
    public Object getItem(int position) {
        return cellInfo.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView tv = new TextView(context);
        CellLocation info = cellInfo.get(position);

        if (info instanceof GsmCellLocation) {
            GsmCellLocation gsm = (GsmCellLocation) info;
            tv.setText("LAC:" + gsm.getLac() + ",CID:" + gsm.getCid() + ",PSC:" + gsm.getPsc());
        } else if (info instanceof CdmaCellLocation) {
            CdmaCellLocation cdma = (CdmaCellLocation) info;
            tv.setText("BaseStationId:" + cdma.getBaseStationId() + ",SystemId:" + cdma.getSystemId() + ",Lat:" + cdma.getBaseStationLatitude() + ",Lng:" +
                    cdma.getBaseStationLongitude());
        }
        return tv;
    }
}
