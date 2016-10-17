package com.wangtao.androiddevice.ui;

import android.annotation.TargetApi;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.telephony.CellLocation;
import android.telephony.NeighboringCellInfo;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.wangtao.androiddevice.R;
import com.wangtao.androiddevice.utils.NetworkInfotion;
import com.wangtao.androiddevice.utils.ReflectUtils;
import com.wangtao.androiddevice.utils.SignMath;
import com.wangtao.androiddevice.utils.SignalOperatUtils;
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

    @Override
    public void setAllData() {
        tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        linearScroll.addView(addShowTxtContent("====第" + 1, "张卡数据========="));
        getStaticPhoneInfo(1);
        getDynamic(1);
        linearScroll.addView(addShowTxtContent("====第" + 2, "张卡数据========="));
        getStaticPhoneInfo(2);
        getOtherParams(twoSubId, 2);
        View view = new View(mContext);
        ViewGroup.LayoutParams par = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) (64 * screenDensity));
        view.setLayoutParams(par);
        linearScroll.addView(view);

    }

    private int twoSubId;

    private void getDynamic(int indexSim) {
        SubscriptionManager mSubscriptionManager = (SubscriptionManager) getSystemService(Context.TELEPHONY_SUBSCRIPTION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            List<SubscriptionInfo> dataList = mSubscriptionManager
                    .getActiveSubscriptionInfoList();
            if (dataList == null) {
                linearScroll.addView(addShowTxtContent("手机的没有任何双卡信息", ""));
                return;
            }
            doLogMsg("'有多少张卡的数据：" + dataList.size());
            if (dataList.size() >= 1) {
                getOtherParams(dataList.get(0).getSubscriptionId(), 1);
            }
            if (dataList.size() >= 2) {
                twoSubId = dataList.get(1).getSubscriptionId();
            }
        }
        doLogMsg("getNetworkType:" + tm.getNetworkType());
        linearScroll.addView(addShowTxtContent("网络类型：", SignMath.getCurrentNetworkType234G(mContext, tm.getNetworkType())));
        List<NeighboringCellInfo> cell = tm.getNeighboringCellInfo();

        linearScroll.addView(addShowTxtContent("基站信息", ""));
        try {
            CellLocation cel = tm.getCellLocation();
            GsmCellLocation gsm = (GsmCellLocation) cel;
            if (gsm != null) {
                linearScroll.addView(addShowTxtContent("  LAC：", gsm.getLac() + ",CID:" + gsm.getCid() + "，主扰码(PSC):" + gsm.getPsc()));
            }
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
        if (cell == null) {
            return;
        }
        for (NeighboringCellInfo info : cell) {
            doLogMsg("NeighboringCellInfo:" + info.getNetworkType());
            if (info.getRssi() == 192 && info.getLac() == -1 && info.getCid() == -1 && info.getPsc() == -1) {
                continue;
            }

            int rssi = info.getRssi();
            if ("2G".equals(SignMath.getCurrentNetworkType234G(mContext, tm.getNetworkType()))) {
                rssi = -113 + 2 * rssi;
            } else {
                rssi = -140 + rssi;
            }
            linearScroll.addView(addShowTxtContent("    ", "LAC:" + info.getLac() + ",CID:" +
                    info.getCid() + "\n信号强度:" + info.getRssi() + "=" + rssi + "dbm,主扰码(PSC):" + info.getPsc()));
        }

    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP_MR1)

    private void getOtherParams(int subId, int index) {
        int event = PhoneStateListener.LISTEN_CALL_FORWARDING_INDICATOR |
                PhoneStateListener.LISTEN_CALL_STATE |
                PhoneStateListener.LISTEN_CELL_LOCATION |
                PhoneStateListener.LISTEN_DATA_ACTIVITY |
                PhoneStateListener.LISTEN_DATA_CONNECTION_STATE |
                PhoneStateListener.LISTEN_MESSAGE_WAITING_INDICATOR |
                PhoneStateListener.LISTEN_SERVICE_STATE |
                PhoneStateListener.LISTEN_SIGNAL_STRENGTH | PhoneStateListener.LISTEN_SIGNAL_STRENGTHS;


        tm.listen(new MyPhotoListener(subId, index), event);

        linearScroll.addView(addShowTxtContent(index + "卡信号质量：", ""));
        linearScroll.addView(addShowTxtContent(index + "卡基站信息：", ""));
        linearScroll.addView(addShowTxtContent(index + "卡RSRP：", ""));
        linearScroll.addView(addShowTxtContent(index + "卡RSRQ：", ""));
//        linearScroll.addView(addShowTxtContent(index + "信号质量(db1)：", ""));
//        linearScroll.addView(addShowTxtContent(index + "isGsm：", ""));

    }

    public void onclickTestRefresh(View view) {
        linearScroll.removeAllViews();
        setAllData();
        doShowToast("刷新数据");
    }

    private class MyPhotoListener extends PhoneStateListener {

        private final int index;

        public MyPhotoListener(int subId, int index) {
            this.index = index;
            ReflectUtils.setFieldValue(this, "mSubId", subId);
        }

        /**
         * 返回手机当前所处的位置
         *
         * @param location
         */
        @Override
        public void onCellLocationChanged(CellLocation location) {
            super.onCellLocationChanged(location);
            doLogMsg("onCellLocationChanged:" + location);
            try {
                CellLocation cel = tm.getCellLocation();
                GsmCellLocation info = (GsmCellLocation) cel;

                if (info != null) {
                    updataShowTxtContent(linearScroll, index + "卡基站信息：", "LAC:" + info.getLac() + ",CID:" +
                            info.getCid() + "主扰码(PSC):" + info.getPsc());
                    doLogMsg("cid:" + info.getCid());
                    doLogMsg("psc:" + info.getPsc());
                    doLogMsg("lac:" + info.getLac());
                }
            } catch (ClassCastException e) {
                e.printStackTrace();
            }

        }

        @Override
        public void onSignalStrengthsChanged(SignalStrength signalStrength) {
            super.onSignalStrengthsChanged(signalStrength);
            doLogMsg("onSignalStrengthsChanged:" + signalStrength);
            updataShowTxtContent(linearScroll, index + "卡RSRP：", SignalOperatUtils.getAllParams(signalStrength.toString(), 8));
            updataShowTxtContent(linearScroll, index + "卡RSRQ：", SignalOperatUtils.getAllParams(signalStrength.toString(), 9));
            if (signalStrength.isGsm()) {
                updataShowTxtContent(linearScroll, index + "卡信号质量：", signalStrength.getGsmSignalStrength()
                        + "=" + (-113 + 2 * signalStrength.getGsmSignalStrength()) + "dbm");
            } else {
                if (NetworkInfotion.getProvidersInt(tm) <= 2) {
                    updataShowTxtContent(linearScroll, index + "卡信号质量：", signalStrength.getCdmaDbm() + "");
                } else {
                    updataShowTxtContent(linearScroll, index + "卡信号质量：", signalStrength.getEvdoDbm() + "");
                }

            }

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


}
