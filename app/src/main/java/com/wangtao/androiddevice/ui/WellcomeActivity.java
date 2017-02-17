package com.wangtao.androiddevice.ui;

import android.Manifest;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.telephony.CellInfoCdma;

import com.wangtao.androiddevice.R;
import com.wangtao.universallylibs.BaseActivity;

public class WellcomeActivity extends BaseActivity {

    @Override
    public void initShowLayout() {
        setContentView(R.layout.activity_wellcome);

    }

    @Override
    public void setAllData() {
//        doSetStatusBars();
        Handler h = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                doStartOter(MainActivity.class);
                finish();
            }
        };
        h.sendEmptyMessageDelayed(0, 2000);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.ACCESS_COARSE_LOCATION},
                1);
        CellInfoCdma c=null;

    }
}
