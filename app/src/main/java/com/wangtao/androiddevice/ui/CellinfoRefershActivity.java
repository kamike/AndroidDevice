package com.wangtao.androiddevice.ui;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.telephony.CellInfo;
import android.telephony.CellLocation;
import android.telephony.PhoneStateListener;
import android.telephony.ServiceState;
import android.telephony.SignalStrength;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.widget.ListView;
import android.widget.TextView;

import com.wangtao.androiddevice.R;
import com.wangtao.androiddevice.ui.adapter.AdapterCellLocation;
import com.wangtao.androiddevice.ui.adapter.AdapterCellinfoList;
import com.wangtao.androiddevice.utils.ReflectUtils;
import com.wangtao.androiddevice.utils.SignMath;
import com.wangtao.universallylibs.BaseActivity;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class CellinfoRefershActivity extends BaseActivity {
    private ListView mListView, mListView2;
    private TextView tvNetwork1;
    private TextView tvNetwork2;
    private TelephonyManager tm;

    @Override
    public void initShowLayout() {
        setContentView(R.layout.activity_cellinfo_refersh);
        mListView = (ListView) findViewById(R.id.cell_info_listview);
        mListView2 = (ListView) findViewById(R.id.cell_info_listview2);
        tvNetwork1 = (TextView) findViewById(R.id.refersh_cellinfo_tv1);
        tvNetwork2 = (TextView) findViewById(R.id.refersh_cellinfo_tv2);
        tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
    }

    @Override
    public void setAllData() {
        SubscriptionManager mSubscriptionManager = (SubscriptionManager) getSystemService(Context.TELEPHONY_SUBSCRIPTION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            List<SubscriptionInfo> dataList = mSubscriptionManager
                    .getActiveSubscriptionInfoList();
            if (dataList == null) {
                doShowMesage("手机的没有任何双卡信息");
                return;
            }
            doLogMsg("'有多少张卡的数据：" + dataList.size());
            for (int i = 0; i < dataList.size(); i++) {
                getOtherParams(dataList.get(i).getSubscriptionId(), i);
            }
        }
        doLogMsg("getNetworkTypeService:" + tm.getNetworkType() + "," + tm.getNetworkOperatorName() + "," + tm.getNetworkOperator());
        List<CellInfo> list = tm.getAllCellInfo();
        if (list != null) {
            mListView.setAdapter(new AdapterCellinfoList(mContext, list, tm.getNetworkType(), tm.getSimOperator()));
        }
    }

    private void showCellInfoList(List<CellInfo> cellInfoList) {

//            int rssi = info.getRssi();
//            if ("2G".equals(SignMath.getCurrentNetworkType234G(mContext, tm.getNetworkTypeService()))) {
//                rssi = -113 + 2 * rssi;
//            } else {
//                rssi = -140 + rssi;
//            }
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

        if (index == 0) {
            tm.listen(new MyPhotoListener1(subId), event);
        } else {
            tm.listen(new MyPhotoListener2(subId), event);
        }

//        linearScroll.addView(addShowTxtContent(index + "卡信号质量：", ""));
//        linearScroll.addView(addShowTxtContent(index + "卡基站信息：", ""));
//        linearScroll.addView(addShowTxtContent(index + "信号质量(db1)：", ""));
//        linearScroll.addView(addShowTxtContent(index + "isGsm：", ""));

    }

    private class MyPhotoListener1 extends PhoneStateListener {


        private List<CellInfo> cellInfoList;
        private int networkName;
        private String operName;

        public MyPhotoListener1(int subId) {
            ReflectUtils.setFieldValue(this, "mSubId", subId);
        }

        private void setCellinfoListData() {
            if (TextUtils.isEmpty(operName)) {
                return;
            }
            if (cellInfoList == null || cellInfoList.isEmpty()) {
                return;
            }
            mListView.setAdapter(new AdapterCellinfoList(mContext, cellInfoList, networkName, operName));
        }

        /**
         * 返回手机当前所处的位置
         *
         * @param location
         */
        @Override
        public void onCellLocationChanged(CellLocation location) {
            super.onCellLocationChanged(location);
            doLogMsg("111onCellLocationChanged:" + location);
//
        }

        @Override
        public void onSignalStrengthsChanged(SignalStrength signalStrength) {
            super.onSignalStrengthsChanged(signalStrength);
            doLogMsg("onSignalStrengthsChanged:" + signalStrength.toString());
//            updataShowTxtContent(linearScroll, index + "卡RSRP：", SignalOperatUtils.getAllParams(signalStrength.toString(), 8));
//            updataShowTxtContent(linearScroll, index + "卡RSRQ：", SignalOperatUtils.getAllParams(signalStrength.toString(), 9));
//            if (signalStrength.isGsm()) {
//                updataShowTxtContent(linearScroll, index + "卡信号质量：", signalStrength.getGsmSignalStrength()
//                        + "=" + (-113 + 2 * signalStrength.getGsmSignalStrength()) + "dbm");
//            } else {
//                if (NetworkInfotion.getProvidersInt(tm) <= 2) {
//                    updataShowTxtContent(linearScroll, index + "卡信号质量：", signalStrength.getCdmaDbm() + "");
//                } else {
//                    updataShowTxtContent(linearScroll, index + "卡信号质量：", signalStrength.getEvdoDbm() + "");
//                }
//
//            }

        }

        @Override
        public void onCellInfoChanged(List<CellInfo> cellInfo) {
            super.onCellInfoChanged(cellInfo);
            this.cellInfoList = cellInfo;
            doLogMsg("onCellInfoChanged:" + cellInfo);
            setCellinfoListData();
        }


        @Override
        public void onDataConnectionStateChanged(int state, int networkType) {
            super.onDataConnectionStateChanged(state, networkType);
            doLogMsg("onDataConnectionStateChanged:" + networkType);
        }

        @Override
        public void onMessageWaitingIndicatorChanged(boolean mwi) {
            super.onMessageWaitingIndicatorChanged(mwi);
            doLogMsg("onMessageWaitingIndicatorChanged:" + mwi);
        }

        /**
         * 0 1 voice home data home 海航移动 海航移动 46001 海航移动 海航移动 46001  UMTS Unknown CSS not supported -1 -1
         * RoamInd=-1 DefRoamInd=-1 EmergOnly=false IsDataRoamingFromRegistration=false
         *
         * @param serviceState
         */
        @Override
        public void onServiceStateChanged(ServiceState serviceState) {
            super.onServiceStateChanged(serviceState);
            doLogMsg("onServiceStateChanged:" + serviceState);
            String str = getNetworkTypeService(serviceState);
            networkName = Integer.parseInt(str);
            operName = serviceState.getOperatorNumeric();
            tvNetwork1.setText("1卡网络类型：" + SignMath.getNetorkTypeName(networkName) + "," + SignMath.getNetworkClassByTypeName(networkName) + "," +
                    serviceState.getOperatorNumeric());
            setCellinfoListData();
        }

    }

    private class MyPhotoListener2 extends PhoneStateListener {


        private List<CellInfo> cellInfoList;
        private int networkName;
        private String operName;
        private CellLocation cellLocation;

        public MyPhotoListener2(int subId) {
            ReflectUtils.setFieldValue(this, "mSubId", subId);
        }

        private void setCellinfoListData() {
            if (TextUtils.isEmpty(operName)) {
                return;
            }
            if (cellLocation != null) {
                ArrayList<CellLocation> listCellLocation = new ArrayList<>();
                listCellLocation.add(cellLocation);
                mListView2.setAdapter(new AdapterCellLocation(mContext, listCellLocation));
            }
            if (cellInfoList == null || cellInfoList.isEmpty()) {
                return;
            }
            mListView2.setAdapter(new AdapterCellinfoList(mContext, cellInfoList, networkName, operName));
        }

        /**
         * 返回手机当前所处的位置
         *
         * @param location
         */
        @Override
        public void onCellLocationChanged(CellLocation location) {
            super.onCellLocationChanged(location);
            doLogMsg("2222onCellLocatioChanged:" + location);
            this.cellLocation = location;
            setCellinfoListData();
        }

        @Override
        public void onSignalStrengthsChanged(SignalStrength signalStrength) {
            super.onSignalStrengthsChanged(signalStrength);
            doLogMsg("22onSignalStrengthsChanged:" + signalStrength.toString());
//            updataShowTxtContent(linearScroll, index + "卡RSRP：", SignalOperatUtils.getAllParams(signalStrength.toString(), 8));
//            updataShowTxtContent(linearScroll, index + "卡RSRQ：", SignalOperatUtils.getAllParams(signalStrength.toString(), 9));
//            if (signalStrength.isGsm()) {
//                updataShowTxtContent(linearScroll, index + "卡信号质量：", signalStrength.getGsmSignalStrength()
//                        + "=" + (-113 + 2 * signalStrength.getGsmSignalStrength()) + "dbm");
//            } else {
//                if (NetworkInfotion.getProvidersInt(tm) <= 2) {
//                    updataShowTxtContent(linearScroll, index + "卡信号质量：", signalStrength.getCdmaDbm() + "");
//                } else {
//                    updataShowTxtContent(linearScroll, index + "卡信号质量：", signalStrength.getEvdoDbm() + "");
//                }
//
//            }

        }

        @Override
        public void onCellInfoChanged(List<CellInfo> cellInfo) {
            super.onCellInfoChanged(cellInfo);
            doLogMsg("222onCellInfoChanged:" + cellInfo);
            this.cellInfoList = cellInfo;
            setCellinfoListData();
        }

        @Override
        public void onDataConnectionStateChanged(int state, int networkType) {
            super.onDataConnectionStateChanged(state, networkType);
            doLogMsg("222onDataConnectionStateChanged:" + networkType);
        }

        @Override
        public void onMessageWaitingIndicatorChanged(boolean mwi) {
            super.onMessageWaitingIndicatorChanged(mwi);
            doLogMsg("222onMessageWaitingIndicatorChanged:" + mwi);
        }

        /**
         * 0 1 voice home data home 海航移动 海航移动 46001 海航移动 海航移动 46001  UMTS Unknown CSS not supported -1 -1
         * RoamInd=-1 DefRoamInd=-1 EmergOnly=false IsDataRoamingFromRegistration=false
         *
         * @param serviceState
         */
        @Override
        public void onServiceStateChanged(ServiceState serviceState) {
            super.onServiceStateChanged(serviceState);
            doLogMsg("22onServiceStateChanged:" + serviceState.toString());
            //mRilVoiceRadioTechnology
            //mRilDataRadioTechnology
            String str = getNetworkTypeService(serviceState);
            this.networkName = Integer.parseInt(str);
            this.operName = serviceState.getOperatorNumeric();
            tvNetwork2.setText("2卡网络类型：" + SignMath.getNetorkTypeName(Integer.parseInt(str)) + "," + SignMath.getNetworkClassByTypeName(networkName) +
                    "," + serviceState.getOperatorNumeric());
            setCellinfoListData();

        }


    }

    private String getNetworkTypeService(ServiceState serviceState) {
        try {
//            Field fidld = ServiceState.class.getDeclaredField("mRilDataRadioTechnology");
//            fidld.setAccessible(true);
//            doLogMsg("22Voice:" + fidld.get(serviceState));
//            Field fidld2 = ServiceState.class.getDeclaredField("mRilVoiceRadioTechnology");
            Field fidld2 = ServiceState.class.getDeclaredField("mRilDataRadioTechnology");
            fidld2.setAccessible(true);
            String res = fidld2.get(serviceState) + "";
            if (TextUtils.equals(res, "0")) {
                Field fidld = ServiceState.class.getDeclaredField("mRilVoiceRadioTechnology");
                fidld.setAccessible(true);
                res = fidld.get(serviceState) + "";
            }

            return res;
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e2) {
            e2.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
