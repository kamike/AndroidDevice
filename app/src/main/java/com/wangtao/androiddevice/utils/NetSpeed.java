package com.wangtao.androiddevice.utils;

import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.TrafficStats;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class NetSpeed {
	private final static String TAG = "NetSpeed";
	private long preRxBytes = 0;
	private Timer mTimer = null;
	private Context mContext;
	private static NetSpeed mNetSpeed;
	private Handler mHandler;

	private NetSpeed(Context mContext, Handler mHandler) {
		this.mContext = mContext;
		this.mHandler = mHandler;
	}

	public static NetSpeed getInstant(Context mContext, Handler mHandler) {
		if (mNetSpeed == null) {
			mNetSpeed = new NetSpeed(mContext, mHandler);
		}
		return mNetSpeed;
	}

	private long getNetworkRxBytes() {
		return TrafficStats.getTotalRxBytes();
	}

	public int getNetSpeed() {

		long curRxBytes = getNetworkRxBytes();
		long bytes = curRxBytes - preRxBytes;
		preRxBytes = curRxBytes;
		int kb = (int) Math.floor(bytes / 1024.0f + 0.5);
		return kb;
	}

	public void startCalculateNetSpeed() {
		preRxBytes = getNetworkRxBytes();
		if (mTimer != null) {
			mTimer.cancel();
			mTimer = null;
		}
		if (mTimer == null) {
			mTimer = new Timer();
			mTimer.schedule(new TimerTask() {
				@Override
				public void run() {
					Message msg = new Message();
					msg.what = 1;
					msg.arg1 = getNetSpeed();
					mHandler.sendMessage(msg);
				}
			}, 1000, 1000);
		}
	}

	public void stopCalculateNetSpeed() {
		if (mTimer != null) {
			mTimer.cancel();
			mTimer = null;
		}
	}


}
