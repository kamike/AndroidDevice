package com.wangtao.androiddevice.ui;

import android.content.Context;
import android.os.Environment;
import android.telephony.CellInfo;
import android.telephony.NeighboringCellInfo;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;
import android.view.View;

import com.wangtao.androiddevice.R;
import com.wangtao.universallylibs.BaseActivity;
import com.wangtao.universallylibs.utils.LogSaveUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class TestActivity extends BaseActivity {

    LogSaveUtils save;

    @Override
    public void initShowLayout() {
        setContentView(R.layout.activity_test);
    }
    TelephonyManager telephonyManager;
    @Override
    public void setAllData() {
        telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

        if (android.os.Build.VERSION.SDK_INT > 20) {
            doShowMesage("系统版本大于5.0");
            return;
        }
    }

    public void onclickSaveData(View view) {

        if (android.os.Build.VERSION.SDK_INT > 20) {
            doShowMesage("系统版本大于5.0");
            return;
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH_mm_ss");
        String fileName = "test_4.2_" + dateFormat.format(new Date()) + "log";
        save = new LogSaveUtils(fileName);
        GsmCellLocation location = (GsmCellLocation) telephonyManager.getCellLocation();
        if(location!=null){
            save.writeString("getCellLocation："+location.toString());
        }


        List<NeighboringCellInfo> infos = telephonyManager
                .getNeighboringCellInfo();
        doLogMsg("NeighboringCellInfo：" + infos.size());
        save.writeString("NeighboringCellInfo：" + infos.size());
        for (NeighboringCellInfo info : infos) {
            doLogMsg(info.toString());
            save.writeString("info:" + info.toString() + "\n");
        }
        List<CellInfo> listCells = telephonyManager.getAllCellInfo();
        doLogMsg("===========getAllCellInfo===============");
        doLogMsg("多少天基站信息：" + listCells.size());
        save.writeString("多少基站信息：" + listCells.size() + "\n");
        for (CellInfo info : listCells) {
            doLogMsg(info.toString());
            save.writeString("info:" + info.toString() + "\n");
        }
        doShowMesage("保存日志到" + Environment.getExternalStorageDirectory().getAbsolutePath() + "/test/" + fileName);

    }
}
