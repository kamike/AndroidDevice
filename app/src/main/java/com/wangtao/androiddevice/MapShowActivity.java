package com.wangtao.androiddevice;

import android.view.View;

import com.wangtao.universallylibs.BaseActivity;

public class MapShowActivity extends BaseActivity {
//    private MapView mMapView;
//    private Overlay temLocalMark;

    @Override
    public void initShowLayout() {
//        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_map_show);
//        mMapView = (MapView) findViewById(R.id.map_show_mapview);
    }

    @Override
    public void setAllData() {

    }

    public void onclickLocaltion(View view) {
//        new LocationUtils(mContext, new BDLocationListener() {
//            @Override
//            public void onReceiveLocation(BDLocation bdLocation) {
//                if (bdLocation == null) {
//                    Toast.makeText(MapShowActivity.this, "定位失败", Toast.LENGTH_LONG).show();
//                    return;
//                }
//                LatLng latlng = new LatLng(bdLocation.getLatitude(), bdLocation.getLongitude());
//                MapStatusUpdate updata = MapStatusUpdateFactory
//                        .newLatLng(latlng);
//                mMapView.getMap().animateMapStatus(updata);
//                OverlayOptions option = new MarkerOptions().position(latlng)
//                        .icon(BitmapDescriptorFactory
//                                .fromResource(R.mipmap.icon_position));
//                if (temLocalMark != null) {
//                    temLocalMark.remove();
//                }
//                temLocalMark = mMapView.getMap().addOverlay(option);
//
//
//            }
//        });
    }

}
