package com.wangtao.androiddevice.ui;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.widget.LinearLayout;

import com.wangtao.androiddevice.R;
import com.wangtao.universallylibs.BaseActivity;

import java.util.Arrays;

public class HardwareActivity extends BaseActivity implements SensorEventListener {
    private LinearLayout linearScroll;
    private SensorManager sensorManager;


    @Override
    public void initShowLayout() {
        setContentView(R.layout.activity_hardware);
        linearScroll = (LinearLayout) findViewById(R.id.hardware_scroll_linear);

    }

    @Override
    public void setAllData() {
        // 获取系统的传感器管理服务
        sensorManager = (SensorManager) getSystemService(
                Context.SENSOR_SERVICE);
        // 为系统的加速度传感器注册监听器
        sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION),
                SensorManager.SENSOR_DELAY_NORMAL);
        //光传感器
        sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT),
                SensorManager.SENSOR_DELAY_FASTEST);
        sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE),
                SensorManager.SENSOR_DELAY_GAME);
        sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE),
                SensorManager.SENSOR_DELAY_NORMAL);

        linearScroll.addView(addShowTxtContent("硬件信息测试", ""));
        linearScroll.addView(addShowTxtContent("加速度x：", ""));
        linearScroll.addView(addShowTxtContent("加速度y：", ""));
        linearScroll.addView(addShowTxtContent("加速度z：", ""));
        linearScroll.addView(addShowTxtContent("瞬时速度：", ""));
        linearScroll.addView(addShowTxtContent("光线亮度：", ""));
        linearScroll.addView(addShowTxtContent("旋转：", ""));

    }

    private float tempA0 = 0;
    private float speed;
    private long tempTime;
    private float tempSpeed;
    private float subDIstance=0.005f;
    @Override
    public void onSensorChanged(SensorEvent event) {
        float[] values = event.values;
        switch (event.sensor.getType()) {
            case Sensor.TYPE_LINEAR_ACCELERATION:
                if(values[0]<=subDIstance){
                   values[0]=0;
                }
                if(values[1]<=subDIstance){
                    values[1]=0;
                }
                if(values[2]<=subDIstance){
                    values[2]=0;
                }
                doLogMsg(Arrays.toString(values));

                updataShowTxtContent(linearScroll, "加速度x：", "x:" + values[0]);
                updataShowTxtContent(linearScroll, "加速度y：", "x:" + values[1]);
                updataShowTxtContent(linearScroll, "加速度z：", "x:" + values[2]);

                float currentA = (float) Math.sqrt(values[0] * values[0] + values[1] * values[1]+values[2]*values[2]);

                speed += (currentA-tempA0) * (Math.abs(System.currentTimeMillis() - tempTime));

                tempTime = System.currentTimeMillis();
                tempA0 = currentA;
                tempSpeed=speed;
                if(tempSpeed<=subDIstance){
                    tempSpeed=0;
                }
                updataShowTxtContent(linearScroll, "瞬时速度：", tempSpeed + "m/s");
                break;
            case Sensor.TYPE_LIGHT:
                updataShowTxtContent(linearScroll, "光线亮度：", "" + values[0]);
                break;
            case Sensor.TYPE_AMBIENT_TEMPERATURE:
                break;
            case Sensor.TYPE_GYROSCOPE:
                updataShowTxtContent(linearScroll, "旋转：", "x:" + values[0] + ",y:" + values[1]);

                break;
        }
    }

    // 当传感器精度改变时回调该方法。
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {


    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        sensorManager.unregisterListener(this);

    }
}
