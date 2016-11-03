package com.wangtao.androiddevice.ui;

import android.content.Context;
import android.telephony.CellInfo;
import android.telephony.NeighboringCellInfo;
import android.telephony.TelephonyManager;
import android.view.View;

import com.wangtao.androiddevice.R;
import com.wangtao.universallylibs.BaseActivity;

import java.util.List;

public class TestActivity extends BaseActivity {


    @Override
    public void initShowLayout() {
        setContentView(R.layout.activity_test);
    }

    @Override
    public void setAllData() {

    }

    public void onclickSaveData(View view) {
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        List<NeighboringCellInfo> infos = telephonyManager
                .getNeighboringCellInfo();
        doLogMsg("NeighboringCellInfo：" + infos.size());
        for (NeighboringCellInfo info : infos) {
            doLogMsg(info.toString());
        }
        List<CellInfo> listCells = telephonyManager.getAllCellInfo();
        doLogMsg("===========getAllCellInfo===============");
        doLogMsg("多少天基站信息：" + listCells.size());
        for (CellInfo info : listCells) {
            doLogMsg(info.toString());
        }

    }
}
