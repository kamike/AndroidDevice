package com.wangtao.androiddevice;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.telephony.CellLocation;
import android.telephony.NeighboringCellInfo;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.wangtao.androiddevice.bean.AllParamsName;
import com.wangtao.androiddevice.utils.CTelephoneInfo;
import com.wangtao.androiddevice.utils.DeviceInfo;
import com.wangtao.androiddevice.utils.NetSpeed;
import com.wangtao.androiddevice.utils.NetworkInfotion;
import com.wangtao.androiddevice.utils.StaticMthod;
import com.wangtao.universallylibs.BaseActivity;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.List;

public class MainActivity extends BaseActivity implements AllParamsName {
    private LinearLayout linearScroll;
    private boolean isAddLat = false;
    private TelephonyManager tm;

    @Override
    public void initShowLayout() {
        setContentView(R.layout.activity_main);
        linearScroll = (LinearLayout) findViewById(R.id.main_scroll_linear);
        tm = (TelephonyManager) this.getSystemService(TELEPHONY_SERVICE);

    }

    @Override
    public void setAllData() {
        linearScroll.removeAllViews();
        final Animation animRefresh = AnimationUtils.loadAnimation(mContext, R.anim.rotate_return);
        findViewById(R.id.main_refersh_iv).startAnimation(animRefresh);
        setTitle("手机信息");
        linearScroll.addView(addShowTxtContent("采集识别码(SID)：", StaticMthod.getRandomSID()));
        linearScroll.addView(addShowTxtContent("维度(wd)：", "-1"));
        linearScroll.addView(addShowTxtContent("经度(jd)：", "-1"));
        linearScroll.addView(addShowTxtContent("实时速度(v)：", "m/s"));
        linearScroll.addView(addShowTxtContent(cityCode, "定位中.."));
        isAddLat = false;
   /*     new LocationUtils(mContext, new BDLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation bdLocation) {
                animRefresh.cancel();
                if (bdLocation == null) {
                    updataShowTxtContent(linearScroll, cityCode, "定位失败");
                    return;
                }
                updataShowTxtContent(linearScroll, cityCode, bdLocation.getCity() + "," + bdLocation.getCityCode());
                updataShowTxtContent(linearScroll, "维度(wd)：", bdLocation.getLatitude() + "");
                updataShowTxtContent(linearScroll, "经度(jd)：", bdLocation.getLongitude() + "");
                updataShowTxtContent(linearScroll, "实时速度(v)：", bdLocation.getSpeed() + "m/s");
            }
        });*/
        linearScroll.addView(addShowTxtContent("品牌(sj_brand)：", android.os.Build.BRAND));
        linearScroll.addView(addShowTxtContent("型号(sj_model)：", android.os.Build.MODEL));

        CTelephoneInfo telephonyInfo = CTelephoneInfo.getInstance(this);
        if (telephonyInfo != null) {
            telephonyInfo.setCTelephoneInfo();
            linearScroll.addView(addShowTxtContent("手机号码2(phone2)：", telephonyInfo.getINumeric2()));
            linearScroll.addView(addShowTxtContent("卡号2(iccid2)：", telephonyInfo.getImeiSIM2()));

        }

        //例如： PHONE_TYPE_NONE  无信号
        // PHONE_TYPE_GSM   GSM信号
        // PHONE_TYPE_CDMA  CDMA信号

        linearScroll.addView(addShowTxtContent("手机类型(sj_kind)：", tm.getPhoneType() + ""));
        linearScroll.addView(addShowTxtContent("卡槽类型(c_kind)：", (DeviceInfo.initMtkDoubleSim(mContext)) ? "1" : "2"));
        linearScroll.addView(addShowTxtContent("系统版本(osver)：", Build.VERSION.SDK_INT + ""));
        linearScroll.addView(addShowTxtContent("手机串号(IMEI)：", tm.getDeviceId()));
        linearScroll.addView(addShowTxtContent("卡号1(iccid1)：", tm.getSimSerialNumber()));
        linearScroll.addView(addShowTxtContent("手机号码1(phone1)：", TextUtils.isEmpty(tm.getLine1Number()) ? "None" : tm.getLine1Number()));
        linearScroll.addView(addShowTxtContent("卡类型1(kakind2)：", DeviceInfo.getSimUsim(tm)));
        linearScroll.addView(addShowTxtContent("卡类型2(kakind1)：", ""));
        linearScroll.addView(addShowTxtContent("1槽运营商(yys1)：", DeviceInfo.getProvidersName(tm)));
        linearScroll.addView(addShowTxtContent("2槽运营商(yys2)：", ""));
        EditText et = new EditText(mContext);
        et.setHint("输入测试名称");
        et.clearFocus();
        linearScroll.addView(et);
        linearScroll.addView(addShowTxtContent("登陆账号(login)：", ""));
        try {
            linearScroll.addView(addShowTxtContent("APP核心版本(APP_VER)：",
                    getPackageManager().getPackageInfo(
                            getPackageName(), 0).versionName));
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        linearScroll.addView(addShowTxtContent("APP属性(APP_U)：", ""));
        linearScroll.addView(addShowTxtContent("创建时间(rec_time)：", StaticMthod.getFormDate(new Date().getTime())));
        linearScroll.addView(addShowTxtContent("是否已上传(is_up)：", "N"));


        View view = new View(mContext);
        view.setBackgroundColor(Color.GRAY);
        view.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 1));
        linearScroll.addView(view);
        linearScroll.addView(addShowTxtContent("表2", ""));
        addTabelTwo();
        Class<?> telephonyClass;
        try {
            telephonyClass = Class.forName(tm.getClass().getName());
            Method[] methods = telephonyClass.getMethods();
            for (int idx = 0; idx < methods.length; idx++) {
                doLogMsg(methods[idx] + " declared by "
                        + methods[idx].getDeclaringClass());
            }
            doLogMsg("双卡信息");
        } catch (ClassNotFoundException e) {
            doLogMsg("单卡信息");
            e.printStackTrace();
        }

    }

    private void addTabelTwo() {
        linearScroll.addView(addShowTxtContent("网络制式(w1)：", NetworkInfotion.getNetworkType234G(mContext)));
        linearScroll.addView(addShowTxtContent("网络制式(w2)：", ""));
        linearScroll.addView(addShowTxtContent("信号强度(dbm1)：", "获取中.."));
        linearScroll.addView(addShowTxtContent("信号强度(dbm2)：", ""));
        linearScroll.addView(addShowTxtContent("信号质量(db1)：", ""));
        linearScroll.addView(addShowTxtContent("信号质量(db2)：", ""));
        tm.listen(new PhoneStateListener() {
            @Override
            public void onSignalStrengthsChanged(SignalStrength signalStrength) {
                super.onSignalStrengthsChanged(signalStrength);
                if (signalStrength == null) {
                    return;
                }
                /**
                 *  signalStrength.isGsm()        是否GSM信号 2G or 3G
                 signalStrength.getCdmaDbm();     联通3G 信号强度
                 signalStrength.getCdmaEcio();    联通3G 载干比
                 signalStrength.getEvdoDbm();     电信3G 信号强度
                 signalStrength.getEvdoEcio();    电信3G 载干比
                 signalStrength.getEvdoSnr();     电信3G 信噪比
                 signalStrength.getGsmSignalStrength();  2G 信号强度
                 signalStrength.getGsmBitErrorRate();    2G 误码率
                 */
//                doLogMsg("是否是2G还是3G：" + signalStrength.isGsm());
                if (signalStrength.isGsm()) {
                    updataShowTxtContent(linearScroll, "信号强度(dbm1)：", -113 + (2 * signalStrength.getGsmSignalStrength()) + "")
                    ;
                } else {
                    if (NetworkInfotion.getProvidersInt(tm) <= 2) {
                        updataShowTxtContent(linearScroll, "信号强度(dbm1)：", signalStrength.getCdmaDbm() + "");
                    } else {
                        updataShowTxtContent(linearScroll, "信号强度(dbm1)：", signalStrength.getEvdoDbm() + "");
                    }

                }
                updataShowTxtContent(linearScroll, "信号质量(db1)：", signalStrength.getGsmSignalStrength() + "");

            }
        }, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);

        List<NeighboringCellInfo> list = tm.getNeighboringCellInfo();
        doLogMsg("小区列表:" + list);
        for (int i = 0; i < list.size(); i++) {
            linearScroll.addView(addShowTxtContent("小区号LAC(" + i + ")：", list.get(i).getLac() + ""));
            linearScroll.addView(addShowTxtContent("小区号CID(" + i + ")：", list.get(i).getCid() + ""));
            linearScroll.addView(addShowTxtContent("小区号Rssi(" + i + ")：", list.get(i).getRssi() + ""));
        }
        try {
            CellLocation cel = tm.getCellLocation();
            GsmCellLocation gsm = (GsmCellLocation) cel;
            if (gsm != null) {
                linearScroll.addView(addShowTxtContent("小区号LAC(ci_1_0)：", gsm.getLac() + ""));
                linearScroll.addView(addShowTxtContent("CID(CID)：", gsm.getCid() + ""));
                doLogMsg("cid:" + gsm.getCid());
                doLogMsg("psc:" + gsm.getPsc());
                doLogMsg("lac:" + gsm.getLac());
            }
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
        linearScroll.addView(addShowTxtContent("交换机号(sw1)：", ""));
        linearScroll.addView(addShowTxtContent("IP地址(ip1)：", NetworkInfotion.getLocalIpAddress()));
        linearScroll.addView(addShowTxtContent("IP地址(ip2)：", ""));
        linearScroll.addView(addShowTxtContent("当前网速(ip2)：", "0kb"));
        if (speed == null) {
            speed = NetSpeed.getInstant(this, speedHandler);
            speed.startCalculateNetSpeed();
        }
        registerReceiver(mBatInfoReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        linearScroll.addView(addShowTxtContent("当前电量(bet)：", ""));
        linearScroll.addView(addShowTxtContent("电池电压(u)：", ""));
        linearScroll.addView(addShowTxtContent("电池温度(t)：", ""));
        if (tm.isNetworkRoaming()) {
            linearScroll.addView(addShowTxtContent("是否漫游(RO)：", "Y"));
        } else {
            linearScroll.addView(addShowTxtContent("是否漫游(RO)：", "N"));
        }
        testData();
    }

    private void testData() {
        CTelephoneInfo telephonyInfo = CTelephoneInfo.getInstance(this);
        if (telephonyInfo == null) {
            return;
        }
        telephonyInfo.setCTelephoneInfo();
        String imeiSIM1 = telephonyInfo.getImeiSIM1();
        String imeiSIM2 = telephonyInfo.getImeiSIM2();
        String iNumeric1 = telephonyInfo.getINumeric1();
        String iNumeric2 = telephonyInfo.getINumeric2();
        boolean network1 = telephonyInfo.isDataConnected1();
        boolean network2 = telephonyInfo.isDataConnected2();
        boolean isSIM1Ready = telephonyInfo.isSIM1Ready();
        boolean isSIM2Ready = telephonyInfo.isSIM2Ready();
        boolean isDualSIM = telephonyInfo.isDualSim();
        StringBuilder sb = new StringBuilder();
        sb.append("是否是双卡：").append(DeviceInfo.initMtkDoubleSim(mContext) + "\n");
        sb.append("imeiSIM1:" + imeiSIM1).append(",\n");
        sb.append("imeiSIM2:" + imeiSIM2).append(",\n");
        sb.append("iNumeric1:" + iNumeric1).append(",\n");
        sb.append("iNumeric2:" + iNumeric2).append(",\n");
        sb.append("network1:" + network1).append(",\n");
        sb.append("network2:" + network2).append(",\n");
        sb.append("isSIM1Ready:" + isSIM1Ready).append(",\n");
        sb.append("isSIM2Ready:" + isSIM2Ready).append(",\n");
        sb.append("isDualSIM:" + isDualSIM).append(",");
        linearScroll.addView(addShowTxtContent("单卡双卡测试：\n", sb.toString()));

    }


    private BroadcastReceiver mBatInfoReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            /*
             * 如果捕捉到的action是ACTION_BATTERY_CHANGED， 就运行onBatteryInfoReceiver()
             */
            if (Intent.ACTION_BATTERY_CHANGED.equals(action)) {
                int n = intent.getIntExtra("level", 0);    //目前电量
                updataShowTxtContent(linearScroll, "当前电量(bet)：", n + "");
                int u = intent.getIntExtra("voltage", 0);  //电池电压
                updataShowTxtContent(linearScroll, "电池电压(u)：", u * 0.001f + "");
                int t = intent.getIntExtra("temperature", 0);  //电池温度
                updataShowTxtContent(linearScroll, "电池温度(t)：", t * 0.1f + "");
            }
        }
    };

    public void onclickMap(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("功能选择");
        //    指定下拉列表的显示数据
        final String[] array = {"地图", "双卡测试", "硬件测试"};
        //    设置一个下拉的列表选择项
        builder.setItems(array, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                switch (which) {
                    case 0:
//                        doStartOter(MapShowActivity.class);
                        break;
                    case 1:
                        doStartOter(DoubleCardActivity.class);
                        break;
                    case 2:
                        doStartOter(HardwareActivity.class);

                        break;
                    default:

                        break;
                }

            }
        });
        builder.setPositiveButton("取消", null);
        builder.show();
    }

    public void onclickRefersh(View view) {
        setAllData();

    }

    private Handler speedHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                updataShowTxtContent(linearScroll, "当前网速(ip2)：", msg.arg1 + "kb/s");
            }

        }
    };
    private NetSpeed speed;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        speed.stopCalculateNetSpeed();
    }

    public void onclickDoubleCard(View view) {
        doStartOter(DoubleCardActivity.class);
    }
}
