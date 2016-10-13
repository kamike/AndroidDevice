package com.wangtao.androiddevice;

import android.telephony.CellLocation;
import android.telephony.NeighboringCellInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;

import com.wangtao.universallylibs.BaseActivity;

import java.util.List;

import static android.bluetooth.BluetoothClass.Service.TELEPHONY;


public class TestActivity extends BaseActivity {


    private TelephonyManager tel;
    private SubscriptionManager subManmger;

    @Override
    public void initShowLayout() {
        setContentView(R.layout.activity_test);
        this.tel = ((TelephonyManager) getSystemService("phone"));
        this.subManmger = ((SubscriptionManager) getSystemService(TELEPHONY_SUBSCRIPTION_SERVICE));
        doLogMsg("getNetworkOperatorName:"+this.tel.getNetworkOperatorName());
        CellLocation cell = this.tel.getCellLocation();
        doLogMsg(cell==null?"null--":cell.toString());
        List<NeighboringCellInfo> listCell = tel.getNeighboringCellInfo();
        for(NeighboringCellInfo info:listCell){
            doLogMsg(info.getLac()+","+info.getCid()+","+info.getRssi()+","+info.getNetworkType());

        }
        doLogMsg("getSimSerialNumber:"+tel.getSimSerialNumber());
        doLogMsg("number:"+tel.getLine1Number());

    }

    @Override
    public void setAllData() {

    }
}
