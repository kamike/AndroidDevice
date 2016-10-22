package com.wangtao.androiddevice.ui;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.wangtao.androiddevice.R;
import com.wangtao.androiddevice.utils.SignMath;
import com.wangtao.universallylibs.BaseActivity;

import java.util.List;

public class New51Activity extends BaseActivity {
    private LinearLayout linearScroll;
    private TelephonyManager tm;

    @Override
    public void initShowLayout() {
        setContentView(R.layout.activity_new51);
        linearScroll = (LinearLayout) findViewById(R.id.new_51scroll_linear);
        doSetStatusBars();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
    @Override
    public void setAllData() {
        tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        SubscriptionManager mSubscriptionManager = (SubscriptionManager) getSystemService(Context.TELEPHONY_SUBSCRIPTION_SERVICE);
        List<SubscriptionInfo> dataList = mSubscriptionManager
                .getActiveSubscriptionInfoList();

        if (dataList == null) {
            doShowMesage("手机的没有任何双卡信息");
            return;
        }
        initManager(dataList, 0);
        initManager(dataList, 1);
//        View view = new View(mContext);
//        ViewGroup.LayoutParams par = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) (64 * screenDensity));
//        view.setLayoutParams(par);
//        linearScroll.addView(view);


    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
    private void initManager(List<SubscriptionInfo> list, int index) {
        doLogMsg("'有多少张卡的数据：" + list.size());

        for (int i = 0; i < list.size(); i++) {
            if (index >= list.size()) {
                return;
            }
            SubscriptionInfo info = list.get(index);
            doLogMsg(info.toString());
            ImageView iv = new ImageView(mContext);
            iv.setImageBitmap(info.createIconBitmap(mContext));
            linearScroll.addView(iv);
            if (index == 0) {
                linearScroll.addView(addShowTxtContent("当前使用的网络类型：", SignMath.getCurrentNetworkType234G(mContext, tm.getNetworkType())));
            }
            linearScroll.addView(addShowTxtContent("iccid：", info.getIccId()));
            linearScroll.addView(addShowTxtContent("卡槽编号：", info.getSimSlotIndex() + ""));
            linearScroll.addView(addShowTxtContent("运营商：", info.getCarrierName() + ""));
            linearScroll.addView(addShowTxtContent("显示名字：", info.getDisplayName() + ""));
            linearScroll.addView(addShowTxtContent("手机号码：", info.getNumber()));
            linearScroll.addView(addShowTxtContent("数据漫游：", info.getDataRoaming() + ""));
            linearScroll.addView(addShowTxtContent("移动国家码(mcc)：", info.getMcc() + ""));
            linearScroll.addView(addShowTxtContent("国家ISO码：", info.getCountryIso()));
            linearScroll.addView(addShowTxtContent("移动网络码(mnc)：", info.getMnc() + ""));
            return;
        }
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
            //1卡数据
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
            //2卡数据
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
