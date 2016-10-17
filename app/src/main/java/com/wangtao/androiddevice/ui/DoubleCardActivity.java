package com.wangtao.androiddevice.ui;

import android.annotation.TargetApi;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.telephony.CellInfo;
import android.telephony.CellLocation;
import android.telephony.PhoneStateListener;
import android.telephony.ServiceState;
import android.telephony.SignalStrength;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;
import android.widget.LinearLayout;

import com.wangtao.androiddevice.R;
import com.wangtao.androiddevice.utils.DoubleCardBean;
import com.wangtao.androiddevice.utils.DoubleNetworkUtils;
import com.wangtao.androiddevice.utils.GaotongDoubleInfo;
import com.wangtao.androiddevice.utils.MtkDoubleInfo;
import com.wangtao.androiddevice.utils.NetworkInfotion;
import com.wangtao.androiddevice.utils.ReflectUtils;
import com.wangtao.universallylibs.BaseActivity;

import java.util.List;

public class DoubleCardActivity extends BaseActivity {
    private LinearLayout linearScroll;
    private Object mOtherParams;


    @Override
    public void initShowLayout() {
        setContentView(R.layout.activity_double_card);
        linearScroll = (LinearLayout) findViewById(R.id.double_card_scroll_linear);
    }

    private TelephonyManager tm;

    @Override
    public void setAllData() {
        tm = (TelephonyManager) this.getSystemService(TELEPHONY_SERVICE);
//        test50();
//        getInfo4();
        test51DoubleCard();


    }

    private void testClassInfo() {
        String className = "com.mediatek.telephony";
        String classManager = "com.mediatek.telephony.TelephonyManagerEx";
        try {
            Class<?> c = Class.forName(className);
            doLogMsg("name:" + c.getName());
            Class<?> m = Class.forName(classManager);
            doLogMsg("name:" + m.getName());

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            doLogMsg("异常信息：" + e.getMessage());
        }
    }

    private void test51DoubleCard() {
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
                getOtherParams(info, index);
            }
        } else {
            linearScroll.addView(addShowTxtContent("手机的sdk版本小于API22(5.0)以下", ""));
//            getInfo4();
            testClassInfo();
        }


    }

    private void getInfo4() {
        linearScroll.addView(addShowTxtContent("====小于5.0获取方式", "========="));
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

    private void test50() {
        Uri uri = Uri.parse("content://telephony/siminfo");
        Cursor cur = getContentResolver().query(uri, null, null, null, null);
        if (cur == null || cur.getCount() == 0) {
            doLogMsg("cursor:" + cur);
            linearScroll.addView(addShowTxtContent("没有双卡信息", ""));
            return;
        }
        doLogMsg("getColumnCount：" + cur.getColumnCount());
        doLogMsg("getCount：" + cur.getCount());

        int index = 0;
        doLogMsg("getColumnName:" + cur.getColumnName(index));
        while (cur.moveToNext()) {
            index++;
            int id = cur.getInt(cur.getColumnIndex("_id"));


            linearScroll.addView(addShowTxtContent("_id:", cur.getString(cur.getColumnIndex("_id"))));
            linearScroll.addView(addShowTxtContent("icc_id:", cur.getString(cur.getColumnIndex("icc_id"))));
            linearScroll.addView(addShowTxtContent("sim_id:", cur.getString(cur.getColumnIndex("sim_id"))));
            linearScroll.addView(addShowTxtContent("display_name:", cur.getString(cur.getColumnIndex("display_name"))));
            linearScroll.addView(addShowTxtContent("name_source:", cur.getString(cur.getColumnIndex("name_source"))));
            linearScroll.addView(addShowTxtContent("color:", cur.getString(cur.getColumnIndex("color"))));
            linearScroll.addView(addShowTxtContent("number:", cur.getString(cur.getColumnIndex("number"))));
            linearScroll.addView(addShowTxtContent("data_roaming:", cur.getString(cur.getColumnIndex("data_roaming"))));
            linearScroll.addView(addShowTxtContent("mcc:", cur.getString(cur.getColumnIndex("mcc"))));
            linearScroll.addView(addShowTxtContent("mnc:", cur.getString(cur.getColumnIndex("mnc"))));
        }

        doLogMsg("循环了多少次：" + index);
        cur.close();

    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP_MR1)
    public void getOtherParams(SubscriptionInfo subinfo, int index) {
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
        doLogMsg("listener--：" + subinfo.getSubscriptionId());
        linearScroll.addView(addShowTxtContent(index + "onMessageWaitingIndicatorChanged：", ""));
        linearScroll.addView(addShowTxtContent(index + "DataConnectionStateChanged：", ""));
        linearScroll.addView(addShowTxtContent(index + "ServiceStateChanged：", ""));
        linearScroll.addView(addShowTxtContent(index + "onSignalStrengthsChanged：", ""));

        linearScroll.addView(addShowTxtContent(index + "信号强度(dbm1)：", ""));
        linearScroll.addView(addShowTxtContent(index + "信号质量(db1)：", ""));
        linearScroll.addView(addShowTxtContent(index + "isGsm：", ""));

    }

    private class MyPhotoListener extends PhoneStateListener {

        private final int index;

        public MyPhotoListener(int subId, int index) {
            this.index = index;
            ReflectUtils.setFieldValue(this, "mSubId", subId);
        }

        @Override
        public void onCallForwardingIndicatorChanged(boolean cfi) {
            super.onCallForwardingIndicatorChanged(cfi);
            doLogMsg("onCallForwardingIndicatorChanged:" + cfi);

        }


        @Override
        public void onCellInfoChanged(List<CellInfo> cellInfo) {
            super.onCellInfoChanged(cellInfo);
            doLogMsg("onCellInfoChanged:" + cellInfo);

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
                GsmCellLocation gsm = (GsmCellLocation) cel;
                if (gsm != null) {
                    doLogMsg("cid:" + gsm.getCid());
                    doLogMsg("psc:" + gsm.getPsc());
                    doLogMsg("lac:" + gsm.getLac());
                }
            } catch (ClassCastException e) {
                e.printStackTrace();
            }

        }

        @Override
        public void onDataActivity(int direction) {
            super.onDataActivity(direction);
            doLogMsg("onDataActivity:" + direction);
        }

        /**
         * 数据连接状态改变可能导致网络类型的改变
         *
         * @param state
         */
        @Override
        public void onDataConnectionStateChanged(int state) {
            super.onDataConnectionStateChanged(state);
            doLogMsg("onDataConnectionStateChanged:" + state);


        }

        @Override
        public void onDataConnectionStateChanged(int state, int networkType) {
            super.onDataConnectionStateChanged(state, networkType);
            doLogMsg("onDataConnectionStateChanged:" + networkType);
            updataShowTxtContent(linearScroll, index + "DataConnectionStateChanged：", state + ",networkType:" + networkType);
        }

        @Override
        public void onMessageWaitingIndicatorChanged(boolean mwi) {
            super.onMessageWaitingIndicatorChanged(mwi);
            doLogMsg("onMessageWaitingIndicatorChanged:" + mwi);
            updataShowTxtContent(linearScroll, index + "onMessageWaitingIndicatorChanged:", "" + mwi);
        }

        @Override
        public void onServiceStateChanged(ServiceState serviceState) {
            super.onServiceStateChanged(serviceState);
            doLogMsg("onServiceStateChanged:" + serviceState);
            updataShowTxtContent(linearScroll, index + "ServiceStateChanged：", serviceState.toString());
        }


        @Override
        public void onSignalStrengthsChanged(SignalStrength signalStrength) {
            super.onSignalStrengthsChanged(signalStrength);
            doLogMsg("onSignalStrengthsChanged:" + signalStrength);
            if (signalStrength.isGsm()) {
                updataShowTxtContent(linearScroll, index + "信号强度(dbm1)：", -113 + (2 * signalStrength.getGsmSignalStrength()) + "");
            } else {
                if (NetworkInfotion.getProvidersInt(tm) <= 2) {
                    updataShowTxtContent(linearScroll, index + "信号强度(dbm1)：", signalStrength.getCdmaDbm() + "");
                } else {
                    updataShowTxtContent(linearScroll, index + "信号强度(dbm1)：", signalStrength.getEvdoDbm() + "");
                }

            }
            updataShowTxtContent(linearScroll, index + "信号质量(db1)：", signalStrength.getGsmSignalStrength() + "");
            updataShowTxtContent(linearScroll, index + "isGsm：", signalStrength.isGsm() + "");
            updataShowTxtContent(linearScroll, index + "onSignalStrengthsChanged：", signalStrength.toString());

        }
    }

    /**
     * 更新SIM卡状态和网络信息
     *
     * @param tm
     */
    private final void updateViews(TelephonyManager tm) {


        DoubleNetworkUtils.mapSimStateToName(tm.getSimState());


        DoubleNetworkUtils.mapNetworkTypeToName(tm.getNetworkType());


    }
}
