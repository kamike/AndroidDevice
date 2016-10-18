package com.wangtao.androiddevice.ui;

import android.os.Bundle;
import android.telephony.CellInfo;
import android.telephony.CellInfoCdma;
import android.telephony.CellInfoGsm;
import android.telephony.CellInfoLte;
import android.telephony.CellInfoWcdma;
import android.telephony.CellLocation;
import android.telephony.TelephonyManager;
import android.telephony.cdma.CdmaCellLocation;
import android.telephony.gsm.GsmCellLocation;

import com.wangtao.androiddevice.R;
import com.wangtao.universallylibs.BaseActivity;

import java.util.List;

public class TempTestActivity extends BaseActivity {

    private TelephonyManager telephonymanager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initShowLayout() {
        setContentView(R.layout.activity_temp_test);

    }

    @Override
    public void setAllData() {
        telephonymanager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        StringBuilder str = new StringBuilder();
        //获取小区信息
        List<CellInfo> cellInfoList = telephonymanager.getAllCellInfo();
        str.append("小区信息:" + "\n");
        int index = 0;
        for (CellInfo cellInfo : cellInfoList) {
            //获取所有Lte网络信息
            if (cellInfo instanceof CellInfoLte) {
                str.append("[" + index + "]==CellInfoLte" + "\n");
                if (cellInfo.isRegistered()) {
                    str.append("isRegistered=YES" + "\n");
                }
                str.append("TimeStamp:" + cellInfo.getTimeStamp() + "\n");
                str.append(((CellInfoLte) cellInfo).getCellIdentity().toString() + "\n");
                str.append(((CellInfoLte) cellInfo).getCellSignalStrength().toString() + "\n");

            }
            //获取所有的cdma网络信息
            if (cellInfo instanceof CellInfoCdma) {
                str.append("[" + index + "]==CellInfoCdma" + "\n");
                if (cellInfo.isRegistered()) {
                    str.append("isRegistered=YES" + "\n");
                }
                str.append("TimeStamp:" + cellInfo.getTimeStamp() + "\n");
                str.append(((CellInfoCdma) cellInfo).getCellIdentity().toString() + "\n");
                str.append(((CellInfoCdma) cellInfo).getCellSignalStrength().toString() + "\n");
            }
            //获取所有的Gsm网络
            if (cellInfo instanceof CellInfoGsm) {
                str.append("[" + index + "]==CellInfoGsm" + "\n");
                if (cellInfo.isRegistered()) {
                    str.append("isRegistered=YES" + "\n");
                }
                str.append("TimeStamp:" + cellInfo.getTimeStamp() + "\n");
                str.append(((CellInfoGsm) cellInfo).getCellIdentity().toString() + "\n");
                str.append(((CellInfoGsm) cellInfo).getCellSignalStrength().toString() + "\n");
            }
            //获取所有的Wcdma网络
            if (cellInfo instanceof CellInfoWcdma) {
                str.append("[" + index + "]==CellInfoWcdma" + "\n");
                if (cellInfo.isRegistered()) {
                    str.append("isRegistered=YES" + "\n");
                }
                str.append("TimeStamp:" + cellInfo.getTimeStamp() + "\n");
                str.append(((CellInfoWcdma) cellInfo).getCellIdentity().toString() + "\n");
                str.append(((CellInfoWcdma) cellInfo).getCellSignalStrength().toString() + "\n");
            }
            index++;
        }

        CellLocation location = telephonymanager.getCellLocation();
        if (location != null && location instanceof GsmCellLocation) {
            GsmCellLocation l1 = (GsmCellLocation) location;
            str.append("使用网络:" + "Gsm" + "\n");
            str.append("cid" + l1.getCid() + "\n");
            str.append("lac" + l1.getLac() + "\n");
            str.append("Psc" + l1.getPsc() + "\n");
        } else if (location != null && location instanceof CdmaCellLocation) {
            CdmaCellLocation l2 = (CdmaCellLocation) location;
            str.append(l2.toString() + "\n");
        }
        doLogMsg("基站信息："+str.toString());
    }
}
