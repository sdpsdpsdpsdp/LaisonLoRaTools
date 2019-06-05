package com.laisontech.laisonloratools.location.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.IBinder;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.google.android.gms.maps.model.LatLng;
import com.laisontech.laisonloratools.location.event.LocationEvent;
import com.laisontech.laisonloratools.location.utils.LocationUtil;

import org.greenrobot.eventbus.EventBus;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by SDP on 2017/9/25.
 */

public class DiffLocationService extends Service implements GoogleLocationService.ILocationListener {
    //定位类型
    public static final int LOCATION_TYPE_BAIDU = 1;
    public static final int LOCATION_TYPE_GOOGLE = 2;

    @IntDef({LOCATION_TYPE_BAIDU, LOCATION_TYPE_GOOGLE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Type {

    }

    //百度定位
    private static final String ACTION_BAIDU_LOCATION = "com.laisontech.action.baidu_location";
    //每隔3秒中获取一次定位
    private static final int SCAN_TIMES = 3 * 1000;
    private Context mContext;
    //google定位管理类
    protected LocationManager mLocationManager;
    //要发送的定位
    protected Location mCurrentLocation;
    //百度定位
    BaiduLocationService mBaiduLocationService = null;
    private GoogleLocationService mGoogleLocationService = null;
    private BaiduLocationService locationService;

    //获取当前点的定位信息
    public Location getCurrentLocation() {
        return mCurrentLocation;
    }

    public volatile static @Type
    int LocationType = LOCATION_TYPE_BAIDU;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = DiffLocationService.this;
        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            String action = intent.getAction();
            if (action != null) {
                switch (action) {
                    case ACTION_BAIDU_LOCATION:
                        startBaiduLocation();
                        break;
                }
            }
        }
        return START_STICKY;
    }

    /**
     * 启用百度定位
     */
    private void startBaiduLocation() {
        mBaiduLocationService = getLocationService();
        if (mBaiduLocationService != null) {
            mBaiduLocationService.registerListener(mBaiDuLocationListener);
            mBaiduLocationService.setLocationOption(mBaiduLocationService.getDefaultLocationClientOption(SCAN_TIMES));
            mBaiduLocationService.start();
        }
    }

    //百度定位的接口
    private BDLocationListener mBaiDuLocationListener = bdLocation -> {
        if (bdLocation != null) {//获取到的百度定位不为空时
            Location buildLocation = buildLocation(bdLocation);
            if (buildLocation == null) {//构建的数据为空时，则使用google定位
                startGoogleLocation();
            } else {//不为空，则使用百度定位
                LocationType = LOCATION_TYPE_BAIDU;
                boolean outOfChina = LocationUtil.outOfChina(buildLocation.getLatitude(), buildLocation.getLongitude());
                if (!outOfChina) { //如果当前定位在国内，直接发送数据
                    sendLocationBroadcastToUI();
                } else {//如果是在国外，启用google定位
                    startGoogleLocation();
                }
            }
        } else {//如果获取到的百度定位为空时，启用google定位
            startGoogleLocation();
        }
    };

    //将百度定位构建成google定位
    private Location buildLocation(BDLocation bdLocation) {
        if (bdLocation == null) return null;
        if (bdLocation.getLatitude() == 4.9E-324 || bdLocation.getLongitude() == 4.9E-324)
            return null;
        if (mLocationManager == null) {
            mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        }
        if (mLocationManager == null) return null;
        if (mCurrentLocation == null) {
            mCurrentLocation = new Location(LocationUtil.getBestProvider(mLocationManager));
        }
        mCurrentLocation.setLatitude(bdLocation.getLatitude());
        mCurrentLocation.setLongitude(bdLocation.getLongitude());
        mCurrentLocation.setAltitude(bdLocation.getAltitude());
        return mCurrentLocation;
    }


    /**
     * 启动google定位
     */
    private void startGoogleLocation() {
        if (mGoogleLocationService == null) {
            mGoogleLocationService = new GoogleLocationService(mContext);
        }
        mGoogleLocationService.setRefreshTime(SCAN_TIMES).setMeterPosition(0).registerGoogleLocationListener(this);
        Location lastLocation = mGoogleLocationService.getGoogleLocation();
        if (lastLocation == null) {
            sendLocationBroadcastToUI();
        } else {
            setGoogleLocation(lastLocation);
        }
    }

    //使用接口获取到google定位了
    @Override
    public void onSuccessLocation(Location location) {
        if (location == null) return;
        finishedBaiduLocation();//结束百度
        setGoogleLocation(location);
    }

    private void setGoogleLocation(Location location) {
        LocationType = LOCATION_TYPE_GOOGLE;
        mCurrentLocation = location;
        sendLocationBroadcastToUI();
    }

    private void sendLocationBroadcastToUI() {
        Location currentLocation = getCurrentLocation();
        if (currentLocation != null) {
            LatLng latLng = LocationUtil.transformFromWGSToGCJ(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()));
            currentLocation.setLatitude(latLng.latitude);
            currentLocation.setLongitude(latLng.longitude);
            EventBus.getDefault().post(new LocationEvent(currentLocation));
        }
    }

    /**
     * 开启定位服务
     */
    public static void startLocationService(Context context) {
        Intent intent = new Intent();
        intent.setAction(ACTION_BAIDU_LOCATION);
        intent.setClass(context, DiffLocationService.class);
        context.startService(intent);
    }

    /**
     * 关闭定位服务
     */
    public static void finishedLocationService(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, DiffLocationService.class);
        context.stopService(intent);
    }


    //字节码计数器
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LocationType = LOCATION_TYPE_BAIDU;
        finishedBaiduLocation();
        finishedGoogleLocation();
    }


    private void finishedBaiduLocation() {
        if (mBaiduLocationService != null && mBaiDuLocationListener != null) {
            mBaiduLocationService.unregisterListener(mBaiDuLocationListener);
            mBaiduLocationService.stop();
            mBaiduLocationService = null;
        }
    }

    private void finishedGoogleLocation() {
        if (mGoogleLocationService != null) {
            mGoogleLocationService.unRegisterGoogleLocationListener();
            mGoogleLocationService = null;
        }
    }

    public BaiduLocationService getLocationService() {
        return locationService;
    }
}
