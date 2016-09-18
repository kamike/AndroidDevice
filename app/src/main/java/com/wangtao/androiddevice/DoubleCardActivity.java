package com.wangtao.androiddevice;

import android.widget.LinearLayout;

import com.wangtao.androiddevice.utils.DoubleCardBean;
import com.wangtao.androiddevice.utils.GaotongDoubleInfo;
import com.wangtao.androiddevice.utils.MtkDoubleInfo;
import com.wangtao.universallylibs.BaseActivity;

public class DoubleCardActivity extends BaseActivity {
    private LinearLayout linearScroll;


    @Override
    public void initShowLayout() {
        setContentView(R.layout.activity_double_card);
        linearScroll = (LinearLayout) findViewById(R.id.double_card_scroll_linear);
    }

    @Override
    public void setAllData() {
        Object obj = DoubleCardBean.isDoubleSim(mContext);
        if (obj == null) {
            doLogMsg("空的双卡信息");
            linearScroll.addView(addShowTxtContent("双卡信息：", "空的双卡信息"));
            return;
        }
        if (obj instanceof GaotongDoubleInfo) {
            GaotongDoubleInfo gaotong = (GaotongDoubleInfo) obj;
            doLogMsg("高通：" + gaotong);
            linearScroll.addView(addShowTxtContent("双卡信息：", "高通：" + gaotong.toString()));
        }
        if (obj instanceof MtkDoubleInfo) {
            MtkDoubleInfo info = (MtkDoubleInfo) obj;
            doLogMsg("联发科：" + info.toString());
            linearScroll.addView(addShowTxtContent("双卡信息：", "联发科：" + info.toString()));
        }

    }

}
