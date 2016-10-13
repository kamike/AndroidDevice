package com.wangtao.androiddevice;

import android.annotation.TargetApi;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.telephony.NeighboringCellInfo;
import android.telephony.PhoneStateListener;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.widget.LinearLayout;

import com.wangtao.androiddevice.utils.ReflectUtils;
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
    }

    @Override
    public void setAllData() {
        tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        linearScroll.addView(addShowTxtContent("====第" + 1, "张卡的静态数据========="));
        testStatic51(1);
        SubscriptionManager mSubscriptionManager = (SubscriptionManager) getSystemService(Context.TELEPHONY_SUBSCRIPTION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP_MR1) {
            List<SubscriptionInfo> dataList = mSubscriptionManager
                    .getActiveSubscriptionInfoList();
            if (dataList == null) {
                linearScroll.addView(addShowTxtContent("手机的没有任何双卡信息", ""));
                return;
            }
            doLogMsg("'有多少张卡的数据：" + dataList.size());
            int index = 0;
            for (SubscriptionInfo info : dataList) {
                index++;
//                getOtherParams(info, index);
            }
        }
        doLogMsg("getNetworkType:" + tm.getNetworkType());
        linearScroll.addView(addShowTxtContent("网络类型：", SignMath.getCurrentNetworkType234G(mContext, tm.getNetworkType())));
        List<NeighboringCellInfo> cell = tm.getNeighboringCellInfo();

        linearScroll.addView(addShowTxtContent("基站信息", ""));
        for (NeighboringCellInfo info : cell) {
            doLogMsg("NeighboringCellInfo:" + info.getNetworkType());
            int rssi = info.getRssi();
            if ("2G".equals(SignMath.getCurrentNetworkType234G(mContext, tm.getNetworkType()))) {
                rssi = -113 + 2 * rssi;
            }
            linearScroll.addView(addShowTxtContent("    ", "LAC:" + info.getLac() + ",CID:" +
                    info.getCid() + "\n信号强度:" + rssi + "dbm,主扰码(PSC):" + info.getPsc()));
        }


    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP_MR1)

    private void getOtherParams(SubscriptionInfo subinfo, int index) {
        linearScroll.addView(addShowTxtContent("====第" + index, "张卡的动态数据========="));
        int event = PhoneStateListener.LISTEN_CALL_FORWARDING_INDICATOR |
                PhoneStateListener.LISTEN_CALL_STATE |
                PhoneStateListener.LISTEN_CELL_LOCATION |
                PhoneStateListener.LISTEN_DATA_ACTIVITY |
                PhoneStateListener.LISTEN_DATA_CONNECTION_STATE |
                PhoneStateListener.LISTEN_MESSAGE_WAITING_INDICATOR |
                PhoneStateListener.LISTEN_SERVICE_STATE |
                PhoneStateListener.LISTEN_SIGNAL_STRENGTH | PhoneStateListener.LISTEN_SIGNAL_STRENGTHS;


        tm.listen(new MyPhotoListener(subinfo.getSubscriptionId(), index), event);

//        linearScroll.addView(addShowTxtContent(index + "信号强度(dbm1)：", ""));
//        linearScroll.addView(addShowTxtContent(index + "信号质量(db1)：", ""));
//        linearScroll.addView(addShowTxtContent(index + "isGsm：", ""));

    }

    private class MyPhotoListener extends PhoneStateListener {

        private final int index;

        public MyPhotoListener(int subId, int index) {
            this.index = index;
            ReflectUtils.setFieldValue(this, "mSubId", subId);
        }
    }

    private void testStatic51(int index) {
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
        while (cur.moveToNext()) {
            if (index == 1) {
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
            break;
        }
        cur.close();

    }


}
