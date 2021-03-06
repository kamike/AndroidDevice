package com.wangtao.androiddevice.ui;

import android.view.View;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.Overlay;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.wangtao.androiddevice.R;
import com.wangtao.androiddevice.utils.LocationUtils;
import com.wangtao.universallylibs.BaseActivity;

public class MapShowActivity extends BaseActivity {
    private MapView mMapView;
    private Overlay temLocalMark;

    @Override
    public void initShowLayout() {
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_map_show);
        mMapView = (MapView) findViewById(R.id.map_show_mapview);
    }

    @Override
    public void setAllData() {

    }

    public void onclickLocaltion(View view) {
        new LocationUtils(mContext, new BDLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation bdLocation) {
                if (bdLocation == null) {
                    Toast.makeText(MapShowActivity.this, "定位失败", Toast.LENGTH_LONG).show();
                    return;
                }
                LatLng latlng = new LatLng(bdLocation.getLatitude(), bdLocation.getLongitude());
                MapStatusUpdate updata = MapStatusUpdateFactory
                        .newLatLng(latlng);
                mMapView.getMap().animateMapStatus(updata);
                OverlayOptions option = new MarkerOptions().position(latlng)
                        .icon(BitmapDescriptorFactory
                                .fromResource(R.mipmap.icon_position));
                if (temLocalMark != null) {
                    temLocalMark.remove();
                }
                temLocalMark = mMapView.getMap().addOverlay(option);


            }
        });
    }

}
