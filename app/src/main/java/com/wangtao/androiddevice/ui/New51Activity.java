package com.wangtao.androiddevice.ui;

import android.database.Cursor;
import android.net.Uri;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.LinearLayout;

import com.wangtao.androiddevice.R;
import com.wangtao.androiddevice.utils.SignMath;
import com.wangtao.universallylibs.BaseActivity;

public class New51Activity extends BaseActivity {
    private LinearLayout linearScroll;
    private TelephonyManager tm;

    @Override
    public void initShowLayout() {
        setContentView(R.layout.activity_new51);
        linearScroll = (LinearLayout) findViewById(R.id.new_51scroll_linear);
        doSetStatusBars();
    }

    @Override
    public void setAllData() {
        tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        linearScroll.addView(addShowTxtContent("====第" + 1, "张卡数据========="));
        linearScroll.addView(addShowTxtContent("网络类型：", SignMath.getCurrentNetworkType234G(mContext, tm.getNetworkType())));
        getStaticPhoneInfo(1);
        linearScroll.addView(addShowTxtContent("====第" + 2, "张卡数据========="));
        getStaticPhoneInfo(2);
//        View view = new View(mContext);
//        ViewGroup.LayoutParams par = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) (64 * screenDensity));
//        view.setLayoutParams(par);
//        linearScroll.addView(view);


    }

    private void getStaticPhoneInfo(int index) {
        Uri uri = Uri.parse("content://telephony/siminfo");
        Cursor cur = getContentResolver().query(uri, null, null, null, null);
        if (cur == null || cur.getCount() == 0) {
            doLogMsg("cursor:" + cur);
            linearScroll.addView(addShowTxtContent("没有双卡信息", ""));
            return;
        }
        doLogMsg("getColumnCount：" + cur.getColumnCount());
        doLogMsg("getCount：" + cur.getCount());

//            int id = cur.getInt(cur.getColumnIndex("_id"));
        if (index == 1) {
            cur.moveToNext();
//                linearScroll.addView(addShowTxtContent("_id:", cur.getString(cur.getColumnIndex("_id"))));
            linearScroll.addView(addShowTxtContent("iccid：", cur.getString(cur.getColumnIndex("icc_id"))));
            linearScroll.addView(addShowTxtContent("卡槽编号：", cur.getString(cur.getColumnIndex("sim_id"))));
            linearScroll.addView(addShowTxtContent("运营商：", cur.getString(cur.getColumnIndex("display_name"))));
            linearScroll.addView(addShowTxtContent("取运营商来源(0系统,1自定义)：", cur.getString(cur.getColumnIndex("name_source"))));
            linearScroll.addView(addShowTxtContent("手机号码：", cur.getString(cur.getColumnIndex("number"))));
            linearScroll.addView(addShowTxtContent("数据漫游：", cur.getString(cur.getColumnIndex("data_roaming"))));
            linearScroll.addView(addShowTxtContent("移动国家码(mcc)：", cur.getString(cur.getColumnIndex("mcc"))));
            linearScroll.addView(addShowTxtContent("移动网络代码(mnc)：", cur.getString(cur.getColumnIndex("mnc"))));
        }
        if (index == 2) {
            cur.moveToNext();
            cur.moveToNext();
//                linearScroll.addView(addShowTxtContent("_id:", cur.getString(cur.getColumnIndex("_id"))));
            linearScroll.addView(addShowTxtContent("iccid：", cur.getString(cur.getColumnIndex("icc_id"))));
            linearScroll.addView(addShowTxtContent("卡槽编号：", cur.getString(cur.getColumnIndex("sim_id"))));
            linearScroll.addView(addShowTxtContent("运营商：", cur.getString(cur.getColumnIndex("display_name"))));
            linearScroll.addView(addShowTxtContent("取运营商来源(0系统,1自定义)：", cur.getString(cur.getColumnIndex("name_source"))));
            linearScroll.addView(addShowTxtContent("手机号码：", cur.getString(cur.getColumnIndex("number"))));
            linearScroll.addView(addShowTxtContent("数据漫游：", cur.getString(cur.getColumnIndex("data_roaming"))));
            linearScroll.addView(addShowTxtContent("移动国家码(mcc)：", cur.getString(cur.getColumnIndex("mcc"))));
            linearScroll.addView(addShowTxtContent("移动网络代码(mnc)：", cur.getString(cur.getColumnIndex("mnc"))));
        }
        cur.close();

    }



    public void onclickTestRefresh(View view) {
        doStartOter(CellinfoRefershActivity.class);
    }




}
