package com.wangtao.androiddevice.utils;

import android.content.Context;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.wangtao.universallylibs.utils.SaveCache;


/**
 * @author wangtao 11-13
 */
public class LocationUtils {
    private final BDLocationListener listener;
    private LocationClient mLocClient;
    private Context mContext;
    private SaveCache save;
    private BDLocation bdLocal;

    public LocationUtils(Context context, BDLocationListener listener) {

        mContext = context;
        save = new SaveCache(context);
        mLocClient = new LocationClient(context);
        mLocClient.registerLocationListener(listener);
        this.listener = listener;
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);
        option.setCoorType("bd09ll");
        option.setAddrType("all");
        option.setScanSpan(10000);
        option.setIsNeedAddress(true);
        option.setIsNeedLocationPoiList(true);
        option.disableCache(false);
        option.setIsNeedLocationPoiList(true);

        mLocClient.setLocOption(option);
        mLocClient.start();
    }

    public void stopLocation() {
        if (mLocClient != null && listener != null) {
            mLocClient.stop();
            mLocClient.unRegisterLocationListener(listener);
        }
    }


}
